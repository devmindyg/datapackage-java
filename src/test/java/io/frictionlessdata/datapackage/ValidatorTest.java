package io.frictionlessdata.datapackage;

import io.frictionlessdata.datapackage.exceptions.DataPackageException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.frictionlessdata.datapackage.TestUtil.getBasePath;

/**
 * Test calls for JSON Validator class.
 * 
 */
public class ValidatorTest {
    private static URL url;

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    private Validator validator = null;
    
    @Before
    public void setup() throws MalformedURLException {
        validator = new Validator();
        url = new URL("https://raw.githubusercontent.com/frictionlessdata/datapackage-java" +
                "/master/src/test/resources/fixtures/datapackages/multi-data/datapackage.json");
    }
    
    @Test
    public void testValidatingInvalidJsonObject() throws IOException, DataPackageException {
        JSONObject datapackageJsonObject = new JSONObject("{\"invalid\" : \"json\"}");
        
        exception.expect(ValidationException.class);
        validator.validate(datapackageJsonObject);  
    }
    
    @Test
    public void testValidatingInvalidJsonString() throws IOException, DataPackageException{
        String datapackageJsonString = "{\"invalid\" : \"json\"}";
        
        exception.expect(ValidationException.class);
        validator.validate(datapackageJsonString);   
    }
    
    @Test
    public void testValidationWithInvalidProfileId() throws Exception {
        Package dp = new Package(url, true);
        
        String invalidProfileId = "INVALID_PROFILE_ID";
        dp.addProperty("profile", invalidProfileId);
        
        exception.expectMessage("Invalid profile id: " + invalidProfileId);
        dp.validate();
    }
    
    @Test
    public void testValidationWithValidProfileUrl() throws Exception {
        Package dp = new Package(url,  true);
        dp.setProfile( "https://raw.githubusercontent.com/frictionlessdata/datapackage-java" +
                "/master/src/main/resources/schemas/data-package.json");
        
        dp.validate();
        
        // No exception thrown, test passes.
        Assert.assertEquals("https://raw.githubusercontent.com/frictionlessdata/datapackage-java/" +
                "master/src/main/resources/schemas/data-package.json", dp.getProfile());
    }
    
    @Test
    public void testValidationWithInvalidProfileUrl() throws Exception {
        Package dp = new Package(url, true);
        
        String invalidProfileUrl = "https://raw.githubusercontent.com/frictionlessdata/datapackage-java" +
                "/master/src/main/resources/schemas/INVALID.json";
        dp.addProperty("profile", invalidProfileUrl);
        
        exception.expectMessage("Invalid profile schema URL: " + invalidProfileUrl);
        dp.validate();
    }
}
