package com.ppgllrd.alfil;

import android.app.ActionBar;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by pepeg on 2/07/13.
 */
public class StudentInfoFragment extends Fragment implements View.OnTouchListener {
    public static final String ARG_STUDENT = "Student";
    public static final String FragmentTag = "StudentInfoFragmentTag";

    private MainActivity mainActivity;

    private Student currentStudent = null;

    public StudentInfoFragment() {
    }

    private static final int MIN_DISTANCE = 75;
    private float downX, downY, upX, upY;
    private boolean fired = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("ppgllrd", "onTouch"+v);
        if(this.isVisible()){
        Log.d("ppgllrd", "onTouch"+v);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                fired = false;
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                float deltaX = downX - upX;
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    //if(deltaX > 0) { this.onRighToLefttSwipe(); return true; }
                    if(deltaX < 0) {
                        fired = true;
                        FragmentManager fm = getActivity().getFragmentManager();
                        if(fm.getBackStackEntryCount()>0){
                            fm.popBackStack();
                        }
                        return true;
                    }
                }
                else {
                    //Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    return false; // We don't consume the event
                }

            }
            /*
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    //if(deltaX > 0) { this.onRighToLefttSwipe(); return true; }
                    if(deltaX < 0) {
                        FragmentManager fm = getActivity().getFragmentManager();
                        if(fm.getBackStackEntryCount()>0){
                            fm.popBackStack();
                        }
                        return true;
                    }
                }
                else {
                    //Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    return false; // We don't consume the event
                }


                return true;
            }
*/
        }
        }
        return false;
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
        rootView.bringToFront();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        Student student = bundle.getParcelable(ARG_STUDENT);
        showStudent(student);
        setHasOptionsMenu(true); // to grab actionBar's menu in onCreateOptionsMenu

        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.info_Fragment_Placeholder);
        frameLayout.setOnTouchListener(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("ppgllrd", "onCreateOptionsMenuSIF" + menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        if(currentStudent != null)
            mainActivity.getActionBar().setTitle(currentStudent.getName()+" "+currentStudent.getSurname1()+" "+currentStudent.getSurname2());
        mainActivity.getActionBarTitleController().setDrawerIndicatorEnabled(false); //show back arrow <
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("ppgllrd", "onOptionsItemSelectedSIF  "+item);
        //mainActivity.showCurrentCourse();
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("ppgllrd", "onOptionsItemSelectedSIFhome  "+item);
                // < menu in action bar clicked;
                FragmentManager fm = getActivity().getFragmentManager();
                if(fm.getBackStackEntryCount()>0){
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showStudent(Student student) {

        currentStudent = student;

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

        TextView dni = (TextView) view.findViewById(R.id.info_dni);
        dni.setText(student.getDNI());

        TextView enrollments = (TextView) view.findViewById(R.id.info_enrollments);
        enrollments.setText(Integer.toString(student.getEnrollments()));

        TextView failures = (TextView) view.findViewById(R.id.info_failures);
        failures.setText(Integer.toString(student.getFailures()));

        ScrollView scrollView = (ScrollView) view.findViewById(R.id.info_scrollView);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
}
