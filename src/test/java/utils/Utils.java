package utils;

import base.BaseTest;

import static io.restassured.RestAssured.given;

public class Utils extends BaseTest {

    public void deleteElement(String element, String id){
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + element + "/" + id)
                .then()
                .statusCode(200);
    }
}
