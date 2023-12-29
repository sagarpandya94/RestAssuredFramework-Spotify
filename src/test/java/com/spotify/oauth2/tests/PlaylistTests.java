package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
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
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {
    @Test
    public void ShouldBeAbleToCreatePlaylist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("New Afternoon kicks");
        requestPlaylist.setDescription("Lazy songs");
        requestPlaylist.setPublic(false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.statusCode(),equalTo(201));

        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(),equalTo(requestPlaylist.getPublic()));
    }

    @Test
    public void ShouldBeAbleToGetPlaylist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("New Afternoon kicks");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.setPublic(true);

        Response response = PlaylistApi.get("2GDsCyVMFFr9P9Nsl347xW");
        assertThat(response.getStatusCode(),equalTo(200));

        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(),equalTo(requestPlaylist.getPublic()));
    }

    @Test
    public void ShouldBeAbleToUpdatePlaylist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("New Afternoon kicks");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.setPublic(true);

        Response response = PlaylistApi.update("2GDsCyVMFFr9P9Nsl347xW",requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(200));
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithoutName(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.setPublic(false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(400));

        ErrorRoot errorRoot = response.as(ErrorRoot.class);

        assertThat(errorRoot.getError().getStatus(),equalTo(400));
        assertThat(errorRoot.getError().getMessage(),equalTo("Missing required field: name"));
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Hello");
        requestPlaylist.setDescription("New Lazy songs");
        requestPlaylist.setPublic(false);

        Response response = PlaylistApi.post("duumy", requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(401));

        ErrorRoot errorRoot = response.as(ErrorRoot.class);

        assertThat(errorRoot.getError().getStatus(),equalTo(401));
        assertThat(errorRoot.getError().getMessage(),equalTo("Invalid access token"));
    }
}
