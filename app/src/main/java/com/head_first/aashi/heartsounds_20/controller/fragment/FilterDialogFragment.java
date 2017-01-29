package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.model.Filter;
import com.head_first.aashi.heartsounds_20.utils.DynamicSearchFilter;
import com.head_first.aashi.heartsounds_20.utils.ExpandableFilterListAdapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterDialogFragment extends DialogFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{
    public static final String FILTER_DIALOG_FRAGMENT_TAG = "FILTER_DIALOG_FRAGMENT";
    public static final String FILTER_CONTENT_MAP_TAG = "FILTER_CONTENT_MAP_TAG";

    //View related variables
    private Dialog filterDialog;
    private SearchView mSearchView;
    private TextView mSearchStringView;
    private Button mSearchButton;
    private ExpandableListView mExpandableListFilter;
    private ExpandableFilterListAdapter expandableFilterAdapter;

    //Data for the Fragment
    private Filter filter;
    private Map<String, List<Filter.GroupItem>> searchFilterContent;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FilterDialogFragment() {
        //Required empty constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterDialogFragment newInstance(String param1, String param2) {
        FilterDialogFragment fragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            //Get the filter object from the Fragment that launched this
            if((filter =(Filter) bundle.getSerializable(FILTER_CONTENT_MAP_TAG)) != null){
                searchFilterContent = new LinkedHashMap<>();
                searchFilterContent.putAll(filter.getFilterContent());
            }
            else{
                //Throw an exception here
            }
        }
        else{
            //Throw an exception here
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        if(filterDialog == null){
//            // Inflate the layout for this fragment
//            filterDialog = inflater.inflate(R.layout.heart_sound_ui_experiments_fragment_filter_layout, container, false);
//        }
//        //SearchView Setup
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        mSearchView = (SearchView) filterDialog.findViewById(R.id.filterItems);
//        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setOnCloseListener(this);
//        //Search bar
//        mSearchStringView = (TextView) filterDialog.findViewById(R.id.filterString);
//        mSearchStringView.setText(filter.getSearchString());
//        mSearchButton = (Button) filterDialog.findViewById(R.id.searchButton);
//        mSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //use the information stored in the filter object to get all the related data from the database
//                //then finish this fragment
//            }
//        });
//
//        //Expandable Filter
//        mExpandableListFilter = (ExpandableListView) filterDialog.findViewById(R.id.expandableFilterView);
//        expandableFilterAdapter = new ExpandableFilterListAdapter(getContext(), searchFilterContent);
//        mExpandableListFilter.setAdapter(expandableFilterAdapter);
//        mExpandableListFilter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                FilterDialogFragment.this.onFilterClick(parent, v, groupPosition, childPosition, id);
//                return false;
//            }
//        });
//        expandableFilterAdapter.notifyDataSetChanged();
//
//        return filterDialog;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        filterDialog = new Dialog(getActivity());
        filterDialog.setCancelable(false);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.fragment_filter_dialog);

        //SearchView Setup
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) filterDialog.findViewById(R.id.filterItems);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        //Search bar
        mSearchStringView = (TextView) filterDialog.findViewById(R.id.filterString);
        mSearchStringView.setText(filter.getSearchString());
        mSearchButton = (Button) filterDialog.findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use the information stored in the filter object to get all the related data from the database
                //then finish this fragment
            }
        });

        //Expandable Filter
        mExpandableListFilter = (ExpandableListView) filterDialog.findViewById(R.id.expandableFilterView);
        expandableFilterAdapter = new ExpandableFilterListAdapter(getContext(), searchFilterContent);
        mExpandableListFilter.setAdapter(expandableFilterAdapter);
        mExpandableListFilter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                FilterDialogFragment.this.onFilterClick(parent, v, groupPosition, childPosition, id);
                return false;
            }
        });
        expandableFilterAdapter.notifyDataSetChanged();
        filterDialog.create();
        return filterDialog;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(this.FILTER_CONTENT_MAP_TAG, filter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void onFilterClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id){
        filter.onFilterSelected(parent, view, groupPosition, childPosition, id);
        expandableFilterAdapter.notifyDataSetChanged();
        mSearchStringView.setText(filter.getSearchString());
    }

    //To display the search button on the toolbar
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_view_menu, menu);
//    }

    //SearchView.OnQueryTextListener Implementation
    @Override
    public boolean onQueryTextSubmit(String query) {
        DynamicSearchFilter.onQueryTextSubmit(query, filter.getFilterContent(), searchFilterContent);
        expandableFilterAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        DynamicSearchFilter.onQueryTextChange(newText , filter.getFilterContent(), searchFilterContent);
        expandableFilterAdapter.notifyDataSetChanged();
        return false;
    }

    //SearchView.OnCloseListener Implementation
    @Override
    public boolean onClose() {
        return false;
    }
}

//    private void restoreExpandableListViewState(){
//        if(!searchString.isEmpty()){
//            String[] filterGroup = StringUtil.split(searchString, SEARCH_STRING_GROUP_HEADER_SEPARATOR.charAt(0));
//            List<View> groupHeaderView = expandableFilterAdapter.getListGroupHeaderView();
//            for(int i = 0; i < groupHeaderView.size() && i < filterGroup.length; i++){
//                List<View> groupItemsView = expandableFilterAdapter.getItems(groupHeaderView.get(i));
//                String[] items = filterGroup[i].split(SEARCH_STRING_GROUP_ITEM_SEPARATOR);
//                for(int j = 0; j < groupItemsView.size(); j++){
//                    for(int k = 0; k < items.length; k++){
//                        CheckBox anItem = (CheckBox) groupItemsView.get(j);
//                        if(anItem.getText().toString().toLowerCase().equals(items[k].toLowerCase())){
//                            anItem.setChecked(true);
//                        }
//                    }
//                }
//            }
//        }
//    }
