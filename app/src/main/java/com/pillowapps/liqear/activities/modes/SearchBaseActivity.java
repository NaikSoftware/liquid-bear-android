package com.pillowapps.liqear.activities.modes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pillowapps.liqear.R;
import com.pillowapps.liqear.activities.MainActivity;
import com.pillowapps.liqear.components.HintMaterialEditText;
import com.pillowapps.liqear.components.LoadMoreRecyclerView;
import com.pillowapps.liqear.components.ResultActivity;

public abstract class SearchBaseActivity extends ResultActivity {

    protected HintMaterialEditText editText;
    protected LoadMoreRecyclerView recycler;
    protected TextView emptyTextView;
    protected ProgressBar progressBar;
    protected ActionBar actionBar;

    public View searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editText = (HintMaterialEditText) findViewById(R.id.search_edit_text_quick_search_layout);
        searchLayout = findViewById(R.id.edit_part_quick_search_layout);
        recycler = (LoadMoreRecyclerView) findViewById(R.id.list);
        emptyTextView = (TextView) findViewById(R.id.empty);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    protected abstract void initWatcher();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home: {
                finish();
                Intent intent = new Intent(SearchBaseActivity.this, MainActivity.class);
                intent.setAction(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            return true;
            default:
                return false;
        }
    }
}
