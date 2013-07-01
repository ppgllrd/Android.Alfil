package com.ppgllrd.alfil;

import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class StudentsListFragment extends ListFragment {
        public static final String ARG_GROUP_NUMBER = "group_number";
        public static final String ARG_GROUP_STUDENTS_FILE = "group_students_file";
        public static final String ARG_GROUP_PHOTO_TEMPLATE = "group_students_photo";

        private final List<StudentsListItem> listItems;

        StudentsListViewAdapter adapter;

        public StudentsListFragment() {
            listItems = new ArrayList<StudentsListItem>();
        }

        private boolean setFilter(String query) {
            Log.d("ppgllrd", "setFilter" + query);
            adapter.getFilter().filter(StringUtils.removeAccents(query.toString().trim()));
            //Note that filter uses toString in StudentsListItem
            return true;
        }

        public void setOnQueryTextListener(Menu menu) {
            final SearchView searchView = (SearchView) menu.findItem(R.id.search_box).getActionView();
            Log.d("ppgllrd", "setOnQueryTextListener" + searchView + menu+ getActivity());
            if (searchView != null) {
                searchView.setQuery(null, true);
                Log.d("ppgllrd", "setOnQueryTextListener" + searchView.getQuery());

                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setIconifiedByDefault(true);
                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    public boolean onQueryTextChange(String newText) {
                        Log.d("ppgllrd", "onQueryTextChange" + newText);
                        return setFilter(newText);
                    }

                    public boolean onQueryTextSubmit(String query) {
                        Log.d("ppgllrd", "onQueryTextSubmit" + query);
                        return setFilter(query);
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);


                int currentApiVersion = Build.VERSION.SDK_INT;
                if (currentApiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    MenuItem menuItem = menu.findItem(R.id.search_box);
                    MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            searchView.setQuery(null, true);
                            // Return true to collapse action view
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            return true;
                        }
                    };
                    if (onActionExpandListener != null)
                        menuItem.setOnActionExpandListener(onActionExpandListener);
                } else {
                    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                        @Override
                        public boolean onClose() {
                            searchView.setQuery("", true);
                            return false; //to clear and dismiss
                        }
                    });
                }
            }
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Bundle bundle = getArguments();
            String fileName = bundle.getString(ARG_GROUP_STUDENTS_FILE);
            listItems.clear();
            adapter = new StudentsListViewAdapter(getActivity(), R.layout.students_list_item, listItems, bundle.getString(ARG_GROUP_PHOTO_TEMPLATE));
            setListAdapter(adapter);
            getActivity().invalidateOptionsMenu();
            new LoadStudentsListViewTask(getActivity(), listItems, adapter, null, new File(fileName)).execute();

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
