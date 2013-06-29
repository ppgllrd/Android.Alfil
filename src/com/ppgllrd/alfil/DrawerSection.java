package com.ppgllrd.alfil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pepeg on 28/06/13.
 */
public class DrawerSection extends DrawerItem {
    private String sectionText;

    public DrawerSection(String sectionText) {
        this.sectionText = sectionText;
    }

    public String getSectionText() {
        return sectionText;
    }

    @Override
    public String toString() {
        return sectionText;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView textView;
    }

    public View getView(Context context, View convertView) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_section_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.drawer_sectionText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(getSectionText());
        return convertView;
    }
}
