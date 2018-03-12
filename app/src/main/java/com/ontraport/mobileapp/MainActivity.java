package com.ontraport.mobileapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import com.ontraport.mobileapp.fragments.CollectionFragment;
import com.ontraport.mobileapp.sdk.http.CustomObjectResponse;
import com.ontraport.mobileapp.utils.ThemeUtils;
import com.ontraport.sdk.http.Meta;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SparseArray<Bundle> nav_info = new SparseArray<>();

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

        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
        buildNavMenu(nav);
        // Contacts Collection
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    .addToBackStack("collection_" + bundle.getInt("objectID"))
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCollectionFragmentSetTitle(int object_id) {
        OntraportApplication app = (OntraportApplication) getApplication();
        Meta.Data meta_data = app.getMetaData(object_id);
        String name = meta_data.getName();
        if (name.startsWith("o") && object_id >= 10000) {
            name = name.substring(1);
        }
        if (!name.endsWith("s")) {
            name += "s";
        }
        setTitle(name);
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
        bundle.putInt("objectID", object_id);
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
                bundle.putInt("objectID", id);
                bundle.putInt("icon", icon);
                bundle.putInt("theme", theme);
                nav_info.put(id, bundle);

                if (id < 10000) {
                    continue;
                }
                menu.add(R.id.nav_object, id, Menu.NONE, name).setIcon(icon);
            }
        }
    }
}
