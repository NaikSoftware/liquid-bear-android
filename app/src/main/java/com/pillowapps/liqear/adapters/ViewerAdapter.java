package com.pillowapps.liqear.adapters;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.pillowapps.liqear.components.viewers.ViewerPage;

import java.util.List;

public class ViewerAdapter extends PagerAdapter {
    private List<ViewerPage> pages;

    public ViewerAdapter(List<ViewerPage> pages) {
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
        View v = pages.get(position).getView();
        pager.addView(v, 0);
        return v;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).getTitle();
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
}