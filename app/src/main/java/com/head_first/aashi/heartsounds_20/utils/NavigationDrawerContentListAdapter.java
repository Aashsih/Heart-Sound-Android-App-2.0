package com.head_first.aashi.heartsounds_20.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Aashish Indorewala on 15-Jan-17.
 */

public class NavigationDrawerContentListAdapter<T> extends BaseAdapter{
    private static final int LIST_ITEM_LAYOUT = R.layout.navigation_view_list_adapter_item;

    //Views and context
    private Context context;

    //Data
    private List<T> objects;

    public NavigationDrawerContentListAdapter(Context context, List<T> objects){
        if(context == null){
            //throw an exception
        }
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        if(objects != null){
            return objects.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(objects != null){
            return objects.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(LIST_ITEM_LAYOUT, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.navigationViewContentItem);
        textView.setText(objects.get(position).toString());

        return convertView;
    }
}
