package io.frictionlessdata.datapackage;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class SpecificationValidityTest {

    private String testVal2 = "[" +
            "{\"schema\":\"https://raw.githubusercontent.com/frictionlessdata/datapackage-java/master/src/test/resources/fixtures/schema/population_schema.json\"," +
            "\"path\":[\"https://raw.githubusercontent.com/frictionlessdata/datapackage-java/master/src/test/resources/fixtures/data/cities.csv\"," +
            "          \"https://raw.githubusercontent.com/frictionlessdata/datapackage-java/master/src/test/resources/fixtures/data/cities2.csv\"," +
            "          \"https://raw.githubusercontent.com/frictionlessdata/datapackage-java/master/src/test/resources/fixtures/data/cities3.csv\"]," +
            "\"name\":\"third-resource\"}," +
            "]";
/*
    @Test
    @DisplayName("Test that a schema can be defined via a URL")
    // Test for https://github.com/frictionlessdata/specs/issues/645
    void testValidationURLAsSchemaReference() throws Exception{
        JSONArray jsonObjectToValidate = new JSONArray(testVal2);
        InputStream inputStream = Validator.class.getResourceAsStream("/schemas/data-package.json");
        if(inputStream != null) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema objSchema = SchemaLoader.load(rawSchema);
            Map<String, Schema> schemas = ((ObjectSchema)objSchema).getPropertySchemas();
            ArraySchema schema = (ArraySchema)schemas.get("resources");
            schema.validate(jsonObjectToValidate); // throws a ValidationException if this object is invalid
        } else {
            throw new FileNotFoundException();
        }
    }


    @Test
    void testLoadFromFileWhenPathExists() throws Exception {
        String fName = "/testsuite-data/basic-csv/datapackage.json";
        // Get string content version of source file.
        String jsonString = TestHelpers.getFileContents(fName);

        // Build DataPackage instance based on source file path.
        new Package(jsonString, true);

    }

    @Test
    void testCreateNews() throws Exception {
        new Package( );
    }*/
}
