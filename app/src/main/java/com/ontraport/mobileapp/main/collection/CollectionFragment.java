package com.ontraport.mobileapp.main.collection;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.MainActivity;
import com.ontraport.mobileapp.main.collection.views.ObjectSearchView;
import com.ontraport.mobileapp.main.record.RecordFragment;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.mobileapp.utils.EndlessScrollListener;
import com.ontraport.mobileapp.utils.SelectableListFragment;
import com.ontraport.sdk.http.RequestParams;

public class CollectionFragment extends SelectableListFragment<CollectionAdapter>
        implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ActionMode.Callback {

    private MainActivity activity;
    private SwipeRefreshLayout swipe_layout;
    private RequestParams params = new RequestParams();
    private RequestParams current_params = new RequestParams();
    private int object_id = 0;
    private String label;
    @DrawableRes
    private int icon;
    @ColorInt
    private int theme;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.collection_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            object_id = bundle.getInt(Constants.OBJECT_TYPE_ID, 0);
            icon = bundle.getInt("icon", R.drawable.ic_person_black_24dp);
            int color = bundle.getInt("theme", R.color.colorAccent);
            theme = getResources().getColor(color);
        }
        params.put(Constants.OBJECT_TYPE_ID, object_id);

        activity = (MainActivity) getActivity();
        if (activity != null) {
            label = activity.getCollectionLabel(object_id);
            activity.onCollectionFragmentSetTitle(label);
        }

        setSelectedTextFormat("%d");
        setHasOptionsMenu(true);

        FloatingActionButton fab = root_view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.setBackgroundTintList(ColorStateList.valueOf(theme));
        //fab.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_person_add_black_24dp));

        swipe_layout = root_view.findViewById(R.id.swipeContainer);
        swipe_layout.setOnRefreshListener(this);

        current_params.putAll(params);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        RecyclerView recycler_view = setRecyclerView(root_view, new CollectionAdapter(params, activity, theme), manager);
        recycler_view.addOnScrollListener(new EndlessScrollListener(manager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (page >= getAdapter().getMaxPages()) {
                    return false;
                }
                System.out.println("getting more data");

                int next_count = --page * 50;
                current_params.put("start", next_count);
                OntraportApplication.getInstance().getCollection(getAdapter(), current_params);
                return true;
            }
        });

        OntraportApplication.getInstance().getCollection(getAdapter(), params);
        return root_view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_collection, menu);
        setIconsToColor(menu, Color.WHITE);

        MenuItem search_item = menu.findItem(R.id.action_search);
        final ObjectSearchView view = (ObjectSearchView) search_item.getActionView();
        view.init(getActivity(), current_params, getAdapter());
        search_item.setOnActionExpandListener(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group:
                return true;
            case R.id.action_select_group:
                return true;
            case R.id.action_select_page:
                return true;
            case R.id.action_select_deselect:
                return true;
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        RecordFragment fragment = new RecordFragment();
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(Constants.OBJECT_TYPE_ID, object_id);
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("record_" + object_id + "_" + "create")
                .commit();
    }

    @Override
    public void onRefresh() {
        OntraportApplication.getInstance().getCollection(getAdapter(), params, true);
        swipe_layout.setRefreshing(false);
        if (getActionMode() != null) {
            getActionMode().finish();
        }
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

        final SparseBooleanArray selectables = getAdapter().getSelectedIds();
        final int num_selected = selectables.size();

        switch (item.getItemId()) {
            case R.id.action_tag:
                new AddRemoveDialog(activity, label) {
                    @Override
                    void onCancel() {
                        mode.finish();
                    }

                    @Override
                    void onSuccess() {
                        mode.finish();
                    }

                }.show();
                break;
            case R.id.action_campaign:
                new AddRemoveDialog(activity, label) {
                    @Override
                    void onCancel() {
                        mode.finish();
                    }

                    @Override
                    void onSuccess() {
                        mode.finish();
                    }

                }.show();
                break;
            case R.id.action_sequence:
                new AddRemoveDialog(activity, label) {
                    @Override
                    void onCancel() {
                        mode.finish();
                    }

                    @Override
                    void onSuccess() {
                        mode.finish();
                    }

                }.show();
                break;
            case R.id.action_field:
                mode.finish();
                break;
            case R.id.action_delete:
                new DeleteDialog(activity, num_selected, label) {
                    @Override
                    void onCancel() {
                        mode.finish();
                    }

                    @Override
                    void onIncorrectInput() {
                        Toast.makeText(getActivity(), "You did not type DELETE", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    void onSuccess() {
                        RequestParams delete_params = new RequestParams();
                        delete_params.put(Constants.OBJECT_TYPE_ID, object_id);
                        String[] ids = new String[num_selected];

                        for (int i = (selectables.size() - 1); i >= 0; i--) {
                            if (selectables.valueAt(i)) {
                                RecordInfo data = getAdapter().getDataAtPosition(selectables.keyAt(i));
                                ids[i] = Integer.toString(data.getId());
                                getAdapter().removeAt(selectables.keyAt(i));
                            }
                        }
                        delete_params.put("ids", TextUtils.join(",", ids));

                        //new DeleteAsyncTask(getAdapter(), delete_params).execute(delete_params);

                        Toast.makeText(getActivity(), num_selected + " item deleted.", Toast.LENGTH_SHORT).show();
                        mode.finish();
                    }

                }.show();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        boolean ret = super.onCreateActionMode(mode, menu);
        ActionBarContextView actionBar = activity.getWindow().getDecorView().findViewById(R.id.action_mode_bar);
        actionBar.setBackgroundColor(theme);
        setIconsToColor(menu, Color.WHITE);
        return ret;
    }

    private void setIconsToColor(Menu menu, @ColorInt int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setTint(color);
            }
        }
    }
}
