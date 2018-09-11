package com.example.gorkemgoknar.carmapdemo.presenter;

import com.example.gorkemgoknar.carmapdemo.model.Placemarks;

/*
  Presenter to get placemark model and use on List View
 */
public class ListPresenter extends AbstractPlacemarkPresenter {

    private View view;

    public ListPresenter(View view) {

        this.view = view;

        //Fetch async json car list;
        new FetchPlacemarks().execute();

    }

    //writing abstract method
    protected void handlePostJson(){
        view.populateView(this.getPlacemarks());
    }
    public interface View{

        void populateView(Placemarks placemarks);

    }


}
