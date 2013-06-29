package com.ppgllrd.alfil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pepeg on 28/06/13.
 */
public class DrawerCourse extends DrawerItem {
    private Course course;


    public DrawerCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }


    @Override
    public String toString() {
        return course.getName();
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView text;
    }


    public View getView(Context context, View convertView) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_course_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.drawer_courseName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.drawer_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(getCourse().getName());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.drawer_circle));
        return convertView;
    }


}
