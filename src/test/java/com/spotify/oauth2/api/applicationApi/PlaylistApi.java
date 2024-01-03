package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.Route.PLAYLISTS;
import static com.spotify.oauth2.api.Route.USERS;
import static com.spotify.oauth2.api.TokenManager.getToken;


public class PlaylistApi {
    static String access_token = getToken();

    public static Response post(Playlist requestPlayList){
        return RestResource.post(USERS + "/"+ ConfigLoader.getInstance().getUserId() + PLAYLISTS,access_token,requestPlayList);
    }

    public static Response post(String token, Playlist requestPlayList){
        return RestResource.post(USERS + "/"+ ConfigLoader.getInstance().getUserId() + PLAYLISTS, token, requestPlayList);
    }

    public static Response get(String playlistId){
        return RestResource.get(PLAYLISTS + "/"+ playlistId,access_token);
    }

    public static Response update(String playlistId, Playlist requestPlayList){
        return RestResource.update(PLAYLISTS + "/" + playlistId,access_token,requestPlayList);
    }
}
