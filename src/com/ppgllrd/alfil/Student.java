package com.ppgllrd.alfil;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * Created by pepeg on 26/06/13.
 */
public class Student implements Parcelable {
    private Course course;
    private int photoId;
    private String name;
    private String surn1;
    private String surn2;
    private String phone;
    private String mobile;
    private String birthDate;
    private String mail1;
    private String mail2;


    public Student(Course course, int photoId, String name, String surn1, String surn2, String phone, String mobile, String birthDate, String mail1, String mail2) {
        this.course = course;
        this.photoId = photoId;
        this.name = name;
        this.surn1 = surn1;
        this.surn2 = surn2;
        this.phone = phone;
        this.mobile = mobile;
        this.birthDate = birthDate;
        this.mail1 = mail1;
        this.mail2 = mail2;
    }

    public Student(Course course) {
        this(course,0,"","","","","","","","");
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(course, flags);
        parcel.writeInt(photoId);
        parcel.writeString(name);
        parcel.writeString(surn1);
        parcel.writeString(surn2);
        parcel.writeString(phone);
        parcel.writeString(mobile);
        parcel.writeString(birthDate);
        parcel.writeString(mail1);
        parcel.writeString(mail2);
    }

    public int describeContents() {
        return 0;
    }

    private Student(Parcel parcel) {
        this.course = parcel.readParcelable(Course.class.getClassLoader());
        this.photoId = parcel.readInt();
        this.name = parcel.readString();
        this.surn1 = parcel.readString();
        this.surn2 = parcel.readString();
        this.phone = parcel.readString();
        this.mobile = parcel.readString();
        this.birthDate = parcel.readString();
        this.mail1 = parcel.readString();
        this.mail2 = parcel.readString();
    }

    public static final Parcelable.Creator<Student> CREATOR
            = new Parcelable.Creator<Student>() {
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getPhotoPath() {
        return String.format(course.getPhotosTemplate(), photoId);
    }
    public int getIdentity() {
        return photoId;
    }
    public void setIdentity(int photoId) {
        this.photoId = photoId;
    }
    public String getSurname1() {
        return surn1;
    }
    public void setSurn1(String surn1) {
        this.surn1 = surn1;
    }
    public String getSurname2() {
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
    public String getPhone() {
        return phone;
    }
    public String getMobile() {
        return mobile;
    }
    public String getBirthdate() {
        return birthDate;
    }
    public String getMail1() {
        return mail1;
    }
    public String getMail2() {
        return mail2;
    }


    @Override
    public String toString() {
        return StringUtils.removeAccents(name + " " + surn1 + " " + surn2); //BEWARE: this string is used for filtering the listview
    }
}
