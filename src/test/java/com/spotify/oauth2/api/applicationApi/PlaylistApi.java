package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static com.spotify.oauth2.api.TokenManager.renewToken;
import static io.restassured.RestAssured.given;

public class PlaylistApi {
    static String access_token = renewToken();

    public static Response post(Playlist requestPlayList){
        return RestResource.post("users/31ft3tx5mdjq3x5xn3soozmlsw6y/playlists",access_token,requestPlayList);
    }

    public static Response post(String token, Playlist requestPlayList){
        return RestResource.post("users/31ft3tx5mdjq3x5xn3soozmlsw6y/playlists", token, requestPlayList);
    }

    public static Response get(String playlistId){
        return RestResource.get("/playlists/"+ playlistId,access_token);
    }

    public static Response update(String playlistId, Playlist requestPlayList){
        return RestResource.update("/playlists/"+ playlistId,access_token,requestPlayList);
    }
}
