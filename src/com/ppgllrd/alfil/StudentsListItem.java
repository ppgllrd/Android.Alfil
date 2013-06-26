package com.ppgllrd.alfil;

/**
 * Created by pepeg on 26/06/13.
 */
public class StudentsListItem {
    private int photoId;
    private String name;
    private String desc;

    public StudentsListItem(int photoId, String name, String desc) {
        this.photoId = photoId;
        this.name = name;
        this.desc = desc;
    }
    public int getPhotoId() {
        return photoId;
    }
    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name + "\n" + desc;
    }
}
