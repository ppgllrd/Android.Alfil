package com.ppgllrd.alfil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by pepeg on 28/06/13.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
    Context context;

    public DrawerAdapter(Context context, int resourceId, List<DrawerItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItem drawerItem = getItem(position);
        return drawerItem.getView(context, convertView);
    }
}
