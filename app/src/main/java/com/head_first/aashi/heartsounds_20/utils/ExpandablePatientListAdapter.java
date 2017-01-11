package com.head_first.aashi.heartsounds_20.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 11-Jan-17.
 */

public class ExpandablePatientListAdapter extends BaseExpandableListAdapter {

    //Views
    private Context context;
    private static final int GROUP_VIEW_ID = R.id.patientListGroup;
    private static final int ITEM_VIEW_ID = R.id.patientListItem;
    private static final int GROUP_LAYOUT_ID = R.layout.expandable_patient_list_group;
    private static final int ITEM_LAYOUT_ID = R.layout.expandable_patient_list_item;

    //Datatype
    private List<String> listGroupHeaderData;
    private Map<String, List<String>> mapGroupItemsData; //maps the group headers to itemms

    public ExpandablePatientListAdapter(Context context, LinkedHashMap<String, List<String>> mapGroupItemsData){
        this.context = context;
        if(mapGroupItemsData == null || mapGroupItemsData.isEmpty()){
            mapGroupItemsData = new LinkedHashMap<>();
            mapGroupItemsData.put("NO CONTENT FOUND", new ArrayList<String>());
        }
        this.listGroupHeaderData = Collections.unmodifiableList(new ArrayList<String>(mapGroupItemsData.keySet()));
        this.mapGroupItemsData = mapGroupItemsData;
    }

    @Override
    public int getGroupCount() {
        if(this.listGroupHeaderData != null){
            return this.listGroupHeaderData.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition >= 0 && groupPosition < this.listGroupHeaderData.size()){
            return this.mapGroupItemsData.get(this.listGroupHeaderData.get(groupPosition)).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(groupPosition >= 0 && groupPosition < this.listGroupHeaderData.size()) {
            return this.listGroupHeaderData.get(groupPosition);
        }
        return 0;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition >= 0 && groupPosition < this.listGroupHeaderData.size()
                && childPosition >= 0 && childPosition < this.mapGroupItemsData.get(this.listGroupHeaderData.get(groupPosition)).size()) {
            return this.mapGroupItemsData.get(this.listGroupHeaderData.get(groupPosition)).get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(GROUP_LAYOUT_ID, null);
        }
        convertView.setClickable(false);
        TextView listGroupHeader = (TextView) convertView.findViewById(this.GROUP_VIEW_ID);
        listGroupHeader.setText((getGroup(groupPosition)).toString());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(ITEM_LAYOUT_ID, null);
        }
        convertView.setClickable(false);
        customLayoutSetup(groupPosition, childPosition, isLastChild, convertView, parent);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void customLayoutSetup(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        TextView patientListItem = (TextView) convertView.findViewById(this.ITEM_VIEW_ID);
        patientListItem.setText((getChild(groupPosition, childPosition)).toString());
    }


}
