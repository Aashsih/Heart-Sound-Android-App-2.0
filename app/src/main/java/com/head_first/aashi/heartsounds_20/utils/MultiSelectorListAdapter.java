package com.head_first.aashi.heartsounds_20.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.enums.CHARACTER;

import java.util.List;

/**
 * Created by Aashish Indorewala on 29-Jan-17.
 */

public class MultiSelectorListAdapter<T extends Enum> extends BaseAdapter {
    private static final int LIST_ITEM_LAYOUT = R.layout.multi_selector_list_item;

    //Views and context
    private Context context;

    //Data
    private List<Object> objects;
    private List<Object> selectedObjects;

    public MultiSelectorListAdapter(Context context, List<Object> objects, List<Object> selectedObjects){
        if(context == null || objects == null || selectedObjects == null){
            //throw an exception
        }
        this.context = context;
        this.objects = objects;
        this.selectedObjects = selectedObjects;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
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
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        checkBox.setText(objects.get(position).toString());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MultiSelectorListAdapter.this.onCheckedChanged(buttonView, isChecked);
            }
        });
        if(selectedObjects.toString().toLowerCase().contains(objects.get(position).toString().toLowerCase())){
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }

        return convertView;
    }

    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        String selectedString = selectedObjects.toString();
        String checkBoxText = ((CheckBox)buttonView).getText().toString();
        if(isChecked){
            if(!selectedString.toLowerCase().contains(checkBoxText.toLowerCase())){
                Object object = objects.get(objects.size() - 1);
                if(object instanceof CHARACTER){
                    selectedObjects.add(CHARACTER.getCharacter(checkBoxText));
                }
                else if(object instanceof String){//later check against instance of Study
                    //later add the study that matches the text in the checkBoxText
                    selectedObjects.add(checkBoxText);
                }

            }
        }
        else{
            if(selectedString.toLowerCase().contains(checkBoxText.toLowerCase())){
                Object object = objects.get(objects.size() - 1);
                if(object instanceof CHARACTER){
                    selectedObjects.remove(CHARACTER.getCharacter(checkBoxText));
                }
                else if(object instanceof String){//later check against instance of Study
                    //later remove the study that matches the text in the checkBoxText
                    selectedObjects.remove(checkBoxText);
                }
            }
        }
    }
}
