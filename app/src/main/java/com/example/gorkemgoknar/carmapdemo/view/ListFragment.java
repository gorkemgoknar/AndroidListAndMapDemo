package com.example.gorkemgoknar.carmapdemo.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gorkemgoknar.carmapdemo.model.Placemark;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.example.gorkemgoknar.carmapdemo.presenter.ListPresenter;
import com.example.gorkemgoknar.carmapdemo.presenter.adapters.PlacemarkListArrayAdapter;
import com.example.gorkemgoknar.carmapdemo.R;

import java.util.ArrayList;

/*
   Handles showing placemarks in listview
 */
public class ListFragment extends Fragment implements ListPresenter.View {

    private ListPresenter presenter;

    ListView listView;
    private ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        presenter = new ListPresenter(this);

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        View header = getLayoutInflater().inflate(R.layout.header_list,null);


        String progressMessage = "Fetching from Internet..";
        if (presenter.isFetchingFromLocal()){
            progressMessage = "Fetching from local cache..";
        }
        progress = ProgressDialog.show(getActivity(), "Fetching Car Data",
                progressMessage, true);

        listView = (ListView) rootView.findViewById(R.id.item_list);
        listView.addHeaderView(header);


        //populateListView();

        return rootView;
    }

    @Override
    public void populateView(Placemarks placemarks){
        //Will be called once json is ready

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


        /*
        *  Test Populating list items

        for(int i=0; i<100; i++) {
            String someString = RandomGenerator.generateRandomString(8);
            Double[] someCoordinate = RandomGenerator.generateRandomCoordinate();
            placeMarkList.add(new Placemark(someString,someCoordinate));
        }
        */

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





}
