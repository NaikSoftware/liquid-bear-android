package com.pillowapps.liqear.activities.modes;

import android.os.Bundle;
import android.view.View;

import com.pillowapps.liqear.activities.base.ListBaseActivity;
import com.pillowapps.liqear.adapters.recyclers.TrackAdapter;
import com.pillowapps.liqear.entities.Track;

import java.util.ArrayList;
import java.util.List;

public class NewcomersTracksActivity extends ListBaseActivity {

    private TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        List<String> stringArrayList = extras.getStringArrayList("tracks");
        String artist = extras.getString("artist");
        String albumTitle = extras.getString("title");
        setTitle(artist + " - " + albumTitle);
        List<Track> tracks = new ArrayList<>();
        if (stringArrayList != null) {
            for (String trackTitle : stringArrayList) {
                tracks.add(new Track(artist, trackTitle));
            }
        }
        fillWithVkTracklist(tracks);
    }

    private void fillWithVkTracklist(List<Track> trackList) {
        if (adapter == null || adapter.getItemCount() == 0) {
            adapter = new TrackAdapter(this, trackList,
                    (view, position) -> openMainPlaylist(adapter.getItems(), position, getToolbarTitle()),
                    (view, position) -> {
                        trackLongClick(adapter.getItems(), position);
                        return true;
                    });
            recycler.setAdapter(adapter);
        } else {
            adapter.addAll(trackList);
        }
        progressBar.setVisibility(View.GONE);
        updateEmptyTextView();
    }
}
