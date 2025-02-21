package com.test;
import com.api.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Synchronized;
import org.example.ReceipeResponse;
import org.example.SimplePojo;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static utils.FakerUtil.*;

public class RestAssuredMethod {

    HashMap<String, String> headerobj = new HashMap<>();

    @org.testng.annotations.Test
    public void test1ValidateBody() {
        headerobj.put("x-api-test", "mock");
        headerobj.put("content-type", "json");
        given().log().all().
                baseUri("https://dummyjson.com")
                .header("x-api-test", "mock1")
                .headers(headerobj)
                .when()
                .get("/products")
                .then()
                .log().all().
                assertThat().
                statusCode(StatusCode.CODE_200.getCode())
                .header("X-Ratelimit-Limit", String.valueOf(100))
                .body(matchesJsonSchemaInClasspath("schema.json"))
                .body("products.id", hasItem(Integer.valueOf("1")),
                        "products.size()", equalTo(30),
                        "products[0].tags", contains("beauty", "mascara"),
                        "products[0].tags", containsInAnyOrder("mascara", "beauty"),
                        "products", hasSize(30),
                        "products[0]", hasKey("id"),
                        "products[0]", hasValue(1),
                        "products[0]", hasEntry("id", 1)

                );

    }

    @org.testng.annotations.Test
    public void test2ExtractResponse() {
        Response res = given().baseUri("https://dummyjson.com")
                .when()
                .get("/products")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();
        System.out.println("response: " + res.asString());
        System.out.println("product 1 title: " + res.path("products[0].title"));

        JsonPath jsonpath = new JsonPath(res.asString());
        System.out.println("product 1 description: " + jsonpath.getString("products[0].description"));

        assertThat(res.path("products[0].title"), equalTo("Essence Mascara Lash Princess"));
        Assert.assertEquals(res.path("products[0].title"), "Essence Mascara Lash Princess");

    }


