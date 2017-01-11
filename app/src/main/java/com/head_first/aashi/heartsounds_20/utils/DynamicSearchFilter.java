package com.head_first.aashi.heartsounds_20.utils;

import com.head_first.aashi.heartsounds_20.model.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 11-Jan-17.
 */

public class DynamicSearchFilter {

    public static final <K,V> void onQueryTextChange(String query, Map<K, List<V>> allContentMap, Map<K, List<V>> searchContentMap){
        if(query == null || query == ""){
            if(searchContentMap != null){
                searchContentMap.putAll(allContentMap);
            }
            return;
        }
        searchContentMap.clear();
        for(K groupHeader : allContentMap.keySet()){
            List<V> filteredGroupItems = new ArrayList<>();
            searchContentMap.put(groupHeader, filteredGroupItems);
            List<V> allGroupItems = allContentMap.get(groupHeader);
            filterGroupItemsOnQueryChange(query, allGroupItems, filteredGroupItems);
        }
    }

    private static <V> void filterGroupItemsOnQueryChange(String query, List<V> allGroupItems, List<V> filteredGroupItems){
        for(int i = 0; i < allGroupItems.size(); i++){
            if(allGroupItems.get(i) instanceof String){
                String groupItem = (String) allGroupItems.get(i);
                if(groupItem.toLowerCase().contains(query.toLowerCase())){
                    filteredGroupItems.add(allGroupItems.get(i));
                }
            }
            else if(allGroupItems.get(i) instanceof Filter.GroupItem){
                Filter.GroupItem groupItem = (Filter.GroupItem) allGroupItems.get(i);
                if(groupItem.getItemName().toLowerCase().contains(query.toLowerCase())){
                    filteredGroupItems.add(allGroupItems.get(i));
                }
            }
        }
    }

    public static final <K, V> void onQueryTextSubmit(String query, Map<K, List<V>> allContentMap, Map<K, List<V>> searchContentMap){
        if(query == null || query == ""){
            if(searchContentMap != null){
                searchContentMap.putAll(allContentMap);
            }
            return;
        }
        searchContentMap.clear();
        for(K groupHeader : allContentMap.keySet()){
            List<V> filteredGroupItems = new ArrayList<>();
            searchContentMap.put(groupHeader, filteredGroupItems);
            List<V> allGroupItems = allContentMap.get(groupHeader);
            filterGroupItemsOnQuerySubmit(query, allGroupItems, filteredGroupItems);

        }
    }

    private static <T> void filterGroupItemsOnQuerySubmit(String query, List<T> allGroupItems, List<T> filteredGroupItems){
        for(int i = 0; i < allGroupItems.size(); i++){
            if(allGroupItems.get(i) instanceof String){
                String groupItem = (String) allGroupItems.get(i);
                if(groupItem.toLowerCase().startsWith(query.toLowerCase())){
                    filteredGroupItems.add(allGroupItems.get(i));
                }
            }
            else if(allGroupItems.get(i) instanceof Filter.GroupItem){
                Filter.GroupItem groupItem = (Filter.GroupItem) allGroupItems.get(i);
                if(groupItem.getItemName().toLowerCase().startsWith(query.toLowerCase())){
                    filteredGroupItems.add(allGroupItems.get(i));
                }
            }
        }
    }

}

