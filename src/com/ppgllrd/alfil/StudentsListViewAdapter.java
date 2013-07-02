package com.ppgllrd.alfil;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pepeg on 26/06/13.
 */
public class StudentsListViewAdapter extends ArrayAdapter<Student> {
    Context context;
    Course course;

    public StudentsListViewAdapter(Context context, int resourceId, List<Student> items, Course course) {
        super(context, resourceId, items);
        this.context = context;
        this.course = course;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView surname;
        TextView name;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Student student = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.students_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.surname = (TextView) convertView.findViewById(R.id.surname);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            LoadPhotoTask task = (LoadPhotoTask) holder.imageView.getTag(R.id.icon);
            if (task != null) {
                task.cancel(true);
            }
        }

        holder.surname.setText(student.getSurname1() + " " + student.getSurname2());
        holder.name.setText(student.getName());

        String fileName = student.getPhotoPath();
        holder.imageView.setImageDrawable(null);
        LoadPhotoTask task = new LoadPhotoTask(holder.imageView);
        task.execute(fileName);

        holder.imageView.setTag(R.id.icon, task);
        //holder.imageView.setImageDrawable(Drawable.createFromPath(fileName));

        return convertView;
    }
}
