package com.ppgllrd.alfil;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Created by pepeg on 29/06/13.
 */
public class Course implements Parcelable {
    public static final String sdCardAlfil = "/mnt/sdcard/alfil";

    private static final String infoFileName = "info.txt";

    private static final String dataFile = "students.txt";

    private static final String imgFolder = "images";

    private String folder;
    private String name;
    private String year;
    private String description;

    public Course(String folder) {
        this.folder = folder;

        try {
            File fp = new File(sdCardAlfil+"/"+folder+"/"+infoFileName);
            Scanner sc = new Scanner(fp);
            this.name = sc.nextLine();
            this.year = sc.nextLine();
            this.description = sc.nextLine();
            sc.close();
        } catch (Exception e) {
            Log.d(LoadStudentsListViewTask.class.getName(), "Exception:", e);
        }
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(folder);
        parcel.writeString(name);
        parcel.writeString(year);
        parcel.writeString(description);
    }

    public int describeContents() {
        return 0;
    }

    private Course(Parcel parcel) {
        this.folder = parcel.readString();
        this.name = parcel.readString();
        this.year = parcel.readString();
        this.description = parcel.readString();
    }

    public static final Parcelable.Creator<Course> CREATOR
            = new Parcelable.Creator<Course>() {
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getFolder() {
        return folder;
    }

    public String getStudentsFileName() {
        return sdCardAlfil+"/"+folder+"/"+dataFile;
    }

    public String getPhotoFullPath(String photo) {
        return sdCardAlfil+"/"+folder+"/"+imgFolder+"/"+photo;
    }

    public static Course[] getCourses() {
        File sdCardAlfilFolder = new File(sdCardAlfil);
        File[] listOfFiles = sdCardAlfilFolder.listFiles();

        int directories = 0;
        for(File fp : listOfFiles)
            if(fp.isDirectory())
                directories++;

        Course courses[] = new Course[directories];
        int j = 0;
        for(int i=0; i<directories; i++)
            if(listOfFiles[i].isDirectory())
                courses[j++] = new Course(listOfFiles[i].getName());

        // sort by names
        Arrays.sort(courses, new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                return course1.name.compareTo(course2.name);
            }
        });

        return courses;
    }
}
