package com.ppgllrd.alfil;

//import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * Created by pepeg on 28/06/13.
 */
public class LoadStudentsListViewTask extends AsyncTask<Void, Student, Void> {
    Context context;
    List<Student> listItems;
    StudentsListViewAdapter adapter;
    StudentsListFragment listFragment;
    Course course;
    // ProgressDialog pDialog;

    public LoadStudentsListViewTask(Context context, Course course, List<Student> listItems, StudentsListViewAdapter adapter, StudentsListFragment listFragment){
        this.course = course;
        this.context = context;
        this.listItems = listItems;
        this.adapter = adapter;
        this.listFragment = listFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
 //   pDialog = new ProgressDialog(context);
     //   pDialog.setMessage("Cargando Lista");
     //   pDialog.setCancelable(true);
     //   pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
     // pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        //Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        int i = 1;
        try {
            Scanner sc = new Scanner(new File(course.getStudentsFileName()));
            while(sc.hasNext()) {
                String surn1 = StringUtils.uppercase(sc.nextLine());
                String surn2 = StringUtils.uppercase(sc.nextLine());
                String name = StringUtils.uppercase(sc.nextLine());
                String phone = sc.nextLine();
                String mobile = sc.nextLine();
                String birthDate = sc.nextLine();
                String mail1 = sc.nextLine();
                String mail2 = sc.nextLine();
                String photo = sc.nextLine();
                String dni = sc.nextLine();
                String expedient = sc.nextLine();
                int enrollments = Integer.parseInt(sc.nextLine());
                int failures = Integer.parseInt(sc.nextLine());
                //students.add(new Student(i, name, surn1, surn2));
                publishProgress(new Student(course, name, surn1, surn2, phone, mobile, birthDate, mail1, mail2, photo, dni, expedient, enrollments, failures));
                i++;
            }
            sc.close();
        } catch (Exception e) {
            Log.d(LoadStudentsListViewTask.class.getName(), "Exception:", e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Student... items) {
        listItems.add(items[0]);
        if(listItems.size() % 20 == 0)
          adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        adapter.notifyDataSetChanged();
       // pDialog.dismiss();
    }
}