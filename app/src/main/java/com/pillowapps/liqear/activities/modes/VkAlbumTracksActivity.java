package com.pillowapps.liqear.activities.modes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pillowapps.liqear.R;
import com.pillowapps.liqear.adapters.TrackAdapter;
import com.pillowapps.liqear.callbacks.VkSimpleCallback;
import com.pillowapps.liqear.entities.Track;
import com.pillowapps.liqear.entities.vk.VkError;
import com.pillowapps.liqear.entities.vk.VkTrack;
import com.pillowapps.liqear.helpers.Converter;
import com.pillowapps.liqear.models.vk.VkAudioModel;

import java.util.List;

public class VkAlbumTracksActivity extends ListBaseActivity {

    public static final String TITLE = "title";
    public static final String UID = "uid";
    public static final String GID = "gid";
    public static final String ALBUM_ID = "album_id";
    private TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        actionBar.setTitle(extras.getString(TITLE));
        long uid = extras.getLong(UID, -1);
        if (uid == -1) {
            long gid = extras.getLong(GID, -1);
            getVkGroupAudioFromAlbum(gid, extras.getLong(ALBUM_ID));
        } else {
            getVkUserAudioFromAlbum(uid, extras.getLong(ALBUM_ID));
        }
    }

    private void fillWithVkTracklist(List<VkTrack> vkTracks) {
        final List<Track> trackList = Converter.convertVkTrackList(vkTracks);
        if (adapter == null || adapter.getItemCount() == 0) {
            emptyTextView.setVisibility(trackList.size() == 0 ? View.VISIBLE : View.GONE);
            adapter = new TrackAdapter(this, trackList, new OnRecyclerItemClickListener() {
                @Override
                public void onItemClicked(View view, int position) {
                    openMainPlaylist(adapter.getItems(), position, getToolbarTitle());
                }
            }, new OnRecyclerLongItemClickListener() {
                @Override
                public boolean onItemLongClicked(View view, int position) {
                    trackLongClick(adapter.getItems(), position);
                    return true;
                }
            });
            recycler.setAdapter(adapter);
        } else {
            adapter.addAll(trackList);
        }
        progressBar.setVisibility(View.GONE);
    }

    private void getVkUserAudioFromAlbum(long uid, long albumId) {
        new VkAudioModel().getUserAudioFromAlbum(uid, albumId, 0, 0,
                vkTracksCallback());
    }

    private VkSimpleCallback<List<VkTrack>> vkTracksCallback() {
        return new VkSimpleCallback<List<VkTrack>>() {
            @Override
            public void success(List<VkTrack> data) {
                fillWithVkTracklist(data);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void failure(VkError error) {
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    private void getVkGroupAudioFromAlbum(long gid, long albumId) {
        new VkAudioModel().getGroupAudioFromAlbum(gid, albumId, 0, 0,
                vkTracksCallback());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_playlist_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.to_playlist: {
                if (adapter == null) return true;
                addToMainPlaylist(adapter.getItems());
                Toast.makeText(VkAlbumTracksActivity.this, R.string.added, Toast.LENGTH_SHORT).show();
            }
            return true;
            case R.id.save_as_playlist: {
                if (adapter == null) return true;
                saveAsPlaylist(adapter.getItems());
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}