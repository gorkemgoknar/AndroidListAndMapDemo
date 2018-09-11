package com.example.gorkemgoknar.carmapdemo.presenter;

import com.example.gorkemgoknar.carmapdemo.model.Placemarks;

/*
  Presenter to get placemark model and use on Map View
 */
public class MapPresenter extends AbstractPlacemarkPresenter {
    private View view;

    //writing abstract method
    protected void handlePostJson(){
        view.populateView(this.getPlacemarks());
    }

    public interface View{

        void populateView(Placemarks placemarks);

    }


}
