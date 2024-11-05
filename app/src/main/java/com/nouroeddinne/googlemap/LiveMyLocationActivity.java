package com.nouroeddinne.googlemap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.nouroeddinne.googlemap.databinding.ActivityLiveMyLocationBinding;
import com.nouroeddinne.googlemap.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LiveMyLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityLiveMyLocationBinding binding;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLiveMyLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }

        googleMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (addressList != null && addressList.size() > 0){
                        Log.d("geocoder", "onLocationChanged: "+
                                addressList.get(0).getAddressLine(0).toString()+" getAdminArea "+
                                addressList.get(0).getAdminArea().toString()+" getLocality "+
                                addressList.get(0).getLocality().toString()+" getCountryCode "+
                                addressList.get(0).getCountryCode().toString()+" getCountryName "+
                                addressList.get(0).getCountryName().toString()+" getPostalCode "+
                                addressList.get(0).getPostalCode() +" getPremises "+
                                addressList.get(0).getPremises()+" getPhone "+
                                addressList.get(0).getPhone()+" getMaxAddressLineIndex "+
                                addressList.get(0).getMaxAddressLineIndex()+" getLocale "+
                                addressList.get(0).getLocale().toString()+" getExtras "+
                                addressList.get(0).getExtras()+" getUrl "+
                                addressList.get(0).getUrl()+" getThoroughfare "+
                                addressList.get(0).getThoroughfare()+" getSubThoroughfare "+
                                addressList.get(0).getSubThoroughfare()+" getSubLocality "+
                                addressList.get(0).getSubLocality()+" getSubAdminArea "+
                                addressList.get(0).getSubAdminArea()+" getFeatureName "+
                                addressList.get(0).getFeatureName());
                    }else{
                        Log.d("geocoder", "no address !");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10,locationListener);
        
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10,locationListener);
            }
        }
    }






















}