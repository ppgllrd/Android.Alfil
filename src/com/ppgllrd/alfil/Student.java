package com.ppgllrd.alfil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepeg on 26/06/13.
 */
public class Student implements Parcelable {
    private Course course;
    private String name;
    private String surn1;
    private String surn2;
    private String phone;
    private String mobile;
    private String birthDate;
    private String mail1;
    private String mail2;
    private String photo;
    private String dni;
    private String expedient;
    private int enrollments;
    private int failures;


    public Student(Course course, String name, String surn1, String surn2
                  ,String phone, String mobile, String birthDate, String mail1, String mail2
                  , String photo, String dni, String expedient, int enrollments, int failures) {
        this.course = course;
        this.name = name;
        this.surn1 = surn1;
        this.surn2 = surn2;
        this.phone = phone;
        this.mobile = mobile;
        this.birthDate = birthDate;
        this.mail1 = mail1;
        this.mail2 = mail2;
        this.photo = photo;
        this.dni = dni;
        this.expedient = expedient;
        this.enrollments = enrollments;
        this.failures = failures;
    }

    public Student(Course course) {
        this(course,"","","","","","","","","","","",0,0);
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(course, flags);
        parcel.writeString(name);
        parcel.writeString(surn1);
        parcel.writeString(surn2);
        parcel.writeString(phone);
        parcel.writeString(mobile);
        parcel.writeString(birthDate);
        parcel.writeString(mail1);
        parcel.writeString(mail2);
        parcel.writeString(photo);
        parcel.writeString(dni);
        parcel.writeString(expedient);
        parcel.writeInt(enrollments);
        parcel.writeInt(failures);
    }

    public int describeContents() {
        return 0;
    }

    private Student(Parcel parcel) {
        this.course = parcel.readParcelable(Course.class.getClassLoader());
        this.name = parcel.readString();
        this.surn1 = parcel.readString();
        this.surn2 = parcel.readString();
        this.phone = parcel.readString();
        this.mobile = parcel.readString();
        this.birthDate = parcel.readString();
        this.mail1 = parcel.readString();
        this.mail2 = parcel.readString();
        this.photo = parcel.readString();
        this.dni = parcel.readString();
        this.expedient = parcel.readString();
        this.enrollments = parcel.readInt();
        this.failures = parcel.readInt();
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
        return course.getPhotoFullPath(photo);
    }
    public String getPhoto() {
        return photo;
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
    public String getDNI() {
        return dni;
    }
    public int getEnrollments() {
        return enrollments;
    }
    public int getFailures() {
        return failures;
    }


    @Override
    public String toString() {
        return StringUtils.removeAccents(name + " " + surn1 + " " + surn2); //BEWARE: this string is used for filtering the listview
    }
}
