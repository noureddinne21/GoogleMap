package com.nouroeddinne.googlemap;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
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

        mf1 = mMap.addMarker(new MarkerOptions().position(f1).title("F1").icon(BitmapFromVector(getApplicationContext(),R.drawable.placeholder)));
        mf1.setTag(0);
        list.add(mf1);

        mf2 = mMap.addMarker(new MarkerOptions().position(f2).title("F2").icon(BitmapFromVector(getApplicationContext(),R.drawable.placeholder)));
        mf2.setTag(0);
        list.add(mf2);

        mf3 = mMap.addMarker(new MarkerOptions().position(f3).title("F3").icon(BitmapFromVector(getApplicationContext(),R.drawable.placeholder)));
        mf3.setTag(0);
        list.add(mf3);

        mf4 = mMap.addMarker(new MarkerOptions().position(f4).title("F4").icon(BitmapFromVector(getApplicationContext(),R.drawable.placeholder)));
        mf4.setTag(0);
        list.add(mf4);

        mf5 = mMap.addMarker(new MarkerOptions().position(f5).title("F5").icon(BitmapFromVector(getApplicationContext(),R.drawable.placeholder)));
        mf5.setTag(0);
        list.add(mf5);



        for (Marker m : list){
            LatLng latLng = new LatLng(m.getPosition().latitude,m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title(m.getTitle()).icon(BitmapFromVector(getApplicationContext(),R.drawable.placeholder)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }









    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {

        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(
                context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(
                0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}