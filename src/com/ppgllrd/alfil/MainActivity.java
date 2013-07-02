/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ppgllrd.alfil;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence appTitle;
    private List<DrawerItem> drawerItems;
    private int drawerSelectedIdx = -1;

    private StudentsListFragment studentsListFragment = null;
    private StudentInfoFragment studentInfoFragment = null;

    private static final String StudentsListFragmentTag = "StudentsListFragmentTag";
    private static final String StudentInfoFragmentTag = "StudentInfoFragmentTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ppgllrd", "ONCRE"+savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfil);

        appTitle = drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerItems = new ArrayList<DrawerItem>();
        drawerItems.add(new DrawerSection("Asignaturas"));
        for(Course course : Course.getCourses())
            drawerItems.add(new DrawerCourse(course));

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        drawerList.setAdapter(new DrawerAdapter(this,
                R.layout.drawer_course_item, drawerItems));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(appTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        //if (savedInstanceState == null) {
        selectItem(1,savedInstanceState); // start by selecting first course
        //}

        if (savedInstanceState != null)
            studentInfoFragment = (StudentInfoFragment) getFragmentManager().findFragmentByTag(StudentInfoFragmentTag);
        else
            studentInfoFragment = new StudentInfoFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("ppgllrd","ONCREMN");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.search_box).setVisible(!drawerOpen);
        if(studentsListFragment !=null && !drawerOpen)
            studentsListFragment.setOnQueryTextListener(menu);

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("ppgllrd","ONPREMN");
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
        /*
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
        */
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position, null);
        }
    }



    private void selectItem(int position, Bundle savedInstanceState) {
        Log.d("ppgllrd", "selectItem"+position);
        boolean isSelectable = drawerItems.get(position) instanceof DrawerCourse;

        if(savedInstanceState != null || (position != drawerSelectedIdx && drawerItems.get(position) instanceof DrawerCourse)) {
            DrawerCourse drawerCourse = (DrawerCourse) drawerItems.get(position);
            drawerSelectedIdx = position;


            // update the main content by replacing fragments
            if (savedInstanceState != null) {
                studentsListFragment = (StudentsListFragment) getFragmentManager().findFragmentByTag(StudentsListFragmentTag);
               // studentsListFragment.setStudents(drawerCourse.getCourse().getStudentsFileName(), drawerCourse.getCourse().getPhotosTemplate());
            } else {
                studentsListFragment = new StudentsListFragment();
                Bundle args = new Bundle();
                args.putInt(StudentsListFragment.ARG_GROUP_NUMBER, position);
                args.putParcelable(StudentsListFragment.ARG_GROUP_STUDENTS_COURSE, drawerCourse.getCourse());
                studentsListFragment.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, studentsListFragment, StudentsListFragmentTag).commit();
            }

            // update selected item and appTitle, then close the drawer
            //drawerList.setItemChecked(position, true);
            setTitle(drawerCourse.getCourse().getName());
        }
        if(isSelectable)
            drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.appTitle = title;
        getActionBar().setTitle(this.appTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}