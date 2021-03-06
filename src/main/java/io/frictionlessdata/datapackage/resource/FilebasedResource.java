package io.frictionlessdata.datapackage.resource;

import io.frictionlessdata.datapackage.Dialect;
import io.frictionlessdata.datapackage.exceptions.DataPackageException;
import io.frictionlessdata.tableschema.Table;
import io.frictionlessdata.tableschema.datasourceformats.DataSourceFormat;
import org.apache.commons.csv.CSVFormat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FilebasedResource<C> extends AbstractReferencebasedResource<File,C> {
    private File basePath;
    private boolean isInArchive;

    public FilebasedResource(String name, Collection<File> paths, File basePath) {
        super(name, paths);
        if (null == paths) {
            throw new DataPackageException("Invalid Resource. " +
                    "The path property cannot be null for file-based Resources.");
        }
        this.basePath = basePath;
        for (File path : paths) {
            /* from the spec: "SECURITY: / (absolute path) and ../ (relative parent path)
               are forbidden to avoid security vulnerabilities when implementing data
               package software."

               https://frictionlessdata.io/specs/data-resource/index.html#url-or-path
             */
            if (path.isAbsolute()) {
                throw new DataPackageException("Path entries for file-based Resources cannot be absolute");
            }
        }
    }

    public File getBasePath() {
        return basePath;
    }

    @Override
    Table createTable(File reference) throws Exception {
        return Table.fromSource(reference, basePath, schema, getCsvFormat());
    }

    @Override
    String getStringRepresentation(File reference) {
        if (File.separator.equals("\\"))
            return reference.getPath().replaceAll("\\\\", "/");
        return reference.getPath();
    }

    @Override
    List<Table> readData () throws Exception{
        List<Table> tables = new ArrayList<>();
        if (this.isInArchive) {
            tables = readfromZipFile();
        } else {
            tables = readfromOrdinaryFile();
        }
        return tables;
    }

    private List<Table> readfromZipFile() throws Exception {
        List<Table> tables = new ArrayList<>();
        for (File file : paths) {
            String fileName = file.getPath().replaceAll("\\\\", "/");
            String content = getZipFileContentAsString (basePath.toPath(), fileName);
            Table table = Table.fromSource(content, schema, getCsvFormat());
            tables.add(table);
        }
        return tables;
    }
    private List<Table> readfromOrdinaryFile() throws Exception {
        List<Table> tables = new ArrayList<>();
        for (File file : paths) {
                /* from the spec: "SECURITY: / (absolute path) and ../ (relative parent path)
                   are forbidden to avoid security vulnerabilities when implementing data
                   package software."

                   https://frictionlessdata.io/specs/data-resource/index.html#url-or-path
                 */
            Path securePath = Resource.toSecure(file.toPath(), basePath.toPath());
            Path relativePath = basePath.toPath().relativize(securePath);
            Table table = createTable(relativePath.toFile());
            tables.add(table);
        }
        return tables;
    }

    @Override
    public void writeDataAsCsv(Path outputDir, Dialect dialect) throws Exception {
        Dialect lDialect = (null != dialect) ? dialect : Dialect.DEFAULT;
        List<String> paths = new ArrayList<>(getReferencesAsStrings());
        int cnt = 0;
        for (String fileName : paths) {
            List<Table> tables = getTables();
            Table t  = tables.get(cnt++);
            Path p;
            if (outputDir.toString().isEmpty()) {
                p = outputDir.getFileSystem().getPath(fileName);
                if (!Files.exists(p)) {
                    Files.createDirectories(p);
                }
            } else {
                if (!Files.exists(outputDir)) {
                    Files.createDirectories(outputDir);
                }
                p = outputDir.resolve(fileName);
            }

            Files.deleteIfExists(p);
            writeTableAsCsv(t, lDialect, p);
        }
    }
    
    public void setIsInArchive(boolean isInArchive) {
        this.isInArchive = isInArchive;
    }
}
