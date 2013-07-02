package com.ppgllrd.alfil;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pepeg on 2/07/13.
 */
public class StudentInfoFragment extends Fragment {

    public static final String ARG_STUDENT = "Student";

    public StudentInfoFragment() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        Student student = bundle.getParcelable(ARG_STUDENT);
        showStudent(student);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.student_info, null);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showStudent(Student student) {
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
    }
}
