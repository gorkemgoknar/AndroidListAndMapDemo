package com.example.gorkemgoknar.carmapdemo.presenter;

import com.example.gorkemgoknar.carmapdemo.model.Persistence;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;

/*
  Presenter to get placemark model and use on List View
 */
public class ListPresenter extends AbstractPlacemarkPresenter {


    private View view;

    public ListPresenter(View view) {

        this.view = view;

        view.showProgress();

        String jsonStr = null;

        try {
            jsonStr = Persistence.getLocalJson("placemarkJson");

            if (jsonStr != null){
                populatePlacemarkFromJson(jsonStr);
                handlePlacemarkIsRefreshedFromCache();

                view.dismissProgress();

                return;

            }

        } catch(Exception e){
            //could not get local json somewhow
            //do nothing for now will fetch anyway if it is null
        }

        new FetchData().execute();


    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void refetchPlacemarks(){
        new FetchData().execute();
    }


    /**
     * Implementation of abstact methods
     */
    protected void handlePlacemarkIsRefreshedFromNet(){
        view.populateView(this.getPlacemarks());
        view.dismissProgress();
    }
    protected void handlePlacemarkIsRefreshedFromCache(){
        view.populateView(this.getPlacemarks());
        view.dismissProgress();
    }
    protected void handleNoPlacemarkInfoError(){
        view.dismissProgress();
        view.showNoPlacemarkError();
    }

    //writing abstract method
    protected void handlePostJson(){

        view.populateView(this.getPlacemarks());
    }

    public interface View{

        void populateView(Placemarks placemarks);
        void dismissProgress();
        void showProgress();

        void showNoPlacemarkError();



    }


}
