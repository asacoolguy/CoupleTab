package com.example.ethan.tabapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.database.sqlite.*;
import java.util.ArrayList;
import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity {
    private TabValue tabValue;
    private SharedPreferences sharedPref;
    // navigation drawer variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private ArrayList<TabEntry> entries;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize navigation drawer variables
        addDrawerItems(savedInstanceState);
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Access sharedPref for saving and loading values
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String currentTabValue_default = getResources().getString(R.string.currentTabValue_default);
        // load saved values into tabValue
        tabValue = new TabValue();
        String oldCurrentTabValue = sharedPref.getString(getString(R.string.currentTabValue),
                currentTabValue_default);
        tabValue.loadCurrentTabValue(oldCurrentTabValue);

        // initialize the fragment in position 0 of the navigation drawer, aka the calculator
        loadFragment(savedInstanceState, 0, true);
        // initialize sqlite database
        dbManager = new DBManager(this);

        dbManager.open();
        Log.d("myTag", "database initial insertion done");
    }

    @Override
    protected void onDestroy() {
        dbManager.close();
        super.onDestroy();
    }

    // helper function that loads fragments onto the main activity screen
    private void loadFragment(Bundle savedInstanceState, int position, boolean initialization){
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment newFragment;
            switch(position){
                default:
                case 0:
                    newFragment = new CalculatorFragment();
                    break;
                case 1:
                    newFragment = new HistoryFragment();
                    break;
            }
            newFragment.setArguments(getIntent().getExtras());
            if (initialization){
                transaction.add(R.id.fragment_container, newFragment);
            }
            else{
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    // helper function that sets up the navigation drawer and its items
    private void addDrawerItems(final Bundle savedInstanceState){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawyer_list_item,
                getResources().getStringArray(R.array.navigation_drawer_items)));
        mActivityTitle = getTitle().toString();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                loadFragment(savedInstanceState, position, false);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    // helper function that sets up the ActionBarDrawerToggle object
    private void setupDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close){
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // calls onPrepareOptionsMenu
            }

            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // calls onPrepareOptionsMenu
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    // makes sure the hamburger menu icon updates appropriately when the drawer opens/closes
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    // makes sure the hamburger menu icon updates appropriately when configuration changes
    // i.e. portrait to landscape mode
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
            //return true;
        //}

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    // TODO: edit this when you implement options menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    public TabValue GetTabValue(){
        return tabValue;
    }

    public DBManager GetDBManager(){ return dbManager; }

    public ArrayList<TabEntry> GetTabEntries(){
        return entries;
    }

    public void AddNewTabEntry(String dt, String c, String a, boolean p){
        entries.add(new TabEntry(dt, c, a, p));
    }

    public void SaveTabValue(){
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.currentTabValue), tabValue.currentTabString.get());
        editor.apply();
    }


}

