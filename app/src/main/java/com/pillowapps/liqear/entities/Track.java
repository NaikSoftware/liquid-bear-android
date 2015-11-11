package com.pillowapps.liqear.entities;

import com.pillowapps.liqear.helpers.TrackUtils;
import com.pillowapps.liqear.helpers.db.LiquidBearDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

@Table(databaseName = LiquidBearDatabase.NAME)
public class Track extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public Long id;

    @Column
    public String title;

    @Column
    public String artist;

    @Column
    public String album;

    @Column
    public long ownerId;

    @Column
    public long audioId;

    @Column
    public boolean local;

    @Column
    public String localUrl;

    private String url;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "playlistId",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    public ForeignKeyContainer<Playlist> playlistModelContainer;

    public int duration;

    private int realPosition;
    private boolean loved = false;
    private boolean addedToVk = false;

    public Track(long audioId, long oid) {
        this.audioId = audioId;
        this.ownerId = oid;
    }

    public Track(String artist, String title, String localUrl, boolean local) {
        this.artist = artist;
        this.title = title;
        this.localUrl = localUrl;
        this.local = local;
    }

    public Track(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    public Track() {
    }

    public Track(String artist, String title, String url) {
        this.artist = artist;
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getAudioId() {
        return audioId;
    }

    public void setAudioId(long audioId) {
        this.audioId = audioId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLoved() {
        return loved;
    }

    public void setLoved(boolean loved) {
        this.loved = loved;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public boolean isAddedToVk() {
        return addedToVk;
    }

    public void setAddedToVk(boolean addedToVk) {
        this.addedToVk = addedToVk;
    }

    public int getRealPosition() {
        return realPosition;
    }

    public void setRealPosition(int realPosition) {
        this.realPosition = realPosition;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void associatePlaylist(Playlist playlist) {
        playlistModelContainer = new ForeignKeyContainer<>(Playlist.class);
        playlistModelContainer.setModel(playlist);
        playlistModelContainer.put(Playlist$Table.ID, playlist.id);
    }

    @Override
    public String toString() {
        return "Track{" + TrackUtils.getNotation(this) + "}";
    }

    public String getLocalUrl() {
        return localUrl;
    }
}