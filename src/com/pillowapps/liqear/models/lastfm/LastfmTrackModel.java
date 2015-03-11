package com.pillowapps.liqear.models.lastfm;

import com.pillowapps.liqear.entities.Track;
import com.pillowapps.liqear.entities.lastfm.LastfmTrack;
import com.pillowapps.liqear.entities.lastfm.roots.LastfmTrackRoot;
import com.pillowapps.liqear.helpers.AuthorizationInfoManager;
import com.pillowapps.liqear.helpers.LastfmApiHelper;
import com.pillowapps.liqear.helpers.LastfmCallbackUtils;
import com.pillowapps.liqear.network.ServiceHelper;
import com.pillowapps.liqear.network.callbacks.LastfmCallback;
import com.pillowapps.liqear.network.callbacks.SimpleCallback;
import com.pillowapps.liqear.network.service.LastfmApiService;

import java.util.Map;
import java.util.TreeMap;

public class LastfmTrackModel {
    private LastfmApiService lastfmService = ServiceHelper.getLastfmService();
    private LastfmApiHelper apiHelper = new LastfmApiHelper();

    public void love(Track track, final SimpleCallback<Object> callback) {
        String sessionKey = AuthorizationInfoManager.getLastfmKey();
        String artist = track.getArtist();
        String title = track.getTitle();

        Map<String, String> params = new TreeMap<>();
        params.put("artist", artist);
        params.put("track", title);
        params.put("sk", sessionKey);
        params.put("method", "track.love");

        lastfmService.love(artist,
                title,
                apiHelper.generateApiSig(params),
                sessionKey,
                LastfmCallbackUtils.createTransitiveCallback(callback));
    }

    public void unlove(Track track, final SimpleCallback<Object> callback) {
        String sessionKey = AuthorizationInfoManager.getLastfmKey();
        String artist = track.getArtist();
        String title = track.getTitle();

        Map<String, String> params = new TreeMap<>();
        params.put("artist", artist);
        params.put("track", title);
        params.put("sk", sessionKey);
        params.put("method", "track.unlove");

        lastfmService.unlove(artist,
                title,
                apiHelper.generateApiSig(params),
                sessionKey,
                LastfmCallbackUtils.createTransitiveCallback(callback));
    }

    public void scrobble(String artist, String title, String album, String timestamp,
                         final SimpleCallback<Object> callback) {
        String sessionKey = AuthorizationInfoManager.getLastfmKey();
        Map<String, String> params = new TreeMap<>();
        params.put("artist", artist);
        params.put("track", title);
        params.put("album", album);
        params.put("sk", sessionKey);
        params.put("method", "track.unlove");
        lastfmService.scrobble(artist,
                title,
                album,
                timestamp,
                apiHelper.generateApiSig(params),
                sessionKey,
                LastfmCallbackUtils.createTransitiveCallback(callback));
    }

    public void nowplaying(Track track, final SimpleCallback<Object> callback) {
        String sessionKey = AuthorizationInfoManager.getLastfmKey();
        Map<String, String> params = new TreeMap<>();
        String artist = track.getArtist();
        String title = track.getArtist();
        String album = track.getAlbum();
        params.put("artist", artist);
        params.put("track", title);
        params.put("sk", sessionKey);
        params.put("method", "track.updateNowPlaying");
        if (album != null) {
            params.put("album", album);
            lastfmService.nowplaying(artist,
                    title,
                    album,
                    apiHelper.generateApiSig(params),
                    sessionKey,
                    LastfmCallbackUtils.createTransitiveCallback(callback));
        } else {
            lastfmService.nowplaying(artist,
                    title,
                    apiHelper.generateApiSig(params),
                    sessionKey,
                    LastfmCallbackUtils.createTransitiveCallback(callback));
        }
    }

    public void getTrackInfo(Track track, String username, final SimpleCallback<LastfmTrack> callback) {
        lastfmService.getTrackInfo(track.getArtist(), track.getTitle(), username,
                new LastfmCallback<LastfmTrackRoot>() {
                    @Override
                    public void success(LastfmTrackRoot data) {
                        callback.success(data.getTrack());
                    }

                    @Override
                    public void failure(String error) {
                        callback.failure(error);
                    }
                });
    }
}
