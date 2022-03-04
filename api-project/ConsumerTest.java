package pactLiveProject;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    Map<String, String> headers = new HashMap<>();

    String createUser = "/api/users";

    @Pact(consumer="UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        headers.put("content-Type", "application/json");
        headers.put("Accept","application/json");
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        return builder.given("A request to create a user")
                .uponReceiving(" A request to create a user")
                .path(createUser)
                .headers(headers)
                .method("POST")
                .body(requestResponseBody).willRespondWith()
                .status(201)
                .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port="8282")
    public void consumerTest() {
        RestAssured.baseURI = "http://localhost:8282";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1);
        requestBody.put("firstName", "Mamatha");
        requestBody.put("lastName", "chappidi");
        requestBody.put("email", "chappidimamatha@gmail.com");

        Response response = RestAssured.given().headers(headers).when().body(requestBody).post(createUser);

        System.out.println(response.getBody().asString());
        response.then().statusCode(201);

    }
}
