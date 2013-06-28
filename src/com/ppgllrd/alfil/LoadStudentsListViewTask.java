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
public class LoadStudentsListViewTask extends AsyncTask<Void, StudentsListItem, Void> {
    Context context;
//    ProgressDialog pDialog;
    MainActivity.StudentsListFragment lf;
    StudentsListViewAdapter adapter;
    List<StudentsListItem> listItems;


    public LoadStudentsListViewTask(Context context, List<StudentsListItem> listItems, StudentsListViewAdapter adapter, MainActivity.StudentsListFragment lf){
        this.context = context;
        this.lf = lf;
        this.listItems = listItems;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

     //   pDialog = new ProgressDialog(context);
     //   pDialog.setMessage("Cargando Lista");
     //   pDialog.setCancelable(true);
     //   pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       // pDialog.show();


;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // TODO Auto-generated method stub

        //Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        File fp = new File("mnt/sdcard/alfil/mates/alumnos.txt");
        int i = 1;
        try {
            Scanner sc = new Scanner(fp);
            while(sc.hasNext()) {
                String surn1 = StringUtils.uppercase(sc.nextLine());
                String surn2 = StringUtils.uppercase(sc.nextLine());
                String name = StringUtils.uppercase(sc.nextLine());
                String phone1 = sc.nextLine();
                String phone2 = sc.nextLine();
                String birthDate = sc.nextLine();
                String mail1 = sc.nextLine();
                String mail2 = sc.nextLine();
                //students.add(new StudentsListItem(i, name, surn1, surn2));
                publishProgress(new StudentsListItem(i, name, surn1, surn2));
                i++;
            }
            sc.close();
        } catch (Exception e) {
            Log.d("com.ppgllrd.alfil", "Exception:", e);
        }

        return null;

    }


    private int addedItems = 0;
    @Override
    protected void onProgressUpdate(StudentsListItem... items) {
        listItems.add(items[0]);
        addedItems++;
        if(addedItems %20==0)
          adapter.notifyDataSetChanged();
    }


    @Override
    protected void onPostExecute(Void v) {
        // TODO Auto-generated method stub
        super.onPostExecute(v);
        adapter.notifyDataSetChanged();

       // pDialog.dismiss();
    }

}