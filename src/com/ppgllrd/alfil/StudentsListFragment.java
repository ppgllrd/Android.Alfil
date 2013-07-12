package com.ppgllrd.alfil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class StudentsListFragment extends ListFragment {
    public static final String ARG_GROUP_STUDENTS_COURSE = "group_students_course";
    public static final String FragmentTag = "StudentsListFragmentTag";

    private final List<Student> listItems;

    private StudentsListViewAdapter adapter;

    private MainActivity mainActivity;


    public StudentsListFragment() {
        listItems = new ArrayList<Student>();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        mainActivity = (MainActivity) a;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.students_list, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("ppgllrd", "SLF onActivityCreated:" + savedInstanceState);
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        Course course = bundle.getParcelable(ARG_GROUP_STUDENTS_COURSE);

        setHasOptionsMenu(true); // to grab actionBar's menu in onCreateOptionsMenu
        loadCourse(course);
    }

    private void loadCourse(Course course) {
        listItems.clear();
        adapter = new StudentsListViewAdapter(getActivity(), R.layout.students_list_item, listItems, course);
        setListAdapter(adapter);
        new LoadStudentsListViewTask(getActivity(), course, listItems, adapter, null).execute();
        getActivity().invalidateOptionsMenu();
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // studentInfoFragment has priority
        if(mainActivity.studentInfoFragment.isShown())
            return;

        Log.d("ppgllrd", "onCreateOptionsMenuSLF" + menu+ " " + query);
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        Bundle bundle = getArguments();
        Course course = bundle.getParcelable(ARG_GROUP_STUDENTS_COURSE);
        mainActivity.getActionBar().setTitle(course.getName());

        mainActivity.getActionBarTitleController().setDrawerIndicatorEnabled(true); // enable drawer indicator

        setOnQueryTextListener(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("ppgllrd", "onOptionsItemSelectedSLW  "+item);
        //mainActivity.showCurrentCourse();
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("ppgllrd", "onOptionsItemSelectedSLVhome  "+item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private String query = "";

    private boolean setFilter(String query) {
        Log.d("ppgllrd", "setFilterSLF" + query);
        this.query = query;
        if(adapter != null) {
        Filter currentFilter = adapter.getFilter();
        if(currentFilter != null)
            currentFilter.filter(StringUtils.removeAccents(query.toString().trim()));
        //Note that filter uses toString in Student
        }
        return true;
    }

    public void setOnQueryTextListener(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.search_box);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        Log.d("ppgllrd", "setOnQueryTextListenerSLF" + searchView.getQuery().toString() + menu + getActivity());
        if (searchView != null) {
            Log.d("ppgllrd", "setOnQueryTextListenerSLF2" + searchView.getQuery());

            //searchView.setQuery(query, false);
            if(!query.isEmpty())
                menuItem.expandActionView();
            searchView.setQuery(query, false);

            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setIconifiedByDefault(true);
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String query) {
                    Log.d("ppgllrd", "onQueryTextChange" + query);
                    return setFilter(query);
                }

                public boolean onQueryTextSubmit(String query) {
                    Log.d("ppgllrd", "onQueryTextSubmit" + query);
                    return setFilter(query);
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);


            int currentApiVersion = Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        Log.d("ppgllrd", "onMenuItemActionCollapse");
                        searchView.setQuery(null, true);
                        setFilter("");
                        // Return true to collapse action view
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        Log.d("ppgllrd", "onMenuItemActionExpand "+query);
                        //searchView.setQuery(query, false);
                        searchView.setQueryHint("type to search");
                        return true;
                    }
                };
                if (onActionExpandListener != null)
                    menuItem.setOnActionExpandListener(onActionExpandListener);
            } else {
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        //searchView.setQuery("", true);
                        return false; //to clear and dismiss
                    }
                });
            }
           // searchView.setQuery(query, true);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("ppgllrd", "setUserVisibleHint> "+hidden);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Student student = adapter.getItem(position);
        mainActivity.studentInfoFragment.showStudent(student);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_right,
                R.anim.none, R.anim.none,
                R.anim.slide_out_right);

        ft.show(mainActivity.studentInfoFragment);
        ft.hide(mainActivity.studentsListFragment);
        ft.addToBackStack(StudentInfoFragment.FragmentTag);

        ft.commit();
        getFragmentManager().executePendingTransactions();

    }

    public Student getStudent(int position) {
        return adapter.getItem(position);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//            filterText.removeTextChangedListener(filterTextWatcher);
    }
}
