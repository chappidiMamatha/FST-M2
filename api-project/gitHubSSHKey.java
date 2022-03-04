package RestAssuredProject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class gitHubSSHKey {

    // Declare request specification
    RequestSpecification requestSpec;
    // Declare SSH Key
    String sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDQ/wwn8eYafPFI1VIy8vd6CCE+YfStJMMxTUN87F8iCmOtQGuLB9TOriuFJkk0JiXUrLBjH56AReooECEOUYJhAHk7aLgWvaIYVwARBw35miEhNW1GB5Mu4iqFl4h7oHuoD6H6FHhrPs77idWr8RApHUXTW3ZVZ1nxmFAvVxyAuvg8czSjXLAHJgm8B5OIo02XqOZhe22YdxdLy7nX1U3KIbSVrtfJKEjV1b9JKz0VtFtjI4Mx4SK+8VLa1UNY/P3iPVdgojDwRSMqhJD9I41pYNKA8bPQEjJ7SGBu/Kd+hpP9lBEk4WhZ4SEuumxyN7mbrHLN9zBL3VWd3S7LAh3J";
    int sshKeyID;

    @BeforeClass
    public void setUp()
    {   // Create request specification
        requestSpec = new RequestSpecBuilder()
                // Set content type
                .setContentType(ContentType.JSON)
                // Set Authorization Token
                .addHeader("Authorization", "token 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")
                // Set base URL
                .setBaseUri("https://api.github.com")
                // Build request specification
                .build();

    }
    @Test(priority = 1)
    // Test case using a DataProvider
    public void postSSHKey()
    {
        String reqBody = "{\"title\": \"TestAPIKey\",\"key\": \""+sshKey+"\"}";
        Response response = given().spec(requestSpec) // Use requestSpec
                .body(reqBody) // Send request body
                .when().post("/user/keys"); // Send POST request
        String resBody = response.getBody().asPrettyString();

        // Print response
        System.out.println(resBody);
        System.out.println(response.getStatusCode());

        // Extract status from response
        sshKeyID = response.then().extract().path("id");

        // Assertions
        response.then().statusCode(201);
    }

    @Test(priority=2)
    //Test case using a DataProvider
    public void getSSHKey()
    {
        Response response = given().spec(requestSpec) // Use requestSpec
                .when().get("/user/keys");  //Send Get request
        String resBody = response.getBody().asPrettyString();
        // Print response
        System.out.println(resBody);
        System.out.println(response.getStatusCode());
        // Assertions
        response.then().statusCode(200);
    }
    @Test(priority=3)

    public void deleteKey()
    {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("keyId", sshKeyID).when().delete("/user/keys/{keyId}"); // Add path parameter
        String resBody = response.getBody().asPrettyString();
        System.out.println(resBody);
        System.out.println(response.getStatusCode());
        // Assertions
        response.then().statusCode(204);
    }
}