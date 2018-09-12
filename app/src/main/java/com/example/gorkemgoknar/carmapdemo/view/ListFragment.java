package com.example.gorkemgoknar.carmapdemo.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gorkemgoknar.carmapdemo.model.Placemark;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.example.gorkemgoknar.carmapdemo.presenter.ListPresenter;
import com.example.gorkemgoknar.carmapdemo.view.adapters.PlacemarkListArrayAdapter;
import com.example.gorkemgoknar.carmapdemo.R;

import java.util.ArrayList;

/*
   Handles showing placemarks in listview
 */
public class ListFragment extends Fragment implements ListPresenter.View {

    private ListPresenter presenter;

    ListView listView;
    private ProgressDialog progress;

    private String progressMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        presenter = new ListPresenter(this);

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        View header = getLayoutInflater().inflate(R.layout.header_list,null);


        progressMessage = "Fetching from Internet..";
        if (presenter.placemarksExistsInCache()){
            progressMessage = "Fetching from local cache..";
        }

        listView = (ListView) rootView.findViewById(R.id.item_list);
        listView.addHeaderView(header);


        //populateListView();

        return rootView;
    }

    @Override
    public void populateView(Placemarks placemarks){
        //Will be called once json is ready
        if (placemarks == null){
            //something is wrong
            Log.e("Placemakr", "NO PLACEMARK - it is null");

            //trigger getting placemark again
            return;
        }

        // Initializing list view with the custom adapter
        ArrayList<Placemark> placeMarkList = new ArrayList<Placemark>();
        PlacemarkListArrayAdapter itemArrayAdapter = new PlacemarkListArrayAdapter
                (this.getContext(), R.layout.list_item, placeMarkList);
        //listView = (ListView) rootView.findViewById(R.id.item_list);
        listView.setAdapter(itemArrayAdapter);


        //Populate
        for (Placemark placemark : placemarks.getPlacemarks()) {
            placeMarkList.add(placemark);
        }

        // Set up list item onclick listener
        setUpListItemClickListener();

        progress.dismiss();

    }


    private void setUpListItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "item " + position + " clicked:", Toast.LENGTH_SHORT).show();
                //TODO show on map

            }
        });
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("LIST VIEW");
    }


    public ListPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(ListPresenter presenter) {
        this.presenter = presenter;
    }


    public void dismissProgress(){
        progress.dismiss();
    }
    public void showProgress(){
        progress = ProgressDialog.show(getActivity(), "Fetching Car Data",
                progressMessage, true);
    }

    public void showNoPlacemarkError(){
        Toast.makeText(getActivity(), "No Placemark to show please retry later..", Toast.LENGTH_LONG).show();
    }

}
