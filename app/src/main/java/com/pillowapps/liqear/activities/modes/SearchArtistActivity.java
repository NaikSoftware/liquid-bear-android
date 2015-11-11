package com.pillowapps.liqear.activities.modes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.pillowapps.liqear.R;
import com.pillowapps.liqear.adapters.ArtistAdapter;
import com.pillowapps.liqear.callbacks.SimpleCallback;
import com.pillowapps.liqear.entities.Artist;
import com.pillowapps.liqear.entities.lastfm.LastfmArtist;
import com.pillowapps.liqear.helpers.Constants;
import com.pillowapps.liqear.helpers.Converter;
import com.pillowapps.liqear.helpers.DelayedTextWatcher;
import com.pillowapps.liqear.helpers.SharedPreferencesManager;
import com.pillowapps.liqear.models.lastfm.LastfmArtistModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SearchArtistActivity extends SearchBaseActivity {

    private ArtistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar.setTitle(getString(R.string.artist_radio));
        editText.setHint(getString(R.string.artist_radio));
        editText.setFloatingLabelText(getString(R.string.artist_radio));
        progressBar.setVisibility(View.GONE);
        recycler.setOnCreateContextMenuListener(this);
        loadArtistPresets();

        initWatcher();
    }

    protected void initWatcher() {
        editText.addTextChangedListener(new DelayedTextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runTaskWithDelay(new Runnable() {
                    @Override
                    public void run() {
                        String searchQuery = editText.getText().toString().trim();
                        if (searchQuery.length() == 0) {
                            loadArtistPresets();
                            return;
                        }
                        searchArtist(searchQuery, getPageSize(), 1);
                    }
                });
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadArtistPresets() {
        LinkedHashSet<Artist> artists = new LinkedHashSet<>(Constants.PRESET_WANTED_COUNT);
        SharedPreferences artistPreferences = SharedPreferencesManager.getArtistPreferences();
        int artistCount = artistPreferences.getInt(Constants.PRESET_LAST_NUMBER, 0);
        if (artistCount >= Constants.PRESET_WANTED_COUNT) {
            for (int i = artistCount - 1; i >= artistCount - Constants.PRESET_WANTED_COUNT; i--) {
                Artist artist = new Artist(artistPreferences
                        .getString(Constants.ARTIST_NUMBER + (i % Constants.PRESET_WANTED_COUNT), ""));
                artist.setPreviewUrl(artistPreferences
                        .getString(Constants.IMAGE + (i % Constants.PRESET_WANTED_COUNT), ""));
                artists.add(artist);
            }
        } else {
            for (int i = artistCount - 1; i >= 0; i--) {
                Artist artist = new Artist(artistPreferences
                        .getString(Constants.ARTIST_NUMBER + i, ""));
                artist.setPreviewUrl(artistPreferences.getString(Constants.IMAGE + i, ""));
                artists.add(artist);
            }
        }
        fillWithArtists(new ArrayList<>(artists));
    }

    private void fillWithArtists(List<Artist> artists) {
        emptyTextView.setVisibility(artists.size() == 0 ? View.VISIBLE : View.GONE);
        adapter = new ArtistAdapter(artists, new OnRecyclerItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                SharedPreferences artistPreferences = SharedPreferencesManager.getArtistPreferences();
                SharedPreferences.Editor editor = artistPreferences.edit();
                int artistLastNumberAll = artistPreferences.getInt(Constants.PRESET_LAST_NUMBER, 0);
                int artistsLastNumberMod = artistLastNumberAll % Constants.PRESET_WANTED_COUNT;
                editor.putInt(Constants.PRESET_LAST_NUMBER, artistLastNumberAll + 1);
                Artist artist = adapter.getItem(position);
                editor.putString(Constants.ARTIST_NUMBER + artistsLastNumberMod, artist.getName());
                editor.putString(Constants.IMAGE + artistsLastNumberMod,
                        artist.getPreviewUrl());
                editor.apply();
                openArtistByName(artist.getName());
            }
        });
        recycler.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private void searchArtist(String searchQuery, int limit, int page) {
        new LastfmArtistModel().searchArtist(searchQuery,
                limit,
                page,
                new SimpleCallback<List<LastfmArtist>>() {
                    @Override
                    public void success(List<LastfmArtist> lastfmArtists) {
                        progressBar.setVisibility(View.GONE);
                        fillWithArtists(Converter.convertArtistList(lastfmArtists));
                    }

                    @Override
                    public void failure(String error) {
                        showError(error);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}