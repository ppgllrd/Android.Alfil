package com.ppgllrd.alfil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pepeg on 2/07/13.
 */
public class StudentInfoFragment extends Fragment {
    public static final String ARG_STUDENT = "Student";
    public static final String FragmentTag = "StudentInfoFragmentTag";

    private Menu menu = null; // menu in actionBar

    private MainActivity mainActivity;

    public StudentInfoFragment() {
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        mainActivity = (MainActivity) a;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.student_info, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        Student student = bundle.getParcelable(ARG_STUDENT);
        showStudent(student);
        setHasOptionsMenu(true); // to grab actionBar's menu in onCreateOptionsMenu
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("ppgllrd", "onCreateOptionsMenu"+menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        Log.d("ppgllrd", "onResume");
        // fragment is being shown
        super.onResume();
        //menu.findItem(R.id.search_box).setVisible(false);
    }

    @Override
    public void onPause() {
        Log.d("ppgllrd", "onPause");
        // fragment is becoming non-visible
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("ppgllrd", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("ppgllrd", "setUserVisibleHint "+hidden);
        if (!hidden) {
            getActivity().getActionBar().setDisplayShowHomeEnabled(false);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            mainActivity.studentInfoFragmentShown = true;
        }
        else {
            getActivity().getActionBar().setDisplayShowHomeEnabled(true);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            mainActivity.studentInfoFragmentShown = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("ppgllrd", "onOptionsItemSelected:<   "+item);
        mainActivity.showCurrentCourse();
        return true; //return super.onOptionsItemSelected(item);
    }

    public void showStudent(Student student) {
        View view = getView();
        ImageView photo = (ImageView) view.findViewById(R.id.info_photo);
        String path = student.getPhotoPath();
        photo.setImageDrawable(Drawable.createFromPath(path));

        TextView surname = (TextView) view.findViewById(R.id.info_surname);
        surname.setText(student.getSurname1()+" "+student.getSurname2());

        TextView name = (TextView) view.findViewById(R.id.info_name);
        name.setText(student.getName());

        TextView phone = (TextView) view.findViewById(R.id.info_phone);
        phone.setText(student.getPhone());

        TextView mobile = (TextView) view.findViewById(R.id.info_mobile);
        mobile.setText(student.getMobile());

        TextView mail1 = (TextView) view.findViewById(R.id.info_mail1);
        mail1.setText(student.getMail1());

        TextView mail2 = (TextView) view.findViewById(R.id.info_mail2);
        mail2.setText(student.getMail2());

        TextView birthdate = (TextView) view.findViewById(R.id.info_birthdate);
        birthdate.setText(student.getBirthdate());

        // show name on navigation bar
        getActivity().getActionBar().setTitle(student.getName());
    }
}
