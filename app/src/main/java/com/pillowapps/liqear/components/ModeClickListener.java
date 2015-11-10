package com.pillowapps.liqear.components;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.pillowapps.liqear.R;
import com.pillowapps.liqear.activities.MainActivity;
import com.pillowapps.liqear.activities.SetlistsActivity;
import com.pillowapps.liqear.activities.modes.LastfmNeighboursActivity;
import com.pillowapps.liqear.activities.modes.LastfmRecommendationsActivity;
import com.pillowapps.liqear.activities.modes.LocalAlbumsActivity;
import com.pillowapps.liqear.activities.modes.LocalArtistsActivity;
import com.pillowapps.liqear.activities.modes.LocalTracksActivity;
import com.pillowapps.liqear.activities.modes.NewcomersActivity;
import com.pillowapps.liqear.activities.modes.SearchAlbumActivity;
import com.pillowapps.liqear.activities.modes.SearchArtistActivity;
import com.pillowapps.liqear.activities.modes.SearchLastfmUserActivity;
import com.pillowapps.liqear.activities.modes.SearchSimpleTrackActivity;
import com.pillowapps.liqear.activities.modes.SearchTagActivity;
import com.pillowapps.liqear.activities.modes.VkFriendsActivity;
import com.pillowapps.liqear.activities.modes.VkGroupsActivity;
import com.pillowapps.liqear.activities.modes.VkRecommendationsActivity;
import com.pillowapps.liqear.activities.viewers.LastfmChartsViewerActivity;
import com.pillowapps.liqear.activities.viewers.LastfmUserViewerActivity;
import com.pillowapps.liqear.activities.viewers.VkUserViewerActivity;
import com.pillowapps.liqear.entities.Category;
import com.pillowapps.liqear.entities.ListItem;
import com.pillowapps.liqear.entities.Mode;
import com.pillowapps.liqear.entities.ModeEnum;
import com.pillowapps.liqear.entities.User;
import com.pillowapps.liqear.helpers.AuthorizationInfoManager;
import com.pillowapps.liqear.helpers.Constants;
import com.pillowapps.liqear.helpers.NetworkUtils;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapterWrapper;

public class ModeClickListener implements android.widget.AdapterView.OnItemClickListener {
    private MainActivity activity;

