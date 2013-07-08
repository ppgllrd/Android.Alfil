package com.ppgllrd.alfil;

import android.app.Activity;
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
import android.view.MenuInflater;
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
    public static final String ARG_GROUP_STUDENTS_COURSE = "group_students_course";
    public static final String FragmentTag = "StudentsListFragmentTag";

    private final List<Student> listItems;

    private StudentsListViewAdapter adapter;

    private Menu menu = null; // menu in actionBar

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
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
        setOnQueryTextListener(menu);
    }

    private boolean setFilter(String query) {
        Log.d("ppgllrd", "setFilter" + query);
        adapter.getFilter().filter(StringUtils.removeAccents(query.toString().trim()));
        //Note that filter uses toString in Student
        return true;
    }

    public void setOnQueryTextListener(Menu menu) {
        final SearchView searchView = (SearchView) menu.findItem(R.id.search_box).getActionView();
        Log.d("ppgllrd", "setOnQueryTextListener" + searchView + menu + getActivity());
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Student student = listItems.get(position);
        Student student = adapter.getItem(position);

                    /*
            int ident = student.getPhoto();
            for(Student st : listItems)
                    if(st.getPhoto() == ident) {
                        student = st;
                        break;
                    }*/


        mainActivity.studentInfoFragment.showStudent(student);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(mainActivity.studentsListFragment);
        ft.show(mainActivity.studentInfoFragment);
        ft.addToBackStack(StudentInfoFragment.FragmentTag);


        /*
                  transaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
         */




        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        /*

        Fragment fragment = new StudentInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(StudentInfoFragment.ARG_STUDENT, student);
        fragment.setArguments(args);

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, StudentInfoFragment.FragmentTag);
        ft.addToBackStack(StudentInfoFragment.FragmentTag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
*/

        // Do something with the data
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
