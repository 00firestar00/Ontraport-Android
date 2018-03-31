package com.ontraport.mobileapp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.collection.CollectionFragment;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.mobileapp.utils.ThemeUtils;
import com.ontraport.sdk.http.CustomObjectResponse;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentManager.OnBackStackChangedListener {

    private SparseArray<Bundle> nav_info = new SparseArray<>();
    private NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        buildNavMenu(nav_view);
        nav_view.setCheckedItem(R.id.nav_contacts);

        // Contacts Collection
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, getObjectFragment(0))
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            id = 0;
        }

        Bundle bundle = nav_info.get(id);

        if (bundle != null) {
            CollectionFragment fragment = new CollectionFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack("collection_" + bundle.getInt(Constants.OBJECT_TYPE_ID))
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackStackChanged() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            String tag = getSupportFragmentManager().getBackStackEntryAt(count - 1).getName();
            if (tag.contains("collection_")) {
                String object_id = tag.split("collection_")[1];
                int item_id = Integer.parseInt(object_id);
                if (item_id == 0) {
                    item_id = R.id.nav_contacts;
                }
                nav_view.setCheckedItem(item_id);
            }
            return;
        }
        nav_view.setCheckedItem(R.id.nav_contacts);
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
        bundle.putInt(Constants.OBJECT_TYPE_ID, object_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void buildNavMenu(NavigationView nav) {
        OntraportApplication app = (OntraportApplication) getApplication();
        CustomObjectResponse res = app.getCustomObjects();
        Menu menu = nav.getMenu();
        if (res != null && res.getData().length > 0) {
            for (CustomObjectResponse.Data data : res.getData()) {
                int id = Integer.parseInt(data.getId());

                String name = data.getPlural();
                int icon = ThemeUtils.getIconByName(data.getIcon());
                int theme = ThemeUtils.getThemeByName(data.getTheme());

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putInt(Constants.OBJECT_TYPE_ID, id);
                bundle.putInt("icon", icon);
                bundle.putInt("theme", theme);
                nav_info.put(id, bundle);

                if (id < 10000) {
                    continue;
                }
                menu.add(R.id.nav_object, id, Menu.NONE, name).setCheckable(true).setIcon(icon);
            }
        }
        menu.setGroupCheckable(R.id.nav_object, true, true);
    }
}
