package com.ppgllrd.alfil;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class StudentsListFragment extends ListFragment {
        public static final String ARG_GROUP_NUMBER = "group_number";
        public static final String ARG_GROUP_STUDENTS_COURSE = "group_students_course";
        public static final String FragmentTag = "FragmentTag";


        private final List<Student> listItems;

        StudentsListViewAdapter adapter;

        public StudentsListFragment() {
            listItems = new ArrayList<Student>();
        }

        private boolean setFilter(String query) {
            Log.d("ppgllrd", "setFilter" + query);
            adapter.getFilter().filter(StringUtils.removeAccents(query.toString().trim()));
            //Note that filter uses toString in Student
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
                            Log.d("ppgllrd", "onMenuItemActionCollapse");
                            searchView.setQuery(null, true);
                            // Return true to collapse action view
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            Log.d("ppgllrd", "onMenuItemActionExpand");
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
            Course course = bundle.getParcelable(ARG_GROUP_STUDENTS_COURSE);
            listItems.clear();
            adapter = new StudentsListViewAdapter(getActivity(), R.layout.students_list_item, listItems, course);
            setListAdapter(adapter);
            getActivity().invalidateOptionsMenu();
            new LoadStudentsListViewTask(getActivity(), course, listItems, adapter, null).execute();

        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            //Student student = listItems.get(position);
            Student student = adapter.getItem(position);

                    /*
            int ident = student.getIdentity();
            for(Student st : listItems)
                    if(st.getIdentity() == ident) {
                        student = st;
                        break;
                    }*/

            Fragment fragment = new StudentInfoFragment();
            Bundle args = new Bundle();
            args.putParcelable(StudentInfoFragment.ARG_STUDENT, student);
            fragment.setArguments(args);

            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, FragmentTag).addToBackStack(FragmentTag);;
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();




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
