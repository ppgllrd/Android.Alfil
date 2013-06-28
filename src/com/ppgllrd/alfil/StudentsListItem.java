package com.ppgllrd.alfil;

import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * Created by pepeg on 26/06/13.
 */
public class StudentsListItem {
    private int photoId;
    private String name;
    private String surn1;
    private String surn2;

    public StudentsListItem(int photoId, String name, String surn1, String surn2) {
        this.photoId = photoId;
        this.name = name;
        this.surn1 = surn1;
        this.surn2 = surn2;
    }
    public int getPhotoId() {
        return photoId;
    }
    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
    public String getSurn1() {
        return surn1;
    }
    public void setSurn1(String surn1) {
        this.surn1 = surn1;
    }
    public String getSurn2() {
        return surn2;
    }
    public void setSurn2(String surn2) {
        this.surn2 = surn1;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name + "\n" + surn1;
    }




    public static List<StudentsListItem> readFromFile(LoadStudentsListViewTask clv, final List<StudentsListItem> students, String fileName) {
        //List<StudentsListItem> students = new ArrayList<StudentsListItem>();
        File fp = new File(fileName);
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
                //clv.publishProgress();
                i++;
            }
            sc.close();
        } catch (Exception e) {
            Log.d("com.ppgllrd.alfil", "Exception:", e);
        }
        return students;


    }

}
