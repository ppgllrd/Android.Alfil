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

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ActionBarTitleController actionBarTitleController;

    private List<DrawerItem> drawerItems;
    private int drawerSelectedIdx = -1;

    public StudentsListFragment studentsListFragment = null;
    public StudentInfoFragment studentInfoFragment = null;


    private Menu menu = null; // menu in actionBar

    public boolean studentInfoFragmentShown = false;

    class ActionBarTitleController extends ActionBarDrawerToggle {
        private Activity activity;
        private DrawerLayout drawerLayout;
        private boolean svSearchBoxVisible = false;
        private int svDisplayOptions = 0;
        private CharSequence svTitle = "";
        private String appTitle;

        public ActionBarTitleController(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
            this.activity = activity;
            this.drawerLayout = drawerLayout;
            this.appTitle = activity.getTitle().toString();
            // set a custom shadow that overlays the main content when the drawer opens
            drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            drawerLayout.setDrawerListener(this);
        }

        public void setTitle(String title) {
            activity.getActionBar().setTitle(title);
            svTitle = title;
        }

        public void closeDrawer() {
            drawerLayout.closeDrawer(Gravity.LEFT); //(drawerList);
        }

        public void onDrawerOpened(View drawerView) {
            svTitle = activity.getActionBar().getTitle();
            activity.getActionBar().setTitle(appTitle);

            svDisplayOptions = activity.getActionBar().getDisplayOptions();
            activity.getActionBar().setDisplayShowHomeEnabled(true);
            MenuItem search = menu.findItem(R.id.search_box);

            if(search != null) {
                svSearchBoxVisible = search.isVisible();
                search.setVisible(false);
            }
        }

        public void onDrawerClosed(View view) {
            Log.d("ppgllrd","onDrawerClosed");
            getActionBar().setTitle(svTitle);
            MenuItem search = menu.findItem(R.id.search_box);
            if(search != null)
                search.setVisible(svSearchBoxVisible);
            getActionBar().setDisplayOptions(svDisplayOptions);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ppgllrd", "onCreate: "+savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_double);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.drawer_list);

        drawerItems = new ArrayList<DrawerItem>();
        drawerItems.add(new DrawerSection(getResources().getString(R.string.Courses)));
        for(Course course : Course.getCourses())
            drawerItems.add(new DrawerCourse(course));


        // set up the drawer's list view with items and click listener
        drawerList.setAdapter(new DrawerAdapter(this,
                R.layout.drawer_course_item, drawerItems));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        /*
        actionBarTitleController = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            private boolean svSearchBoxVisible = false;
            private CharSequence svTitle = "";

            public void setTitle(String title) {
                getActionBar().setTitle(title);
                svTitle = title;
            }

            public void onDrawerOpened(View drawerView) {
                svTitle = getActionBar().getTitle();
                getActionBar().setTitle(drawerTitle);
                MenuItem search = menu.findItem(R.id.search_box);

                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                if(search != null) {
                    svSearchBoxVisible = search.isVisible();
                    search.setVisible(false);
                }
            }

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(svTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                MenuItem search = menu.findItem(R.id.search_box);
                if(search != null)
                    search.setVisible(svSearchBoxVisible);
            }

        };
        */




        actionBarTitleController = new ActionBarTitleController(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );
        //actionBarTitleController.setTitle("Alfil");
        //drawerLayout.setDrawerListener(actionBarTitleController);

//        selectCourse(1, savedInstanceState); // start by selecting first course


        FragmentManager fragMgr = getFragmentManager();
        FragmentTransaction xact = fragMgr.beginTransaction();

        drawerSelectedIdx = 1; // start by selecting first course
        if(fragMgr.findFragmentByTag(StudentsListFragment.FragmentTag)==null) {
            DrawerCourse drawerCourse = (DrawerCourse) drawerItems.get(drawerSelectedIdx);
            studentsListFragment = new StudentsListFragment();
            Bundle args = new Bundle();
            args.putParcelable(StudentsListFragment.ARG_GROUP_STUDENTS_COURSE, drawerCourse.getCourse());
            studentsListFragment.setArguments(args);

            xact.add(R.id.list_Fragment_Placeholder, studentsListFragment, StudentsListFragment.FragmentTag).
                        commit();
        } else {
            studentsListFragment = (StudentsListFragment) getFragmentManager().findFragmentByTag(StudentsListFragment.FragmentTag);
        }

        xact = fragMgr.beginTransaction();
        if(fragMgr.findFragmentByTag(StudentInfoFragment.FragmentTag)==null) {
            studentInfoFragment = new StudentInfoFragment();
            Bundle args = new Bundle();
            args.putParcelable(StudentInfoFragment.ARG_STUDENT, new Student(((DrawerCourse) drawerItems.get(drawerSelectedIdx)).getCourse()));
            studentInfoFragment.setArguments(args);

            xact.add(R.id.info_Fragment_Placeholder, studentInfoFragment, StudentInfoFragment.FragmentTag).
                    commit();
        } else {
            studentInfoFragment = (StudentInfoFragment) getFragmentManager().findFragmentByTag(StudentInfoFragment.FragmentTag);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(studentInfoFragment);
        ft.show(studentsListFragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("ppgllrd","onCreateOptionsMenu");
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        Log.d("ppgllrd", "onOptionsItemSelected: "+item);

        if(studentInfoFragmentShown)
            return false;

        if (actionBarTitleController.onOptionsItemSelected(item)) {
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
            Log.d("ppgllrd","DrawerItemClickListener");
            selectCourse(position);
        }
    }


    private void selectCourse(int position) {
        Log.d("ppgllrd", "selectCourse"+position+" "+drawerSelectedIdx);
        boolean isSelectable = drawerItems.get(position) instanceof DrawerCourse;

        if(isSelectable) {
            final DrawerCourse drawerCourse = (DrawerCourse) drawerItems.get(position);
            FragmentManager fragmentManager = getFragmentManager();
            Log.d("ppgllrd", "Count:"+fragmentManager.getBackStackEntryCount());


            if (position != drawerSelectedIdx) {
                Log.d("ppgllrd", "YES");
                drawerSelectedIdx = position;

                studentsListFragment = new StudentsListFragment();
                Bundle args = new Bundle();
                args.putParcelable(StudentsListFragment.ARG_GROUP_STUDENTS_COURSE, drawerCourse.getCourse());
                studentsListFragment.setArguments(args);


                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.list_Fragment_Placeholder, studentsListFragment, StudentsListFragment.FragmentTag);
                ft.addToBackStack(StudentInfoFragment.FragmentTag);
                ft.commit();


        /*
                  transaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
         */




                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);



      /*

                if(fragmentManager.getBackStackEntryCount()==0)
                    fragmentManager.beginTransaction().
                            add(R.id.content_frame, studentsListFragment, StudentsListFragment.FragmentTag).
                            addToBackStack(StudentsListFragment.FragmentTag).
                                    commit();
                else
                fragmentManager.beginTransaction().
                        replace(R.id.content_frame, studentsListFragment, StudentsListFragment.FragmentTag).
                        // addToBackStack(StudentsListFragment.FragmentTag).
                        commit();
    */
            }
            Log.d("ppgllrd", "YYY");

            actionBarTitleController.closeDrawer();
            actionBarTitleController.setTitle(drawerCourse.getCourse().getName());

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(studentInfoFragment);
            ft.show(studentsListFragment);
            ft.addToBackStack(StudentInfoFragment.FragmentTag);
            ft.commit();



        }
        Log.d("ppgllrd", "ZZZ");
    }

    public void showCurrentCourse() {
        selectCourse(drawerSelectedIdx);
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarTitleController.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarTitleController.onConfigurationChanged(newConfig);
    }
}