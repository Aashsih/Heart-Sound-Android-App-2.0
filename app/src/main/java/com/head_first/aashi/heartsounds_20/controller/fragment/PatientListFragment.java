package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.activities.PatientHeartSoundActivity;
import com.head_first.aashi.heartsounds_20.model.Filter;
import com.head_first.aashi.heartsounds_20.utils.DynamicSearchFilter;
import com.head_first.aashi.heartsounds_20.utils.ExpandablePatientListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientListFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    private static final String DEFAULT_SEARCH_STRING = "";

    private DialogFragment filterFragment;
    private View mRootView;
    private ImageButton mFilterButton;
    private SearchView mSearchView;
    private ImageButton mAddNewPatient;
    private ExpandableListView mExpandableListView;
    private ExpandablePatientListAdapter expandablePatientListAdapter;

    //Data for the fragment
    private Filter filter;
    private String oldFilterString; //this is made equal to the filter String from filter.getSearchString() before the view of this fragment is destroyed
    //There will be a few more data structures here that will store the information from the Database, more specifically
    //Information about Patient and Study.
    //The Study model along with Patient model will be used to create the HashMap<String, Patient> in the Filter Model
    //That HashMap will have all the information that the app requires based on the FilterDialogFragment.
    //Another HashMap will be created from the previous one which will contain all the information based on the
    //search filter in this fragment.

    //This Map gets it information from the filterContent of the Filter Model
    //it is basially its replica apart from the fact that the groupItems here are String instead of Filter.GroupItem
    private LinkedHashMap<String, List<String>> filterContentMap;
    private LinkedHashMap<String, List<String>> searchContentMap;
    private boolean myPatientClicked;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PatientListFragment() {
        filter = new Filter(DEFAULT_SEARCH_STRING);
        myPatientClicked = true;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientListFragment newInstance(String param1, String param2) {
        PatientListFragment fragment = new PatientListFragment();
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
        oldFilterString = "null";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!oldFilterString.equals(filter.getSearchString())){
            setUpDataForViews();
        }
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_patients, container, false);

        if(!myPatientClicked){
            mAddNewPatient = (ImageButton) mRootView.findViewById(R.id.addPatient);
            mAddNewPatient.setVisibility(View.INVISIBLE);
        }

        //Filter Button
        mFilterButton = (ImageButton) mRootView.findViewById(R.id.filterButton);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilterFragment();
            }
        });

        //SearchView Setup
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) mRootView.findViewById(R.id.patientSearchView);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);

        //Setup ExpandableListView
        expandablePatientListAdapter = new ExpandablePatientListAdapter(getContext(), searchContentMap);
        mExpandableListView = (ExpandableListView) mRootView.findViewById(R.id.expandableListView);
        mExpandableListView.setAdapter(expandablePatientListAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onPatientSelected(parent, v, groupPosition, childPosition, id);
                return false;
            }
        });
        setHasOptionsMenu(true);

        return mRootView;
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
    public void onDestroyView(){
        super.onDestroyView();
        oldFilterString = filter.getSearchString();
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

    public void setMyPatientClicked(boolean myPatientClicked){
        this.myPatientClicked = myPatientClicked;
    }

    public boolean isMyPatientClicked(){
        return this.myPatientClicked;
    }

    private void setUpDataForViews(){
        //Load all the models over here based on myPatientClicked
        if(myPatientClicked){
            //Load models for MyPatients
            int x = 0;
            x++;
        }
        else{
            //Load models for SharedPatients
            int x = 0;
            x++;
        }
        //after the models are loaded, load the filterContent in the Filter Model
        filter.populateFilterContent(myPatientClicked);//Pass in the required models as parameters
        // the models will be usd to populate the filterContentMap and not filter.filterContent
        //this is just used temporarily
        filterContentMap = getFilterContentMap(filter.getFilterContent());
        setupSearchContent();//This will get content from models.
    }


    private void launchFilterFragment(){
        oldFilterString = filter.getSearchString();
        if(filterFragment == null){
            filterFragment = new FilterDialogFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(FilterDialogFragment.FILTER_CONTENT_MAP_TAG, filter);
        filterFragment.setArguments(bundle);
        filterFragment.show(getFragmentManager(), "");
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainer, filterFragment)
//                .addToBackStack(null)
//                .commit();

    }
    //Utility Methods for the fragment
    private void setupSearchContent(){
        if(searchContentMap == null){
            searchContentMap = new LinkedHashMap<>();
        }
        searchContentMap.clear();
        searchContentMap.putAll(filterContentMap);
        //set the content for searchContentMap here
        //SearchBar.onQueryTextSubmit("", filterContentMap, searchContentMap);

    }

    private LinkedHashMap<String, List<String>> getFilterContentMap(Map<String, List<Filter.GroupItem>> filterContent){
        LinkedHashMap<String, List<String>> filterContentMap = new LinkedHashMap<>();
        for(String groupHeader : filter.getFilterContent().keySet()){
            List<String> groupItems = new ArrayList<>();
            for(Filter.GroupItem groupItem : filter.getFilterContent().get(groupHeader)){
                groupItems.add(groupItem.getItemName());
            }
            filterContentMap.put(groupHeader, groupItems);
        }
        return filterContentMap;
    }

    private void onPatientSelected(ExpandableListView parent, View view, int groupPosition, int childPosition, long id){
        Intent patientHeartSoundActivityIntent = new Intent(getActivity(), PatientHeartSoundActivity.class);
        startActivity(patientHeartSoundActivityIntent);
    }

    //SearchView onCreateOptionsMenu implementation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view_menu, menu);
    }

    //SearchView.OnQueryTextListener Implementation
    @Override
    public boolean onQueryTextSubmit(String query) {
        DynamicSearchFilter.onQueryTextSubmit(query, filterContentMap, searchContentMap);
        expandablePatientListAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        DynamicSearchFilter.onQueryTextChange(newText , filterContentMap, searchContentMap);
        expandablePatientListAdapter.notifyDataSetChanged();
        return false;
    }

    //SearchView.OnCloseListener Implementation
    @Override
    public boolean onClose() {
        return false;
    }
}
