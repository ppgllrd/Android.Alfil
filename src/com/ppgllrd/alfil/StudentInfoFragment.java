package com.ppgllrd.alfil;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pepeg on 2/07/13.
 */
public class StudentInfoFragment extends Fragment  {
    public static final String ARG_STUDENT = "Student";
    public static final String FragmentTag = "StudentInfoFragmentTag";

    private static float outOfScreenX = 800f;

    private MainActivity mainActivity;

    private Student currentStudent = null;

    public StudentInfoFragment() {
    }

    public boolean isShown() {
        return isVisible();// && getView().getX() < outOfScreenX;
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

    private void sendMailOnClick(int idFrame, final int idMailText) {
        View view = getActivity().findViewById(idFrame);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mailView = (TextView) view.findViewById(idMailText);
                Log.d("ppgllrd", "send mail" + mailView.getText());

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mailView.getText().toString()});
                //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                //i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.sendMail)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mainActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void DoPhonecall(int idFrame, final int idPhoneNumberText) {
        View view = getActivity().findViewById(idFrame);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView phoneNumberView = (TextView) view.findViewById(idPhoneNumberText);
                Log.d("ppgllrd", "phone call" + phoneNumberView.getText());

                String uri = "tel:" + phoneNumberView.getText().toString().trim();
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(uri));

                try {
                    startActivity(i);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mainActivity, "There is no phone available on this device.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        Student student = bundle.getParcelable(ARG_STUDENT);
        showStudent(student);
        setHasOptionsMenu(true); // to grab actionBar's menu in onCreateOptionsMenu

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.info_layout);
        layout.setOnTouchListener(new TouchListener());

        sendMailOnClick(R.id.info_mail1_frame, R.id.info_mail1);
        sendMailOnClick(R.id.info_mail2_frame, R.id.info_mail2);
        DoPhonecall(R.id.info_phone_frame, R.id.info_phone);
        DoPhonecall(R.id.info_mobile_frame, R.id.info_mobile);
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
        view.bringToFront();
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

    private class TouchListener implements View.OnTouchListener {
        private float xDelta;
        private static final int MIN_DISTANCE = 200;
        private static final float initialAlpha = 0.0f;


        private void updateAlpha(View view) {
            mainActivity.studentsListFragment.getView().setAlpha(initialAlpha+(1f-initialAlpha)*view.getX()/(3*MIN_DISTANCE));
        }


        private class XAnimator {
            private ObjectAnimator transAnimation;
            public XAnimator(final View view, float initialX, float finalX, int duration) {
                transAnimation = ObjectAnimator.ofFloat(view, "x", initialX, finalX);
                transAnimation.setDuration(duration);
                transAnimation.setInterpolator(new LinearInterpolator());

                transAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                transAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        updateAlpha(view);
                    }
                });
            }
            public void onEnd() {}

            public void start() {
                transAnimation.start();
            }
        }


        private void cancel(final View view) {
            XAnimator xAnimator = new XAnimator(view, view.getX(), 0, 250) {
              @Override
              public void onEnd() {
                  FragmentTransaction ft = getFragmentManager().beginTransaction();
                  ft.hide(mainActivity.studentsListFragment);
                  ft.commit();
                  mainActivity.studentsListFragment.getView().setAlpha(1.0f);
              }
            };
            xAnimator.start();
        }

        private VelocityTracker mVelocityTracker = null;

        @Override
        public boolean onTouch(final View view, MotionEvent event) {
            Log.d("ppgllrd", "onTouch" + view);
            Log.d("ppgllrd", "onTouch" + mainActivity.studentInfoFragment.isVisible()+" "+mainActivity.studentsListFragment.isVisible());


            final float rawX = event.getRawX();
            final float rawY = event.getRawY();

            int index = event.getActionIndex();
            int action = event.getActionMasked();
            int pointerId = event.getPointerId(index);

            Log.d("ppgllrd", "onTouch" + view + " " + rawX + " " + rawY);
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    xDelta = rawX - view.getX();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.show(mainActivity.studentsListFragment);
                    ft.commit();

                    updateAlpha(view);

                    if(mVelocityTracker == null) {
                        // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    else {
                        // Reset the velocity tracker back to its initial state.
                        mVelocityTracker.clear();
                    }
                    // Add a user's movement to the tracker.
                    mVelocityTracker.addMovement(event);
                    return true;
                }

                case MotionEvent.ACTION_MOVE: {
                    mVelocityTracker.addMovement(event);
                    float offX = rawX - xDelta;
                    if (offX > 0) {
                        view.setX(offX);
                        updateAlpha(view);
                        view.invalidate();
                    }
                    return true;
                }

                case MotionEvent.ACTION_UP: {
                    mVelocityTracker.computeCurrentVelocity(1000);

                    float velX = VelocityTrackerCompat.getXVelocity(mVelocityTracker,pointerId);
                    Log.d("ppgllrd", "velX "+velX);

                    float offX = rawX - xDelta;
//                    if (velX > 500 || offX > MIN_DISTANCE) {
                    if (velX > 300) { //more than 200px per second
                        XAnimator xAnimator = new XAnimator(view, view.getX(), outOfScreenX, 500) {
                            @Override
                            public void onEnd() {
                                FragmentManager fm = getActivity().getFragmentManager();
                                if (fm.getBackStackEntryCount() > 0) {
                                    fm.popBackStack();
                                }
                                mainActivity.studentsListFragment.getView().setAlpha(1.0f);
                            }
                        };
                        xAnimator.start();
                    } else {
                        cancel(view);
                    }
                    mVelocityTracker.recycle();
                    return true;
                }

                case MotionEvent.ACTION_CANCEL:
                    mVelocityTracker.recycle();
                    cancel(view);
                    return true;
            }
            return false;
        }
    }
}
