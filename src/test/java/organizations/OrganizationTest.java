package organizations;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import utils.Utils;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationTest extends BaseTest {
    private static String teamId;

    @Test
    /// Create new organization with all valid fields
    //there are digits added to 'name' - test to check if 'name' starts with entered text
    public void createNewOrganization(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Test1 displayName")
                .queryParam("desc", "Test1 organization description")
                .queryParam("name", "test_name1")
                .queryParam("website", "https://abc.test.abc")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
       assertThat(json.getString("displayName")).isEqualTo("Test1 displayName");
       assertThat(json.getString("desc")).isEqualTo("Test1 organization description");
       assertThat(json.getString("name")).startsWith("test_name1");
       assertThat(json.getString("website")).isEqualTo("https://abc.test.abc");

        teamId = json.get("id");
        Utils.deleteElement(ORGANIZATIONS,teamId);

    }

    @Test
    /// Create new organization only with mandatory fields
    public void createNewOrganizationOnlyMandatoryFields(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Test2 displayName")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Test2 displayName");
        assertThat(json.getString("desc")).isNullOrEmpty();
        assertThat(json.getString("website")).isNullOrEmpty();

        teamId = json.get("id");
        Utils.deleteElement(ORGANIZATIONS,teamId);
    }

    @Test
    /// Create new organization without mandatory fields - displayName
    public void createNewOrganizationWithoutMandatoryFields(){

        Response response = given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    /// Create new organization with email that does not start with http://
    //there are http:// added to 'email' - test to check if 'email' is corrected
    public void createNewOrganizationWithInvalidEmail(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Test3 displayName")
                .queryParam("desc", "Test3 organization description")
                .queryParam("name", "test_name3")
                .queryParam("website", "not.started.with.http")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Test3 displayName");
        assertThat(json.getString("desc")).isEqualTo("Test3 organization description");
        assertThat(json.getString("name")).startsWith("test_name3");
        assertThat(json.getString("website")).startsWith("http://");
        assertThat(json.getString("website")).isEqualTo("http://not.started.with.http");

        teamId = json.get("id");
        Utils.deleteElement(ORGANIZATIONS,teamId);
    }
}
