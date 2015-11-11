package com.pillowapps.liqear.components.viewers;

import android.content.Context;
import android.view.View;

import com.pillowapps.liqear.adapters.AlbumAdapter;
import com.pillowapps.liqear.entities.Album;
import com.pillowapps.liqear.entities.vk.VkAlbum;
import com.pillowapps.liqear.helpers.Converter;

import java.util.List;

public class VkAlbumViewerPage extends ViewerPage<Album> {
    private AlbumAdapter adapter;

    public VkAlbumViewerPage(Context context,
                             View view,
                             String title) {
        super(context, view, title);
    }

    public VkAlbumViewerPage(Context context,
                             View view,
                             int titleRes) {
        super(context, view, titleRes);
    }

    @Override
    public boolean isNotLoaded() {
        return adapter == null;
    }

    public void setAdapter(AlbumAdapter adapter) {
        this.adapter = adapter;
    }


    public List<Album> getItems() {
        return adapter.getItems();
    }

    public void clear() {
//        adapter.clear(adapter.getItems());
    }

    @Override
    protected void onItemClicked(int position) {
        onViewerItemClicked(adapter.getItems(), position);
    }

    @Override
    protected boolean onItemLongClicked(int position) {
        onViewerItemLongClicked(adapter.getItems(), position);
        return true;
    }

    public void fill(List<VkAlbum> albums) {
        int adapterSize = adapter == null ? 0 : adapter.getItemCount();

        int count = adapterSize + albums.size();
        filledFull = (adapterSize == count);
        showEmptyPlaceholder(count == 0);
        showProgressBar(false);
        updateAdapter(Converter.convertVkAlbums(albums));
    }

    private void updateAdapter(List<Album> albums) {
        if (adapter == null) {
            adapter = new AlbumAdapter(albums, listener);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.addAll(albums);
            onLoadMoreComplete();
        }
    }
}