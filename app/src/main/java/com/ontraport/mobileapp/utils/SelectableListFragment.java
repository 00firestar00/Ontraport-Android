package com.ontraport.mobileapp.utils;

import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.ontraport.mobileapp.R;

public abstract class SelectableListFragment<A extends SelectableItemAdapter>
        extends Fragment implements ActionMode.Callback {

    private A adapter;
    private ActionMode action_mode;
    private String format = "%d selected";

    public A getAdapter() {
        return adapter;
    }

    public ActionMode getActionMode() {
        return action_mode;
    }

    public void setSelectedTextFormat(String format) {
        this.format = format;
    }

    public String getSelectedTextFormat(int count) {
        return String.format(format, count);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.action_tag).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_campaign).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_sequence).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_field).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @CallSuper
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.removeSelection();
        if (action_mode != null) {
            action_mode = null;
        }
    }

    protected final RecyclerView setRecyclerView(View view, A adapter, LinearLayoutManager manager) {
        this.adapter = adapter;

        RecyclerView recycler_view = view.findViewById(R.id.collection);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(manager);
        recycler_view.addOnItemTouchListener(new OnTouchListener(getContext(), recycler_view) {
            @Override
            public void onClick(View view, int position) {
                if (action_mode != null) {
                    onListItemSelect(position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                onListItemSelect(position);
            }
        });
        recycler_view.setAdapter(adapter);
        return recycler_view;
    }

    protected final void onListItemSelect(int position) {
        adapter.toggleSelection(position);

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;

        if (hasCheckedItems && action_mode == null && getActivity() != null) {
            action_mode = getActivity().startActionMode(this);
        }
        else if (!hasCheckedItems && action_mode != null) {
            action_mode.finish();
        }

        if (action_mode != null) {
            action_mode.setTitle(getSelectedTextFormat(adapter.getSelectedCount()));
        }
    }
}
