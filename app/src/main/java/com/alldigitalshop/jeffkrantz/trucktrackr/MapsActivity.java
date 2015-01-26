package com.alldigitalshop.jeffkrantz.trucktrackr;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.alldigitalshop.jeffkrantz.trucktrackr.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements AsyncResponse  {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        actionBar.setCustomView(cView);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        setCamera();

        getTrucks();
    }

    private void getTrucks(){
        DownloadTask dlTask = new DownloadTask();
        dlTask.execute("ya");
        dlTask.delegate = this;
    }

    private void setTrucks(JSONObject truck){
        try{
            double latitude = ((Number)truck.get("latitude")).doubleValue();
            double longitude = ((Number)truck.get("longitude")).doubleValue();
            String  name = truck.getString("name");
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                    .title(name)
                    .icon(BitmapDescriptorFactory.fromAsset("truck.png")));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
//        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, -87.6297982))
//                .title("Soups in the Loop")
//                .icon(BitmapDescriptorFactory.fromAsset("truck.png")));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(41.8722222, -87.6275211))
//                .title("Wow Bao")
//                .icon(BitmapDescriptorFactory.fromAsset("truck.png")));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(41.8719872, -87.6244261))
//                .title("Beaver Donuts")
//                .icon(BitmapDescriptorFactory.fromAsset("truck.png")));
    }

    private void setCamera(){
        LatLng CHICAGO = new LatLng(41.8781136, -87.6297982);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(CHICAGO)
                .zoom(14)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void processFinish(String output){
        Log.e("log_tag", output);
        try {
//            JSONObject jObject = new JSONObject(output);
            JSONArray jArray = new JSONArray(output);

            ArrayList<String> list = new ArrayList<String>();
            if (jArray != null) {
                int len = jArray.length();
                for (int i=0;i<len;i++){
                    list.add(jArray.get(i).toString());
                }
            }

            for(Integer i = 0; i <= list.size() - 1; i++)
            {
                JSONObject jsonObject = new JSONObject(list.get(i));
                setTrucks(jsonObject);
            }

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        //this you will received result fired from async class of onPostExecute(result) method.
    }
}
