package com.ontraport.mobileapp.main.collection.views;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.main.collection.CollectionAdapter;
import com.ontraport.sdk.http.RequestParams;

public class ObjectSearchView extends SearchView
        implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private Activity activity;
    private CollectionAdapter adapter;
    private RequestParams params;

    public ObjectSearchView(Context context) {
        super(context);
    }

    public void init(Activity activity, RequestParams params, CollectionAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        this.params = params;
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);

        setOnQueryTextListener(this);
        if (manager == null) {
            return;
        }

        setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        params.put("search", query);

        OntraportApplication.getInstance().getCollection(adapter, params, true);

        Toast.makeText(activity, "Searching for: " + query, Toast.LENGTH_SHORT).show();
        clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        // true here because we do want it to expand, but we aren't taking action
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        Toast.makeText(activity, "close", Toast.LENGTH_SHORT).show();
        params.remove("search");
        return true;
    }
}
