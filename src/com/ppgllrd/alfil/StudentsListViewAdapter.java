package com.ppgllrd.alfil;

import java.io.File;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ppgllrd.alfil.R;

/**
 * Created by pepeg on 26/06/13.
 */
public class StudentsListViewAdapter extends ArrayAdapter<StudentsListItem> {
        Context context;

        public StudentsListViewAdapter(Context context, int resourceId, List<StudentsListItem> items) {
            super(context, resourceId, items);
            this.context = context;
        }

        /*private view holder class*/
        private class ViewHolder {
            ImageView imageView;
            TextView txtName;
            TextView txtDesc;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            StudentsListItem studentsListItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.students_list_item, null);
                holder = new ViewHolder();
                holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
                holder.txtName = (TextView) convertView.findViewById(R.id.name);
                holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.txtDesc.setText(studentsListItem.getDesc());
            holder.txtName.setText(studentsListItem.getName());

            //File file = new File("/mnt/sdcard/alfil/mates/alu"+studentsListItem.getPhotoId()+".jpg");
            String fileName = "/mnt/sdcard/alfil/mates/alu"+studentsListItem.getPhotoId()+".jpg";

//            holder.imageView.setImageResource(studentsListItem.getPhotoId());
            holder.imageView.setImageDrawable(Drawable.createFromPath(fileName));

            return convertView;
        }
    }