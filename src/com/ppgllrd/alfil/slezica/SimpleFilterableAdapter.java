package com.ppgllrd.alfil.slezica;

/*
* The MIT License
* Copyright (c) 2011 Santiago Lezica (slezica89@gmail.com)
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*
*/


import android.content.Context;

import java.util.List;


public class SimpleFilterableAdapter<ObjectType> extends FilterableAdapter<ObjectType, String> {

    public SimpleFilterableAdapter(Context context, int resourceId, int textResourceId, List<ObjectType> objects) {
        super(context, resourceId, textResourceId, objects);
    }

    public SimpleFilterableAdapter(Context context, int resourceId,
                                   List<ObjectType> objects) {
        super(context, resourceId, objects);
    }

    public SimpleFilterableAdapter(Context context, int resourceId) {
        super(context, resourceId);
    }

    public SimpleFilterableAdapter(Context context, List<ObjectType> objects) {
        super(context, objects);
    }

    public SimpleFilterableAdapter(Context context) {
        super(context);
    }

    @Override
    protected String prepareFilter(CharSequence seq) {
        return seq.toString().toLowerCase();
    }

    @Override
    protected boolean passesFilter(ObjectType object, String constraint) {
        String repr = object.toString().toLowerCase();

        if (repr.startsWith(constraint))
            return true;

        else {
            final String[] words = repr.split(" ");
            final int wordCount = words.length;

            for (int i = 0; i < wordCount; i++) {
                if (words[i].startsWith(constraint))
                    return true;
            }
        }

        return false;
    }
}

/*

package me.alxandr.android.mymir.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import me.alxandr.android.mymir.R;
import me.alxandr.android.mymir.model.Manga;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MangaListAdapter extends ArrayAdapter<Manga> implements SectionIndexer
{
    public ArrayList<Manga> items;
    public ArrayList<Manga> filtered;
    private Context context;
    private HashMap<String, Integer> alphaIndexer;
    private String[] sections = new String[0];
    private Filter filter;
    private boolean enableSections;

    public MangaListAdapter(Context context, int textViewResourceId, ArrayList<Manga> items, boolean enableSections)
    {
        super(context, textViewResourceId, items);
        this.filtered = items;
        this.items = filtered;
        this.context = context;
        this.filter = new MangaNameFilter();
        this.enableSections = enableSections;

        if(enableSections)
        {
            alphaIndexer = new HashMap<String, Integer>();
            for(int i = items.size() - 1; i >= 0; i--)
            {
                Manga element = items.get(i);
                String firstChar = element.getName().substring(0, 1).toUpperCase();
                if(firstChar.charAt(0) > 'Z' || firstChar.charAt(0) < 'A')
                    firstChar = "@";

                alphaIndexer.put(firstChar, i);
            }

            Set<String> keys = alphaIndexer.keySet();
            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();
            while(it.hasNext())
                keyList.add(it.next());

            Collections.sort(keyList);
            sections = new String[keyList.size()];
            keyList.toArray(sections);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if(v == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.mangarow, null);
        }

        Manga o = items.get(position);
        if(o != null)
        {
            TextView tt = (TextView) v.findViewById(R.id.MangaRow_MangaName);
            TextView bt = (TextView) v.findViewById(R.id.MangaRow_MangaExtra);
            if(tt != null)
                tt.setText(o.getName());
            if(bt != null)
                bt.setText(o.getLastUpdated() + " - " + o.getLatestChapter());

            if(enableSections && getSectionForPosition(position) != getSectionForPosition(position + 1))
            {
                TextView h = (TextView) v.findViewById(R.id.MangaRow_Header);
                h.setText(sections[getSectionForPosition(position)]);
                h.setVisibility(View.VISIBLE);
            }
            else
            {
                TextView h = (TextView) v.findViewById(R.id.MangaRow_Header);
                h.setVisibility(View.GONE);
            }
        }

        return v;
    }

    @Override
    public void notifyDataSetInvalidated()
    {
        if(enableSections)
        {
            for (int i = items.size() - 1; i >= 0; i--)
            {
                Manga element = items.get(i);
                String firstChar = element.getName().substring(0, 1).toUpperCase();
                if(firstChar.charAt(0) > 'Z' || firstChar.charAt(0) < 'A')
                    firstChar = "@";
                alphaIndexer.put(firstChar, i);
            }

            Set<String> keys = alphaIndexer.keySet();
            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();
            while (it.hasNext())
            {
                keyList.add(it.next());
            }

            Collections.sort(keyList);
            sections = new String[keyList.size()];
            keyList.toArray(sections);

            super.notifyDataSetInvalidated();
        }
    }

    public int getPositionForSection(int section)
    {
        if(!enableSections) return 0;
        String letter = sections[section];

        return alphaIndexer.get(letter);
    }

    public int getSectionForPosition(int position)
    {
        if(!enableSections) return 0;
        int prevIndex = 0;
        for(int i = 0; i < sections.length; i++)
        {
            if(getPositionForSection(i) > position && prevIndex <= position)
            {
                prevIndex = i;
                break;
            }
            prevIndex = i;
        }
        return prevIndex;
    }

    public Object[] getSections()
    {
        return sections;
    }

    @Override
    public Filter getFilter()
    {
        if(filter == null)
            filter = new MangaNameFilter();
        return filter;
    }

    private class MangaNameFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // NOTE: this function is *always* called from a background thread, and
            // not the UI thread.
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<Manga> filt = new ArrayList<Manga>();
                ArrayList<Manga> lItems = new ArrayList<Manga>();
                synchronized (items)
                {
                    Collections.copy(lItems, items);
                }
                for(int i = 0, l = lItems.size(); i < l; i++)
                {
                    Manga m = lItems.get(i);
                    if(m.getName().toLowerCase().contains(constraint))
                        filt.add(m);
                }
                result.count = filt.size();
                result.values = filt;
            }
            else
            {
                synchronized(items)
                {
                    result.values = items;
                    result.count = items.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            filtered = (ArrayList<Manga>)results.values;
            notifyDataSetChanged();
        }

    }
}

*/