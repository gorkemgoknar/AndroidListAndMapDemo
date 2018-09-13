package com.example.gorkemgoknar.carmapdemo.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.app.AlertDialog;

import com.example.gorkemgoknar.carmapdemo.model.Placemark;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.example.gorkemgoknar.carmapdemo.presenter.ListPresenter;
import com.example.gorkemgoknar.carmapdemo.view.adapters.PlacemarkListArrayAdapter;
import com.example.gorkemgoknar.carmapdemo.R;

import java.util.ArrayList;

/*
   Handles showing placemarks in listview
 */

//TODO : better inherit list and map fragments from common base for same functionality

public class ListFragment extends Fragment implements ListPresenter.View {

    private static final String TAG = ListFragment.class.getSimpleName();


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


        progressMessage = "Fetching new data..";


        listView = (ListView) rootView.findViewById(R.id.item_list);
        listView.addHeaderView(header);

        fetchData();


        return rootView;
    }

    //used to refresh data
    //can be called from outside the class too
    public void fetchData(){
        Log.i(TAG,"fetch data");
        presenter.fetchPlacemarks();
    }

    public void fetchDataFromNet(){
        Log.i(TAG,"fetch data");
        presenter.fetchPlacemarksFromNetwork();
    }
    @Override
    public void populateView(Placemarks placemarks){
        //Will be called once json is ready
        if (placemarks == null){

            Log.e(TAG,"null placemark");
            //TODO: given placemarks was null
            //Send some message to indicate could not populate

            this.showNoPlacemarkError();
            return;
        }

        if (placemarks.getSize() == 0){
            Log.e(TAG,"no car info");
            //TODO: no information on placemarks
            //likely sent json did not contain any car,

            return;
        }

            // Initializing list view with the custom adapter
            ArrayList<Placemark> placeMarkList = new ArrayList<Placemark>();

            PlacemarkListArrayAdapter itemArrayAdapter = new PlacemarkListArrayAdapter
                    (this.getActivity().getApplicationContext(), R.layout.list_item, placeMarkList);

            listView.setAdapter(itemArrayAdapter);


            //Populate list with placemarks
            for (Placemark placemark : placemarks.getPlacemarks()) {
                placeMarkList.add(placemark);
            }

            // Set up list item onclick listener
            setUpListItemClickListener();

    }


    private void setUpListItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), + position + " clicked:", Toast.LENGTH_SHORT).show();
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
        if(this.progress != null) {
            this.progress.dismiss();
        }
    }
    public void showProgress(){
        this.progress = ProgressDialog.show(getActivity(), "Fetching Car Data",
                progressMessage, true);
    }

    public void showNoPlacemarkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getResources().getString(R.string.title_noplacemark));
        builder.setMessage(getResources().getString(R.string.message_noplacemark));
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //retry fetching
                fetchDataFromNet();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        }

    public void showNetworkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getResources().getString(R.string.title_network_unreachable));
        builder.setMessage(getResources().getString(R.string.message_network_unreachable));
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //retry fetching
                fetchData();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
