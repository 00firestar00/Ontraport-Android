package com.ontraport.mobileapp.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.collection.CollectionFragment;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.mobileapp.utils.SelectableExpandableDrawerItem;
import com.ontraport.mobileapp.utils.ThemeUtils;
import com.ontraport.sdk.http.CustomObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements Drawer.OnDrawerItemClickListener,
        FragmentManager.OnBackStackChangedListener {

    private SparseArray<Bundle> nav_info = new SparseArray<>();
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withOnDrawerItemClickListener(this)
                .withAccountHeader(
                        new AccountHeaderBuilder()
                                .withActivity(this)
                                .withHeaderBackground(R.drawable.side_nav_bar)
                                .withSelectionListEnabledForSingleProfile(false)
                                .withProfileImagesClickable(false)
                                .addProfiles(
                                        new ProfileDrawerItem()
                                                .withName("ONTRAPORT")
                                                .withIcon(R.drawable.ic_launcher_round)
                                ).build()
                ).build();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer.getDrawerLayout(), toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setActionBarDrawerToggle(toggle);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        List<IDrawerItem> drawer_items = buildObjectsNavMenu(drawer.getAdapter());
        drawer_items.add(new DividerDrawerItem());
        drawer_items.addAll(getAccountActionsNavMenu());

        for (IDrawerItem item : drawer_items) {
            drawer.addItem(item);
        }

        // Contacts Collection
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, getObjectFragment(0))
                .commit();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        Integer id = (Integer) drawerItem.getTag();
        if (id == null) {
            return false;
        }

        Bundle bundle = nav_info.get(id);
        if (bundle == null) {
            return false;
        }

        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("collection_" + bundle.getInt(Constants.OBJECT_ID))
                .commit();

        if (drawerItem instanceof SelectableExpandableDrawerItem) {
            ((SelectableExpandableDrawerItem) drawerItem).collapse();
        }

        drawer.closeDrawer();
        return true;
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * Gets a new CollectionFragment from the Object ID.
     *
     * @param object_id The id of the object for the collection
     * @return a new CollectionFragment
     */
    private CollectionFragment getObjectFragment(int object_id) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.OBJECT_ID, object_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<IDrawerItem> buildObjectsNavMenu(FastAdapter<IDrawerItem> adapter) {

        List<IDrawerItem> objects = new ArrayList<>();

        objects.add(new SelectableExpandableDrawerItem(adapter)
                .withName("Contacts")
                .withTag(0)
                .withIcon(R.drawable.ic_person_black_24dp)
                .withSelectable(true)
                .withSubItems(getNewSubMenuItems())
                .withIconTintingEnabled(true)
                .withIconColorRes(R.color.colorAccent)
                .withSelectedIconColorRes(R.color.colorAccent));

        OntraportApplication app = (OntraportApplication) getApplication();
        CustomObjectResponse res = app.getCustomObjects();

        if (res != null && res.getData().length > 0) {
            for (CustomObjectResponse.Data data : res.getData()) {
                int id = Integer.parseInt(data.getId());

                String name = data.getPlural();
                int icon = ThemeUtils.getIconByName(data.getIcon());
                int theme = ThemeUtils.getThemeByName(data.getTheme());

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putInt(Constants.OBJECT_ID, id);
                bundle.putInt("icon", icon);
                bundle.putInt("theme", theme);
                nav_info.put(id, bundle);

                if (id < 10000) {
                    continue;
                }

                objects.add(new SelectableExpandableDrawerItem(adapter)
                        .withName(name)
                        .withTag(id)
                        .withIcon(icon)
                        .withSelectable(true)
                        .withSubItems(getNewSubMenuItems())
                        .withIconTintingEnabled(true)
                        .withIconColorRes(theme)
                        .withSelectedIconColorRes(theme));

            }
        }
        return objects;
    }

    private List<IDrawerItem> getNewSubMenuItems() {
        List<IDrawerItem> sub = new ArrayList<>();
        sub.add(new SecondaryDrawerItem().withName(R.string.action_campaign).withLevel(2));
        sub.add(new SecondaryDrawerItem().withName(R.string.action_sequence).withLevel(2));
        sub.add(new SecondaryDrawerItem().withName(R.string.action_rule).withLevel(2));
        sub.add(new SecondaryDrawerItem().withName(R.string.action_form).withLevel(2));
        sub.add(new SecondaryDrawerItem().withName(R.string.action_message).withLevel(2));
        return sub;
    }

    private List<IDrawerItem> getAccountActionsNavMenu() {
        List<IDrawerItem> sub = new ArrayList<>();
        sub.add(new PrimaryDrawerItem().withName(R.string.menu_account).withIcon(R.drawable.ic_person_black_24dp).withSelectable(true));
        sub.add(new PrimaryDrawerItem().withName(R.string.menu_admin).withIcon(R.drawable.ic_settings_black_24dp).withSelectable(true));
        sub.add(new PrimaryDrawerItem().withName(R.string.menu_users).withIcon(R.drawable.ic_supervisor_account_black_24dp).withSelectable(true));
        sub.add(new PrimaryDrawerItem().withName(R.string.menu_profile).withIcon(R.drawable.ic_account_box_black_24dp).withSelectable(true));
        sub.add(new PrimaryDrawerItem().withName(R.string.menu_logout).withIcon(R.drawable.ic_logout_24dp).withSelectable(true));
        return sub;
    }
}
