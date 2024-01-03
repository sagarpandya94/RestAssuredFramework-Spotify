package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Spotify OAuth 2.0")
@Feature("Playlist")
public class PlaylistTests extends BaseTest{

    @Story("Create playlist")
    @Description("This test case creates a playlist")
    @Test(description = "Create a playlist")
    public void ShouldBeAbleToCreatePlaylist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName(generateName());
        requestPlaylist.setDescription(generateDescription());
        requestPlaylist.set_public(false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.statusCode(), equalTo(StatusCode.CODE_201.getCode()));

        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
    }

    @Test
    public void ShouldBeAbleToGetPlaylist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("New Afternoon kicks");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.set_public(true);

        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertThat(response.getStatusCode(),equalTo(StatusCode.CODE_200.getCode()));

        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
    }

    @Test
    public void ShouldBeAbleToUpdatePlaylist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("New Afternoon kicks");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.set_public(true);

        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(StatusCode.CODE_200.getCode()));
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithoutName(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.set_public(false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(StatusCode.CODE_400.getCode()));

        ErrorRoot errorRoot = response.as(ErrorRoot.class);

        assertThat(errorRoot.getError().getStatus(),equalTo(StatusCode.CODE_400.getCode()));
        assertThat(errorRoot.getError().getMessage(),equalTo(StatusCode.CODE_400.getMsg()));
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Hello");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.set_public(false);

        Response response = PlaylistApi.post("duumy", requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(StatusCode.CODE_401.getCode()));

        ErrorRoot errorRoot = response.as(ErrorRoot.class);

        assertThat(errorRoot.getError().getStatus(),equalTo(StatusCode.CODE_401.getCode()));
        assertThat(errorRoot.getError().getMessage(),equalTo(StatusCode.CODE_401.getMsg()));
    }
}
