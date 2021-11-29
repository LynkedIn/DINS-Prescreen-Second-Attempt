import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.IReporter;
import org.testng.annotations.Test;
import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class TestCases implements IReporter{
    JSONParser parser = new JSONParser();

    @Test
    void addUser() throws Exception {
        int randomNumber = (int) (Math.random() * 10000);
        int lastID;
        String firstName = "Truman" + String.valueOf(randomNumber);
        try {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://localhost:8080/users/")
                    .then()
                    .statusCode(200)
                    .extract().response();
            JSONArray responseJSON = (JSONArray) parser.parse(response.body().asString());
            lastID = responseJSON.size();

        } catch (AssertionError e) {
            throw new Exception("Can't find users\n" + e);
        } catch (ParseException e) {
            throw new Exception("Can't parse users\n" + e);
        }

        HashMap<String, String> newUser = new HashMap<>();
        newUser.put("id", String.valueOf(lastID + 1));
        newUser.put("firstName", firstName);
        newUser.put("lastName", "Burbank");
        JSONObject newUserJSON = new JSONObject(newUser);
        try {
            given()
                    .contentType(ContentType.JSON)
                    .body(newUserJSON.toJSONString())
                    .when()
                    .post("http://localhost:8080/users/")
                    .then()
                    .statusCode(201);
        } catch (AssertionError e) {
            throw new Exception("User creating failure\n" + e);
        }

        try {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://localhost:8080/users/search?name=" + firstName)
                    .then()
                    .statusCode(200);
        } catch (AssertionError e) {
            throw new Exception("Created user not found\n" + e);
        }
    }

    @Test
    void deleteNotexistingContact() throws Exception {
        int randomNumber = (int) (Math.random() * 10000);
        String firstName = "Truman" + String.valueOf(randomNumber);
        int lastID;
        try {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://localhost:8080/users/")
                    .then()
                    .statusCode(200)
                    .extract().response();
            JSONArray responseJSON = (JSONArray) parser.parse(response.body().asString());
            lastID = responseJSON.size();
        } catch (AssertionError e) {
            throw new Exception("Can't find users\n" + e);
        } catch (ParseException e) {
            throw new Exception("Can't parse users\n" + e);
        }
        HashMap<String, String> newUser = new HashMap<>();
        newUser.put("id", String.valueOf(lastID + 1));
        newUser.put("firstName", firstName);
        newUser.put("lastName", "Burbank");
        JSONObject newUserJSON = new JSONObject(newUser);
        try {
            given()
                    .contentType(ContentType.JSON)
                    .body(newUserJSON.toJSONString())
                    .when()
                    .post("http://localhost:8080/users/")
                    .then()
                    .statusCode(201);
        } catch (AssertionError e) {
            throw new Exception("User creating failure\n" + e);
        }

        try {
            given()
                    .when()
                    .delete("http://localhost:8080/users/" + lastID + "/contacts/1")
                    .then()
                    .statusCode(404);
        } catch (AssertionError e) {
            throw new Exception("Not existing contact was deleted\n" + e);
        }
    }

    @Test
    void invalidEmailInContact() throws Exception {
        int randomNumber = (int) (Math.random() * 10000);
        String firstName = "Truman" + String.valueOf(randomNumber);
        int lastID;
        try {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://localhost:8080/users/")
                    .then()
                    .statusCode(200)
                    .extract().response();
            JSONArray responseJSON = (JSONArray) parser.parse(response.body().asString());
            lastID = responseJSON.size();
        } catch (AssertionError e) {
            throw new Exception("Can't find users\n" + e);
        } catch (ParseException e) {
            throw new Exception("Can't parse users\n" + e);
        }
        HashMap<String, String> newUser = new HashMap<>();
        newUser.put("id", String.valueOf(lastID + 1));
        newUser.put("firstName", firstName);
        newUser.put("lastName", "Burbank");
        JSONObject newUserJSON = new JSONObject(newUser);
        try {
            given()
                    .contentType(ContentType.JSON)
                    .body(newUserJSON.toJSONString())
                    .when()
                    .post("http://localhost:8080/users/")
                    .then()
                    .statusCode(201);
        } catch (AssertionError e) {
            throw new Exception("User creating failure\n" + e);
        }

        HashMap<String, String> newContact = new HashMap<>();
        newContact.put("id", "1");
        newContact.put("firstName", "Ada");
        newContact.put("lastName", "Lovelace");
        newContact.put("phone", "1234567890");
        newContact.put("email", "definitely_not_email");
        JSONObject newContactJSON = new JSONObject(newContact);

        try {
            given()
                    .contentType(ContentType.JSON)
                    .body(newContactJSON.toJSONString())
                    .when()
                    .post("http://localhost:8080/users/" + String.valueOf(lastID + 1) + "/contacts")
                    .then()
                    .statusCode(400);
        } catch (AssertionError e) {
            throw new Exception("Contact with invalid email was added\n" + e);
        }
    }

    @Test
    void deleteUserWithContact() throws Exception {
        int randomNumber = (int) (Math.random() * 10000);
        String firstName = "Truman" + String.valueOf(randomNumber);
        int lastID;
        try {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://localhost:8080/users/")
                    .then()
                    .statusCode(200)
                    .extract().response();
            JSONArray responseJSON = (JSONArray) parser.parse(response.body().asString());
            lastID = responseJSON.size();
        } catch (AssertionError e) {
            throw new Exception("Can't find users\n" + e);
        } catch (ParseException e) {
            throw new Exception("Can't parse users\n" + e);
        }
        HashMap<String, String> newUser = new HashMap<>();
        newUser.put("id", String.valueOf(lastID + 1));
        newUser.put("firstName", firstName);
        newUser.put("lastName", "Burbank");
        JSONObject newUserJSON = new JSONObject(newUser);
        try {
            given()
                    .contentType(ContentType.JSON)
                    .body(newUserJSON.toJSONString())
                    .when()
                    .post("http://localhost:8080/users/")
                    .then()
                    .statusCode(201);
        } catch (AssertionError e) {
            throw new Exception("User creating failure\n" + e);
        }

        HashMap<String, String> newContact = new HashMap<>();
        newContact.put("id", "1");
        newContact.put("firstName", "Ada");
        newContact.put("lastName", "Lovelace");
        newContact.put("phone", "1234567890");
        newContact.put("email", "definitely_email@email.com");

        try {
            JSONObject newContactJSON = new JSONObject(newContact);
            given()
                    .contentType(ContentType.JSON)
                    .body(newContactJSON.toJSONString())
                    .when()
                    .post("http://localhost:8080/users/" + String.valueOf(lastID + 1) + "/contacts")
                    .then()
                    .statusCode(201);
        } catch (AssertionError e) {
            throw new Exception("Contact wasn't added\n" + e);
        }

        try {
            given()
                    .when()
                    .delete("http://localhost:8080/users/" + String.valueOf(lastID + 1))
                    .then()
                    .statusCode(202); // or 4xx status code confirming that this action is not possible
        } catch (AssertionError e) {
            throw new Exception("User wasn't deleted\n" + e);
        }
    }
}