    @Test
    public void test3RequestResponseBuilder() {
        RequestSpecBuilder requestspecbuilder = new RequestSpecBuilder()
                .setBaseUri("https://videogamedb.uk:443/api/v2/videogame")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        requestSpecification = requestspecbuilder.build();

        ResponseSpecBuilder responsespecbuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.ALL);
        responseSpecification = responsespecbuilder.build();
    }

    @Test
    public void test3validatePostRequest() {
        String payload =
                "{\n  \"category\": \"Platform2\",\n  \"name\": \"Mario\",\n  \"rating\": \"Mature1\",\n  \"releaseDate\": \"2012-05-04\",\n  \"reviewScore\": 85\n}";
        given()
                .body(payload)
                .when()
                .post()
                .then()
                .assertThat()
                .body("id", equalTo(0));
    }

    @Test
    public void test4validateDeleteRequest() {
        String id = "1";
        given()
                .pathParam("id", id)
                .when()
                .delete("/{id}")
                .then()
                .assertThat()
                .contentType(ContentType.TEXT);
    }

    @Test
    public void test5validatePostRequest() {
        File file = new File("src/main/resources/videogame.json");
        given()
                .body(file)
                .when()
                .post()
                .then()
                .assertThat()
                .body("id", equalTo(0));
    }

    @Test
    public  void test6validatePostRequest() {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("category", "Platform2");
        payload.put("name", "Mario");
        payload.put("rating", "Mature1");
        payload.put("releaseDate", "2012-05-04");
        payload.put("reviewScore", 85);

        given()
                .body(payload)
                .when()
                .post()
                .then()
                .assertThat()
                .body("id", equalTo(0));
    }

    @Test
    public void test7passcomplexjson() {
        ArrayList<String> developedCountries = new ArrayList<>();
        developedCountries.add("South Africa");
        developedCountries.add("Egypt");
        developedCountries.add("Nigeria");
        ArrayList<String> oceans = new ArrayList<>();
        oceans.add("Atlantic Ocean");
        oceans.add("Indian Ocean");
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Equator");
        lines.add("Prime Meridian");
        HashMap<String, Object> rootHashmap = new HashMap<>();
        rootHashmap.put("code", "AF");
        rootHashmap.put("name", "Africa");
        rootHashmap.put("areaSqKm", 30370000);
        rootHashmap.put("population", 1340598000);
        rootHashmap.put("countries", 54);
        rootHashmap.put("lines", lines);
        rootHashmap.put("oceans", oceans);
        rootHashmap.put("developedCountries", developedCountries);
        ArrayList<HashMap<String, Object>> rootArraylist = new ArrayList<>();
        rootArraylist.add(rootHashmap);
        given().log().all()
                .body(rootArraylist);

        System.out.println(rootArraylist);

    }

    @Test
    public void test8validatePostRequestObjectMapper() throws JsonProcessingException {
        HashMap<String, Object> payload1 = new HashMap<>();
        payload1.put("category", "Platform2");
        payload1.put("name", "Mario");
        payload1.put("rating", "Mature1");
        payload1.put("releaseDate", "2012-05-04");
        payload1.put("reviewScore", 85);

        com.fasterxml.jackson.databind.ObjectMapper objectmapper = new ObjectMapper();
        String payloadobj = objectmapper.writeValueAsString(payload1);

        given()
                .body(payloadobj)
                .when()
                .post()
                .then()
                .assertThat()
                .body("id", equalTo(0));
    }

    @Test
    public void test9SerializewithPOJO() {
        SimplePojo simplepojo = new SimplePojo("Sony", "2019-10-09", 70, "Platform3", "Mature3");
        given()
                .body(simplepojo)
                .when()
                .post()
                .then()
                .assertThat()
                .body("id", equalTo(0));
    }

    @Test(dependsOnMethods = { "test9SerializewithPOJO" })
    public void test9bDeSerialize() {
        {
            com.fasterxml.jackson.databind.ObjectMapper objectmapper = new ObjectMapper();
            //   objectmapper.
            ReceipeResponse receipe = given().log().all().
                    baseUri("https://dummyjson.com")
                    .header("x-api-test", "mock1")
                    .headers(headerobj)
                    .when()
                    .get("/recipes")
                    .then()
                    .log().all().
                    assertThat().
                    statusCode(200)
                    .extract()
                    .response()
                    .as(ReceipeResponse.class);
       //     Assert.assertEquals("Vegetarian Stir-Fry", receipe.getRecipes().get(1).name);

        }
    }

    @Test(dataProvider = "classdetails",dependsOnMethods = { "test9SerializewithPOJO" })
    public void test9cSerializewithPOJOwithDataProvider(String name, String releaseDate, int reviewScore, String category, String rating) {
        SimplePojo simplepojo = new SimplePojo(name, releaseDate, reviewScore, category, rating);
        given()
                .body(simplepojo)
                .when()
                .post()
                .then()
                .assertThat()
                .body("id", equalTo(0));
    }

    @DataProvider(name = "classdetails")
    public Object[][] getClassDetails() {
        return new Object[][]
                {
                        {"Sony", "2019-05-05", 70, "Platform3", "Mature3"},
                        {"Ser", "2019-05-06", 71, "Platform1", "Mature2"}
                };
    }

    @Test(dependsOnMethods = { "test9SerializewithPOJO" })
    public void test9duseBasicAuthentication() {

        String usernamepassword = "myusr:mypwd";
        String base64Encoded = Base64.getEncoder().encodeToString(usernamepassword.getBytes());
        System.out.println(base64Encoded);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Encoded);
        System.out.println(new String(decodedBytes));

    }
// change for new commit
    @Test(dependsOnMethods = { "test9SerializewithPOJO" })
    public void test9eUseFakerApi() {
        SimplePojo simplepojo = new SimplePojo(generateClassName(), "2019-09-09", 10,generateClassName(),generateClassName());
        given()
                .body(simplepojo)
                .when()
                .post()
                .then()
                .assertThat()
                .body("id", equalTo(0));
    }
}