package spotify.oauth2.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String access_token = "BQBzlHbRI3bEb405ucE1bG4Bk7KDoALGKACbtRNWLHu9Q7GlTjQGdz7b4ruyoDFyX98yBwQzLKWJt_aCHLfqbsmNYvO5DA8bnUqA9W1QW95L1q0R7LPZsQ6fdFecJhY4wGPvTszIzwFAOLHm21KZ0e4ozQ1zSq3Yvb_s1DbtecOEubwDGXVLDnb_BbJ_v-0ToPffEkACsDWoekZKXBX5X644FYeNawJl7TbL_nQlgua9g5rYI2YH737AbJBxiUrCpk6nC537lWcp62Ok";

    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.spotify.com").
                setBasePath("/v1").
                addHeader("Authorization","Bearer " + access_token).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void ShouldBeAbleToCreatePlaylist(){
        String payload = "{\n" +
                "    \"name\":\"New Afternoon kicks\",\n" +
                "    \"description\":\"Lazy songs\",\n" +
                "    \"public\":false\n" +
                "}";

        given(requestSpecification).
                body(payload).
        when().
                post("users/31ft3tx5mdjq3x5xn3soozmlsw6y/playlists").
        then().spec(responseSpecification).
                assertThat().statusCode(201).
                body("name",equalTo("New Afternoon kicks"),
                        "description",equalTo("Lazy songs"),
                        "public",equalTo(false));
    }

    @Test
    public void ShouldBeAbleToGetPlaylist(){
        given(requestSpecification).
        when().
                get("/playlists/2GDsCyVMFFr9P9Nsl347xW").
        then().spec(responseSpecification).
                assertThat().statusCode(200).
                body("name",equalTo("New Afternoon kicks"),
                        "description",equalTo("Lazy songs"),
                        "public",equalTo(true));
    }

    @Test
    public void ShouldBeAbleToUpdatePlaylist(){
        String payload = "{\n" +
                "    \"name\":\"New Afternoon kicks\",\n" +
                "    \"description\":\"New Lazy songs\",\n" +
                "    \"public\":false\n" +
                "}";

        given(requestSpecification).
                body(payload).
        when().
                put("/playlists/2GDsCyVMFFr9P9Nsl347xW").
        then().spec(responseSpecification).
                assertThat().statusCode(200);
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithoutName(){
        String payload = "{\n" +
                "    \"name\":\"\",\n" +
                "    \"description\":\"Lazy songs\",\n" +
                "    \"public\":false\n" +
                "}";

        given(requestSpecification).
                body(payload).
        when().
                post("users/31ft3tx5mdjq3x5xn3soozmlsw6y/playlists").
        then().spec(responseSpecification).
                assertThat().statusCode(400).
                body("error.status",equalTo(400),
                        "error.message",equalTo("Missing required field: name"));
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken(){
        String payload = "{\n" +
                "    \"name\":\"Hello\",\n" +
                "    \"description\":\"Lazy songs\",\n" +
                "    \"public\":false\n" +
                "}";

        given().
                baseUri("https://api.spotify.com").
                basePath("/v1").
                header("Authorization","Bearer " + "dummy").
                contentType(ContentType.JSON).
                log().all().
                body(payload).
        when().
                post("users/31ft3tx5mdjq3x5xn3soozmlsw6y/playlists").
        then().spec(responseSpecification).
                assertThat().statusCode(401).
                body("error.status",equalTo(401),
                        "error.message",equalTo("Invalid access token"));
    }
}
