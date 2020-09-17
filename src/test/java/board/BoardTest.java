package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class BoardTest extends BaseTest {


    @Test
    public void createNewBoard(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Test board")
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Test board");

        String boardId = json.get("id");

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(200);

    }

    @Test
    public void createBoardWithEmptyBoardName(){
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "")
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(400)
                .extract()
                .response();

    }

    @Test
    public void createBoardWithoutDefaultLists(){
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board with empty default lists")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Board with empty default lists");

        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + BOARDS + "/" + boardId + "/" + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonGet = responseGet.jsonPath();
        List<String> idList = jsonGet.getList("id");

        assertThat(idList.size()).isEqualTo(0);
        assertThat(idList).hasSize(0);

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(200);

    }

    @Test
    public void createBoardWithDefaultLists(){
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board with default lists")
                .queryParam("defaultLists", true)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Board with default lists");

        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + BOARDS + "/" + boardId + "/" + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonGet = responseGet.jsonPath();
        List<String> nameList = jsonGet.getList("name");

        assertThat(nameList).hasSize(3).contains("To Do", "Doing", "Done");

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(200);
    }
}
