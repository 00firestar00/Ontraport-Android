package com.ontraport.mobileapp.main.collection;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.MainActivity;
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
    private RequestParams endless = new RequestParams();
    private int object_id = 0;
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
        endless.putAll(params);
        activity = (MainActivity) getActivity();
        activity.onCollectionFragmentSetTitle(params.getAsInt(Constants.OBJECT_TYPE_ID));

        FloatingActionButton fab = root_view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.setBackgroundTintList(ColorStateList.valueOf(theme));
        //fab.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_person_add_black_24dp));

        swipe_layout = root_view.findViewById(R.id.swipeContainer);
        swipe_layout.setOnRefreshListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        RecyclerView recycler_view = setRecyclerView(root_view, new CollectionAdapter(params, activity, theme), manager);
        recycler_view.addOnScrollListener(new EndlessScrollListener(manager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                int next_count = --page * 50;
                if (page >= adapter.getMaxPages()) {
                    return false;
                }
                System.out.println("getting more data");
                endless.put("start", next_count);
                OntraportApplication.getInstance().getCollection(getAdapter(), endless);
                return true;
            }
        });

        OntraportApplication.getInstance().getCollection(getAdapter(), params);
        return root_view;
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
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                SparseBooleanArray selected = getAdapter().getSelectedIds();

                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        RecordInfo data = getAdapter().getDataAtPosition(selected.keyAt(i));
                        getAdapter().notifyDataSetChanged();
                    }
                }
                Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();
                mode.finish();
                break;
            case R.id.action_copy:
                selected = getAdapter().getSelectedIds();
                int selectedMessageSize = selected.size();

                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        RecordInfo data = getAdapter().getDataAtPosition(selected.keyAt(i));
                        Log.e("Selected Items", "Title - " + data.get("id") + "n" + "Sub Title - " + data.get("email"));
                    }
                }
                Toast.makeText(activity, "You selected Copy menu.", Toast.LENGTH_SHORT).show();
                mode.finish();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        boolean ret = super.onCreateActionMode(mode, menu);
        ActionBarContextView actionBar = activity.getWindow().getDecorView().findViewById(R.id.action_mode_bar);
        actionBar.setBackgroundColor(theme);
        return ret;
    }
}
