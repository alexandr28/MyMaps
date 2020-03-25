package com.acampdev.mymaps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Maps extends Fragment implements OnMapReadyCallback{

    GoogleMap mMap;
    LocationManager locationManager;
    Marker marker;
    private static final int REQUEST_LOCATION_PERMISSION=1;
    LocationListener locationListener;

    public  static final String TAG="data";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity= getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_maps,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsG);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync((OnMapReadyCallback) this);

        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSION);
        }

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //obteniendo latitud & longitud
                    final double latitude= location.getLatitude();
                    final double longitude= location.getLongitude();
                    // instanciamos class LatLng
                    LatLng latLng= new LatLng(latitude,longitude);
                    // instanciamos the class GeoCorder

                    if(getActivity()!=null){
                        Geocoder geocoder= new Geocoder(getActivity(), Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                            final String dir = addressList.get(0).getAddressLine(0);

                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(dir));
                            CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(16).bearing(0).tilt(45).build();
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        } catch (IOException e){e.printStackTrace();}
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //obteniendo latitud & longitud
                    double latitude= location.getLatitude();
                    double longitude= location.getLongitude();
                    // instanciamos class LatLng
                    LatLng latLng= new LatLng(latitude,longitude);
                    // instanciamos the class GeoCorder

                    if(getActivity()!=null){
                        Geocoder geocoder= new Geocoder(getActivity());
                        try{
                            List<Address> addressList= geocoder.getFromLocation(latitude,longitude,1);
                            String dir= addressList.get(0).getAddressLine(0);
                            //dir+=addressList.get(0).getCountryName();
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(dir));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));
                            CameraPosition cameraPosition=CameraPosition.builder().target(latLng).zoom(16).bearing(0).tilt(45).build();
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            // Firebase Data

                        }catch (Exception e){e.printStackTrace();}
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
