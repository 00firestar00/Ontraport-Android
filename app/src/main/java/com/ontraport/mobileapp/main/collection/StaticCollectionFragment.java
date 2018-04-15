package com.ontraport.mobileapp.main.collection;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public class StaticCollectionFragment extends CollectionFragment {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        boolean ret = super.onCreateActionMode(mode, menu);
        return ret;
    }

}
