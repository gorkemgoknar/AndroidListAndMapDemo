package com.example.gorkemgoknar.carmapdemo.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.gorkemgoknar.carmapdemo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

/**
 *  Adapter to show custom Info Window on map marker click
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        this.mContext = context;
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_map_info_window,null);

    }

    private void renderWindowText(Marker marker,View view){
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.map_info_window_title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        /*
        //Other information can also be added provided view exists
        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.map_info_window_detail);

        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }

        */

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);

        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);

        return mWindow;
    }
}
