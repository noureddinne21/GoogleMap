package com.nouroeddinne.googlemap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nouroeddinne.googlemap.databinding.ActivityDrawingRouteMapsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawingRouteMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityDrawingRouteMapsBinding binding;
    ArrayList markerPoints= new ArrayList();
    private LatLng origin;
    private LatLng destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrawingRouteMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

        if (destination!=null){
            origin=null;
            destination=null;
            mMap.clear();
        }else {
            if (origin==null){
                origin=latLng;
                mMap.addMarker(new MarkerOptions().position(origin).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            }else{
                destination=latLng;
                mMap.addMarker(new MarkerOptions().position(destination).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
                drawRoute(origin, destination);
            }
        }

    }


    private void drawRoute(LatLng origin, LatLng destination) {
        String url = getDirectionsUrl(origin, destination);
        new FetchUrl().execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        String parameters = str_origin + "&" + str_dest + "&sensor=false&mode=driving&&alternatives";
        return "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=AIzaSyASgNQd06LXXRHYR16qSdmlIe3eunxVFDQ";
    }

    // URL encoding helper method
    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Handle error if encoding fails
            e.printStackTrace();
            return value; // Return the original value if encoding fails
        }
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        private String downloadUrl(String strUrl) throws Exception {
            StringBuilder data = new StringBuilder();
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            br.close();
            return data.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            try {
                JSONObject jObject = new JSONObject(jsonData[0]);
                JSONArray jRoutes = jObject.getJSONArray("routes");
                for (int i = 0; i < jRoutes.length(); i++) {
                    JSONArray jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List<HashMap<String, String>> path = new ArrayList<>();
                    for (int j = 0; j < jLegs.length(); j++) {
                        JSONArray jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).getString("points");
                            List<LatLng> list = decodePoly(polyline);
                            for (LatLng l : list) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString(l.latitude));
                                hm.put("lng", Double.toString(l.longitude));
                                path.add(hm);
                            }
                        }
                    }
                    routes.add(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            PolylineOptions lineOptions = new PolylineOptions();
            for (List<HashMap<String, String>> path : result) {
                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng latLng = new LatLng(lat, lng);
                    lineOptions.add(latLng);
                }
            }
            lineOptions.width(10);
            lineOptions.color(Color.BLUE);
            mMap.addPolyline(lineOptions);

            // Move camera to the starting point
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,30));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));

        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result >> 1) ^ -(result & 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result >> 1) ^ -(result & 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }
        return poly;
    }
}