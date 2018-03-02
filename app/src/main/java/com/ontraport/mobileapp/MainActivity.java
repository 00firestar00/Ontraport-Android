package com.ontraport.mobileapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.fragments.CollectionFragment;
import com.ontraport.sdk.http.Meta;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        final Menu menu = navigationView.getMenu();
//        for (int i = 1; i <= 3; i++) {
//            menu.add("Runtime item "+ i);
//        }
//        final SubMenu subMenu = menu.addSubMenu("SubMenu Title");
//        for (int i = 1; i <= 2; i++) {
//            subMenu.add("SubMenu Item " + i);
//        }
//

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, getObjectFragment(0))
                    .addToBackStack("collection_" + 0)
                    .commit();
        }
        if (id == R.id.nav_customobject1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, getObjectFragment(10000))
                    .addToBackStack("collection_" + 10000)
                    .commit();
        }
        if (id == R.id.nav_customobject2) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, getObjectFragment(10001))
                    .addToBackStack("collection_" + 10001)
                    .commit();
        }
        if (id == R.id.nav_customobject3) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, getObjectFragment(10002))
                    .addToBackStack("collection_" + 10002)
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
}