    public ModeClickListener(MainActivity context) {
        this.activity = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Mode mode;
        if (adapterView.getAdapter() instanceof StickyGridHeadersBaseAdapterWrapper) {
            StickyGridHeadersBaseAdapterWrapper adapter =
                    ((StickyGridHeadersBaseAdapterWrapper) adapterView.getAdapter());
            StickyGridHeadersBaseAdapter wrappedAdapter = adapter.getWrappedAdapter();
            mode = (Mode) wrappedAdapter.getItem(position);
        } else {
            Object itemAtPosition = adapterView.getItemAtPosition(position);
            if (itemAtPosition instanceof ListItem) {
                mode = ((ListItem) itemAtPosition).getMode();
            } else {
                return;
            }
        }
        if (mode.isNeedLastfm() && !AuthorizationInfoManager.isAuthorizedOnLastfm()) {
            Toast.makeText(activity, R.string.last_fm_not_authorized, Toast.LENGTH_SHORT).show();
            return;
        } else if ((mode.getCategory() == Category.VK || mode.getModeEnum() == ModeEnum.RADIOMIX
                || mode.getModeEnum() == ModeEnum.LIBRARY)
                && NetworkUtils.isOnline() && !AuthorizationInfoManager.isAuthorizedOnVk()) {
            Toast.makeText(activity, R.string.vk_not_authorized, Toast.LENGTH_SHORT).show();
            return;
        } else if (mode.getCategory() != Category.LOCAL && !NetworkUtils.isOnline()) {
            Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (mode.getModeEnum()) {
            case LOVED: {
                Intent intent = new Intent(activity, LastfmUserViewerActivity.class);
                intent.putExtra(LastfmUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getLastfmName()));
                intent.putExtra(LastfmUserViewerActivity.TAB_INDEX,
                        LastfmUserViewerActivity.LOVED_INDEX);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] User Loved");
            }
            break;
            case TOP_TRACKS: {
                Intent intent = new Intent(activity, LastfmUserViewerActivity.class);
                intent.putExtra(LastfmUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getLastfmName()));
                intent.putExtra(LastfmUserViewerActivity.TAB_INDEX,
                        LastfmUserViewerActivity.TOP_TRACKS_INDEX);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Top Tracks");
            }
            break;
            case TOP_ARTISTS: {
                Intent intent = new Intent(activity, LastfmUserViewerActivity.class);
                intent.putExtra(LastfmUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getLastfmName()));
                intent.putExtra(LastfmUserViewerActivity.TAB_INDEX,
                        LastfmUserViewerActivity.TOP_ARTISTS_INDEX);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Top Artists");
            }
            break;
            case CHARTS: {
                Intent intent = new Intent(activity, LastfmChartsViewerActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Charts");
            }
            break;
            case LIBRARY: {
                activity.openLibrary();
                sendAnalyticsModeClickEvent("[LAST.FM] Library");
            }
            break;
            case ARTIST_RADIO: {
                Intent intent = new Intent(activity, SearchArtistActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Artist search");
            }
            break;
            case TAG_RADIO: {
                Intent intent = new Intent(activity, SearchTagActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Tag search");
            }
            break;
            case ALBUM_RADIO: {
                Intent intent = new Intent(activity, SearchAlbumActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Album search");
            }
            break;
            case RECOMMENDATIONS: {
                Intent intent = new Intent(activity, LastfmRecommendationsActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Recommended");
            }
            break;
            case RADIOMIX: {
                activity.openRadiomix();
                sendAnalyticsModeClickEvent("[LAST.FM] Radiomix");
            }
            break;
            case NEIGHBOURS: {
                Intent intent = new Intent(activity, LastfmNeighboursActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Neighbours");
            }
            break;
            case FRIENDS_LAST: {
                Intent intent = new Intent(activity, SearchLastfmUserActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Friends");
            }
            break;
            case RECENT_LAST: {
                Intent intent = new Intent(activity, LastfmUserViewerActivity.class);
                intent.putExtra(LastfmUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getLastfmName()));
                intent.putExtra(LastfmUserViewerActivity.TAB_INDEX,
                        LastfmUserViewerActivity.RECENT_INDEX);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LAST.FM] Recent");
            }
            break;
            case USER_AUDIO_VK: {
                Intent intent = new Intent(activity, VkUserViewerActivity.class);
                intent.putExtra(VkUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getVkName(),
                                AuthorizationInfoManager.getVkUserId()));
                intent.putExtra(VkUserViewerActivity.TAB_INDEX,
                        VkUserViewerActivity.USER_AUDIO_INDEX);
                intent.putExtra(VkUserViewerActivity.YOU_MODE, true);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] User Audio");
            }
            break;
            case GROUP_VK: {
                Intent intent = new Intent(activity, VkGroupsActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Group");
            }
            break;
            case FRIENDS_VK: {
                Intent intent = new Intent(activity, VkFriendsActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Friends");
            }
            break;
            case SEARCH_VK: {
                Intent intent = new Intent(activity, SearchSimpleTrackActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Search");
            }
            break;
            case ALBUMS_VK: {
                Intent intent = new Intent(activity, VkUserViewerActivity.class);
                intent.putExtra(VkUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getVkName(),
                                AuthorizationInfoManager.getVkUserId()));
                intent.putExtra(VkUserViewerActivity.TAB_INDEX, VkUserViewerActivity.ALBUM_INDEX);
                intent.putExtra(VkUserViewerActivity.YOU_MODE, true);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Albums");
            }
            break;
            case RECOMMENDATIONS_VK: {
                Intent intent = new Intent(activity, VkRecommendationsActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Recommendations");
            }
            break;
            case WALL_VK: {
                Intent intent = new Intent(activity, VkUserViewerActivity.class);
                intent.putExtra(VkUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getVkName(),
                                AuthorizationInfoManager.getVkUserId()));
                intent.putExtra(VkUserViewerActivity.TAB_INDEX, VkUserViewerActivity.WALL_INDEX);
                intent.putExtra(VkUserViewerActivity.YOU_MODE, true);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Wall");
            }
            break;
            case FAVORITES_VK: {
                Intent intent = new Intent(activity, VkUserViewerActivity.class);
                intent.putExtra(VkUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getVkName(),
                                AuthorizationInfoManager.getVkUserId()));
                intent.putExtra(VkUserViewerActivity.TAB_INDEX, VkUserViewerActivity.FAVORITES);
                intent.putExtra(VkUserViewerActivity.YOU_MODE, true);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Favorites");
            }
            break;
            case FEED_VK: {
                Intent intent = new Intent(activity, VkUserViewerActivity.class);
                intent.putExtra(VkUserViewerActivity.USER,
                        new User(AuthorizationInfoManager.getVkName(),
                                AuthorizationInfoManager.getVkUserId()));
                intent.putExtra(VkUserViewerActivity.TAB_INDEX, VkUserViewerActivity.NEWS_FEED);
                intent.putExtra(VkUserViewerActivity.YOU_MODE, true);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[VK] Feed");
            }
            break;
            case FUNKY: {
                Intent intent = new Intent(activity, NewcomersActivity.class);
                intent.putExtra(NewcomersActivity.MODE,
                        NewcomersActivity.Mode.FUNKYSOULS);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[OTHER] FunkySouls");
            }
            break;
            case ALTERPORTAL: {
                Intent intent = new Intent(activity, NewcomersActivity.class);
                intent.putExtra(NewcomersActivity.MODE,
                        NewcomersActivity.Mode.ALTERPORTAL);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[OTHER] Alterportal");
            }
            break;
            case SETLIST: {
                Intent intent = new Intent(activity, SetlistsActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[OTHER] Setlists");
            }
            break;
            case LOCAL_ARTISTS: {
                Intent intent = new Intent(activity, LocalArtistsActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LOCAL] Artists");
            }
            break;
            case LOCAL_TRACKS: {
                Intent intent = new Intent(activity, LocalTracksActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LOCAL] Tracks");
            }
            break;
            case LOCAL_ALBUMS: {
                Intent intent = new Intent(activity, LocalAlbumsActivity.class);
                activity.startActivityForResult(intent, Constants.MAIN_REQUEST_CODE);
                sendAnalyticsModeClickEvent("[LOCAL] Albums");
            }
            break;
            default:
                break;
        }
    }

    private void sendAnalyticsModeClickEvent(String mode) {
        EasyTracker easyTracker = EasyTracker.getInstance(activity);
        easyTracker.send(MapBuilder
                        .createEvent("ui_action",     // Event category (required)
                                "mode_click",  // Event action (required)
                                mode,   // Event label
                                null)            // Event value
                        .build()
        );
    }
}