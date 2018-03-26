package com.ontraport.mobileapp.main.collection;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

        final RequestParams endless = new RequestParams();
        endless.putAll(params);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        RecyclerView recycler_view = setRecyclerView(root_view, new CollectionAdapter(params, activity, theme), manager);
        recycler_view.addOnScrollListener(new EndlessScrollListener(manager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                int next_count = --page * 50;
                if (page >= getAdapter().getMaxPages()) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_collection, menu);
        super.onCreateOptionsMenu(menu, inflater);
        setIconsToColor(menu, Color.WHITE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
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
        switch (item.getItemId()) {
            case R.id.action_delete:
                final SparseBooleanArray deletables = getAdapter().getSelectedIds();

                for (int i = (deletables.size() - 1); i >= 0; i--) {
                    if (deletables.valueAt(i)) {
                        RecordInfo data = getAdapter().getDataAtPosition(deletables.keyAt(i));
                        getAdapter().notifyDataSetChanged();
                    }
                }

                final int num_deleted = deletables.size();

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("DELETE");
                builder.setMessage("Are you sure you want to delete these " + num_deleted + " " + label + "?\nPlease type DELETE to confirm.");

                final EditText input = new EditText(activity);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                builder.setView(input);

                builder.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mode.finish();
                                dialog.cancel();
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText().toString().equals("DELETE")) {
                                    Toast.makeText(getActivity(), num_deleted + " item deleted.", Toast.LENGTH_SHORT).show();
                                    mode.finish();
                                }
                                else {
                                    Toast.makeText(getActivity(), "You did not type DELETE", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                builder.show();
                break;
            case R.id.action_field:
                final SparseBooleanArray editables = getAdapter().getSelectedIds();
                int selectedMessageSize = editables.size();

                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (editables.valueAt(i)) {
                        RecordInfo data = getAdapter().getDataAtPosition(editables.keyAt(i));
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
