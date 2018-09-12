package com.example.gorkemgoknar.carmapdemo.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gorkemgoknar.carmapdemo.model.Placemark;
import com.example.gorkemgoknar.carmapdemo.R;

import java.util.ArrayList;

/*
   Adapter to fill view with Placemark model
 */
public class PlacemarkListArrayAdapter  extends ArrayAdapter<Placemark>{

    private int placemarkListLayout;

    public PlacemarkListArrayAdapter(Context context, int layoutId, ArrayList<Placemark> placemarkList) {
        super(context, layoutId, placemarkList);
        placemarkListLayout = layoutId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Placemark placemark = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(placemarkListLayout, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.row_name);
            viewHolder.coordinate = (TextView) convertView.findViewById(R.id.row_coordinate);
            viewHolder.engineType = (TextView) convertView.findViewById(R.id.row_engineType);
            viewHolder.vin = (TextView) convertView.findViewById(R.id.row_vin);

            //plus any other visible properties


            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.name.setText(placemark.getName());
        viewHolder.coordinate.setText(placemark.getCoordinateAsString());
        viewHolder.engineType.setText(placemark.getEngineType());
        viewHolder.vin.setText(placemark.getVin());

        // Return the completed view to render on screen
        return convertView;
    }

    // The ViewHolder, only one item for simplicity and demonstration purposes, you can put all the views inside a row of the list into this ViewHolder
    private static class ViewHolder {
        TextView name;
        //add other properties to see...

        TextView address;

        TextView coordinate;
        TextView engineType;
        TextView exterior;
        TextView fuel;
        TextView interior;
        TextView vin;

    }
}
