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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence appTitle;
    private List<DrawerItem> drawerItems;
    int drawerSelectedIdx = -1;

    StudentsListFragment fragment = null;
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        if (savedInstanceState == null) {
            selectItem(1); // start by selecting first course
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        if(fragment!=null)
            fragment.setOnQueryTestListener(menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.search_box).setVisible(!drawerOpen);

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
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        boolean isSelectable = drawerItems.get(position) instanceof DrawerCourse;

        if(position != drawerSelectedIdx && drawerItems.get(position) instanceof DrawerCourse) {
            DrawerCourse drawerCourse = (DrawerCourse) drawerItems.get(position);
            drawerSelectedIdx = position;

            // update the main content by replacing fragments
            fragment = new StudentsListFragment();
            Bundle args = new Bundle();
            args.putInt(StudentsListFragment.ARG_GROUP_NUMBER, position);
            args.putString(StudentsListFragment.ARG_GROUP_STUDENTS_FILE, drawerCourse.getCourse().getStudentsFileName());
            args.putString(StudentsListFragment.ARG_GROUP_PHOTO_TEMPLATE, drawerCourse.getCourse().getPhotosTemplate());


            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

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

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public class StudentsListFragment extends ListFragment {
        private static final String ARG_GROUP_NUMBER = "group_number";
        private static final String ARG_GROUP_STUDENTS_FILE = "group_students_file";
        private static final String ARG_GROUP_PHOTO_TEMPLATE = "group_students_photo";

        private final List<StudentsListItem> listItems;

        StudentsListViewAdapter adapter;

        public StudentsListFragment() {
            listItems = new ArrayList<StudentsListItem>();
        }



        private boolean setFilter(String query) {
            adapter.getFilter().filter(StringUtils.removeAccents(query.toString().trim()));
            //Note that filter uses toString in StudentsListItem
            return true;
        }

        private boolean unsetFilter() {
            adapter.getFilter().filter("");
            //Note that filter uses toString in StudentsListItem
            return true;
        }

        public void setOnQueryTestListener(Menu menu) {
            SearchView searchView = (SearchView) menu.findItem(R.id.search_box).getActionView();
            if (searchView != null) {
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(true);
                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    public boolean onQueryTextChange(String newText) {
                        return setFilter(newText);
                    }
                    public boolean onQueryTextSubmit(String query) {
                        return setFilter(query);
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);

                if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    MenuItem menuItem = menu.findItem(R.id.search_box);
                    menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            // Return true to collapse action view
                            return unsetFilter();
                        }

                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            return true;
                        }
                    });
                } else {
                    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                        @Override
                        public boolean onClose() {
                            unsetFilter();
                            return false; //to clear and dismiss
                        }
                    });
                }
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Bundle bundle = this.getArguments();
            String fileName = bundle.getString(ARG_GROUP_STUDENTS_FILE);
            listItems.clear();
            adapter = new StudentsListViewAdapter(getActivity(), R.layout.students_list_item, listItems, bundle.getString(ARG_GROUP_PHOTO_TEMPLATE));
            setListAdapter(adapter);
            new LoadStudentsListViewTask(getActivity(), listItems, adapter, this, new File(fileName)).execute();
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // Do something with the data

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.students_list, null);
            return rootView;
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
//            filterText.removeTextChangedListener(filterTextWatcher);
        }

    }
}