package com.nouroeddinne.googlemap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nouroeddinne.googlemap.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LatLng  f1 = new LatLng(40.7058094,-73.9985485);
    private LatLng  f2 = new LatLng(40.702550, -73.996362);
    private LatLng  f3 = new LatLng(40.7034205,-73.9947206);
    private LatLng  f4 = new LatLng(40.7020296,-73.9893455);
    private LatLng  f5 = new LatLng(40.7003297,-73.9914912);

    private Marker mf1,mf2,mf3,mf4,mf5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<Marker> list = new ArrayList<Marker>();

        mf1 = mMap.addMarker(new MarkerOptions().position(f1).title("F1"));
        mf1.setTag(0);
        list.add(mf1);

        mf2 = mMap.addMarker(new MarkerOptions().position(f2).title("F2"));
        mf2.setTag(0);
        list.add(mf2);

        mf3 = mMap.addMarker(new MarkerOptions().position(f3).title("F3"));
        mf3.setTag(0);
        list.add(mf3);

        mf4 = mMap.addMarker(new MarkerOptions().position(f4).title("F4"));
        mf4.setTag(0);
        list.add(mf4);

        mf5 = mMap.addMarker(new MarkerOptions().position(f5).title("F5"));
        mf5.setTag(0);
        list.add(mf5);

        for (Marker m : list){
            LatLng latLng = new LatLng(m.getPosition().latitude,m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title(m.getTitle()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }












    }
}