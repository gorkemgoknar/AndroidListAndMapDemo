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

/**
 * Adapter to fill list view with Placemark model
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
            viewHolder.exterior = (TextView) convertView.findViewById(R.id.row_exterior);
            viewHolder.interior = (TextView) convertView.findViewById(R.id.row_interior);
            viewHolder.fuel = (TextView) convertView.findViewById(R.id.row_fuel);
            viewHolder.address = (TextView) convertView.findViewById(R.id.row_address);
            viewHolder.vin = (TextView) convertView.findViewById(R.id.row_vin);

            //what to show
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.name.setText(placemark.getName());
        viewHolder.coordinate.setText(placemark.getCoordinateAsString());
        viewHolder.engineType.setText(placemark.getEngineType());
        viewHolder.vin.setText(placemark.getVin());
        viewHolder.exterior.setText(placemark.getExterior());
        viewHolder.interior.setText(placemark.getInterior());
        viewHolder.address.setText(placemark.getAddress());
        viewHolder.fuel.setText(placemark.getFuel().toString());

        // Return the completed view to render on screen
        return convertView;
    }

    // The ViewHolder what is to be shown

    private static class ViewHolder {
        TextView name;
        TextView address;
        TextView coordinate;
        TextView engineType;
        TextView exterior;
        TextView interior;
        TextView fuel;
        TextView vin;

    }
}
