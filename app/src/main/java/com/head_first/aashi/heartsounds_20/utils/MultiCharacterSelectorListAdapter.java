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

public class MultiCharacterSelectorListAdapter<T extends Enum> extends BaseAdapter {
    private static final int LIST_ITEM_LAYOUT = R.layout.multi_character_selector_list_item;

    //Views and context
    private Context context;

    //Data
    private List<CHARACTER> selectedObjects;

    public MultiCharacterSelectorListAdapter(Context context, List<CHARACTER> selectedObjects){
        if(context == null){
            //throw an exception
        }
        this.context = context;
        this.selectedObjects = selectedObjects;
    }


    @Override
    public int getCount() {
        return CHARACTER.values().length;
    }

    @Override
    public Object getItem(int position) {
        return CHARACTER.values()[position];
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
        checkBox.setText(CHARACTER.values()[position].toString());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MultiCharacterSelectorListAdapter.this.onCheckedChanged(buttonView, isChecked);
            }
        });
        if(selectedObjects.contains(CHARACTER.values()[position])){
            checkBox.setChecked(true);
        }

        return convertView;
    }

    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        String selectedString = selectedObjects.toString();
        String checkBoxText = ((CheckBox)buttonView).getText().toString();
        if(isChecked){
            if(!selectedString.toLowerCase().contains(checkBoxText.toLowerCase())){
                selectedObjects.add(CHARACTER.getCharacter(checkBoxText));
            }
        }
        else{
            if(selectedString.toLowerCase().contains(checkBoxText.toLowerCase())){
                selectedObjects.remove(CHARACTER.getCharacter(checkBoxText));
            }
        }
    }
}
