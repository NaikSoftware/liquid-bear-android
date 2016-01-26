package com.pillowapps.liqear.entities.lastfm.roots;

import com.google.gson.annotations.SerializedName;
import com.pillowapps.liqear.entities.lastfm.LastfmAlbum;
import com.pillowapps.liqear.entities.lastfm.LastfmResponse;

public class LastfmAlbumRoot extends LastfmResponse {

    @SerializedName("album")
    LastfmAlbum album;

    public LastfmAlbumRoot() {
    }

    public LastfmAlbum getAlbum() {
        return album;
    }

    @Override
    public String toString() {
        return "LastfmAlbumRoot{" +
                "albums=" + album +
                '}';
    }
}
