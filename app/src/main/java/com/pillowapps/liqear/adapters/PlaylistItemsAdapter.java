package com.pillowapps.liqear.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pillowapps.liqear.R;
import com.pillowapps.liqear.entities.Track;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class PlaylistItemsAdapter extends ArrayAdapter<Track> {
    private final Context context;

    private List<Track> tracks;
    private List<Track> original;
    private PlaylistItemsFilter playlistItemsFilter;

    private boolean editMode = false;
    private int currentIndex = -1;
    private LinkedList<Integer> queue = new LinkedList<>();

    public PlaylistItemsAdapter(Context context) {
        super(context, R.layout.playlist_tab_list_item);
        this.context = context;
        this.tracks = new ArrayList<>();
        this.original = new ArrayList<>();
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setQueue(LinkedList<Integer> queue) {
        this.queue = queue;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public void toggleEditMode() {
        this.editMode = !editMode;
        notifyDataSetChanged();
    }

    public List<Track> getValues() {
        return tracks;
    }

    public void setValues(List<Track> tracks) {
        Timber.d("setValues: " + tracks);
        if (tracks == null) {
            return;
        }
        this.tracks.clear();
        this.original.clear();
        this.tracks.addAll(tracks);
        this.original.addAll(tracks);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Track getItem(int position) {
        if (position >= tracks.size()) {
            return null;
        }
        return tracks.get(position);
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position < 0 || position >= tracks.size()) return convertView;
        int realPosition = tracks.get(position).getRealPosition();
        Track currentTrack = original.get(realPosition);
        Timber.d("getView Playlist : " + currentTrack.toString());
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.playlist_tab_list_item, parent, false);
            holder = new ViewHolder();
            holder.artistTextView = (TextView) convertView.findViewById(R.id.artist_list_item);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title_list_item);
            holder.positionTextView =
                    (TextView) convertView.findViewById(R.id.position_text_view_list_item);
            holder.queueTextView =
                    (TextView) convertView.findViewById(R.id.queue_number_text_view_list_item);
            holder.playImageView = convertView.findViewById(R.id.play_image_view_list_item);
            holder.grabber = convertView.findViewById(R.id.grabber_list_item);
            holder.layout = convertView.findViewById(R.id.playlist_tab_item_main_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.layout.setBackgroundResource(position % 2 == 0 ?
                R.drawable.list_item_background : R.drawable.list_item_background_tinted);

        holder.positionTextView.setText(String.valueOf(currentTrack.getRealPosition() + 1));
        holder.artistTextView.setText(Html.fromHtml(currentTrack.getArtist()));
        holder.titleTextView.setText(Html.fromHtml(currentTrack.getTitle()));

        holder.playImageView.setVisibility(currentIndex == realPosition ? View.VISIBLE : View.INVISIBLE);

        int queueIndex = queue.indexOf(realPosition);
        holder.queueTextView.setText(queueIndex++ != -1 ? String.format("(%d)", queueIndex) : "");
        if (!editMode) {
            holder.grabber.setVisibility(View.GONE);
        } else {
            holder.grabber.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (playlistItemsFilter == null) {
            playlistItemsFilter = new PlaylistItemsFilter();
        }
        return playlistItemsFilter;
    }

    static class ViewHolder {
        TextView artistTextView;
        TextView titleTextView;
        TextView positionTextView;
        TextView queueTextView;
        View grabber;
        View playImageView;
        View layout;
    }

    private class PlaylistItemsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.length() > 0) {
                List<Track> founded = new ArrayList<>();
                for (Track t : original) {
                    if (t == null) {
                        break;
                    }
                    String artist = t.getArtist();
                    String title = t.getTitle();
                    if (artist != null && title != null) {
                        if (constraint.toString().startsWith("-") && constraint.length() > 1) {
                            String subConstraint =
                                    constraint.subSequence(1, constraint.length()).toString();
                            if (!artist.toLowerCase()
                                    .contains(subConstraint) && !title.toLowerCase().contains(
                                    subConstraint)) {
                                founded.add(t);
                            }
                        } else {
                            if (artist.toLowerCase().contains(constraint)
                                    || title.toLowerCase().contains(constraint)) {
                                founded.add(t);
                            }
                        }
                    }
                }
                result.values = founded;
                result.count = founded.size();
            } else {
                result.values = original;
                result.count = original.size();
            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            tracks.clear();
            List<Track> values = (List<Track>) filterResults.values;
            tracks.addAll(values);
            notifyDataSetChanged();
        }
    }
}
