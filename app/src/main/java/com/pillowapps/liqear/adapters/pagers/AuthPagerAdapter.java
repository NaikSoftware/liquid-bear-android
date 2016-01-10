package com.pillowapps.liqear.adapters.pagers;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.pillowapps.liqear.LBApplication;
import com.pillowapps.liqear.R;

import java.util.List;

public class AuthPagerAdapter extends PagerAdapter {
    public static final int LASTFM_TAB_INDEX = 1;
    public static final int VK_TAB_INDEX = 0;

    private List<View> views = null;
    private String[] titles = new String[2];

    {
        titles[VK_TAB_INDEX] = LBApplication.getAppContext().getString(R.string.vk);
        titles[LASTFM_TAB_INDEX] = LBApplication.getAppContext().getString(R.string.last_fm);
    }

    public AuthPagerAdapter(List<View> inViews) {
        views = inViews;
    }

    public String getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
        View v = views.get(position);
        pager.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object view) {
        pager.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void finishUpdate(ViewGroup view) {
    }

    @Override
    public void restoreState(Parcelable p, ClassLoader c) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup view) {
    }

    @Override
    public float getPageWidth(int position) {
        Resources resources = LBApplication.getAppContext().getResources();
        boolean isTablet = resources.getBoolean(R.bool.isTablet);
        if (isTablet && resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 0.5f;
        }
        return super.getPageWidth(position);
    }
}