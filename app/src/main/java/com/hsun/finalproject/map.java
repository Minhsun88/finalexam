package com.hsun.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hsun.finalproject.databinding.ActivityMapBinding;

public class map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private String[] format = {"一般","衛星","地形","混合"};
    private String[] Examplace = {"台南考區","台中考區","台北考區"};
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> adap1= new ArrayAdapter<String>(map.this,
                android.R.layout.simple_list_item_1,Examplace);
        binding.sp1.setAdapter(adap1);

        ArrayAdapter<String> adap2 = new ArrayAdapter<String>(map.this,
                android.R.layout.simple_list_item_1,format);
        binding.sp2.setAdapter(adap2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                {
                    mMap.clear();

                    db.collection("Examplace")
                            .whereEqualTo("type","台南")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        LatLng Last = null;
                                        for(QueryDocumentSnapshot doc : task.getResult()){
                                            String title = doc.getString("title");
                                            GeoPoint GPS = doc.getGeoPoint("GPS");

                                            LatLng Tainan = new LatLng(GPS.getLatitude(),GPS.getLongitude());
                                            mMap.addMarker(new MarkerOptions().position(Tainan).title(title));
                                            Last = Tainan;
                                        }
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(Last).zoom(15).build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(map.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else if(i==1){
                    mMap.clear();

                    db.collection("Examplace")
                            .whereEqualTo("type","台中")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        LatLng Last = null;
                                        for(QueryDocumentSnapshot doc : task.getResult()){
                                            String title = doc.getString("title");
                                            GeoPoint GPS = doc.getGeoPoint("GPS");

                                            LatLng Taichun = new LatLng(GPS.getLatitude(),GPS.getLongitude());
                                            mMap.addMarker(new MarkerOptions().position(Taichun).title(title));
                                            Last = Taichun;
                                        }
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(Last).zoom(15).build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(map.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else if(i==2){
                    mMap.clear();

                    db.collection("Examplace")
                            .whereEqualTo("type","台北")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        LatLng Last = null;
                                        for(QueryDocumentSnapshot doc : task.getResult()){
                                            String title = doc.getString("title");
                                            GeoPoint GPS = doc.getGeoPoint("GPS");

                                            LatLng Taipei = new LatLng(GPS.getLatitude(),GPS.getLongitude());
                                            mMap.addMarker(new MarkerOptions().position(Taipei).title(title));
                                            Last = Taipei;
                                        }
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(Last).zoom(12).build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(map.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                else if(i == 1)
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                else if(i == 2)
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                else if(i == 3)
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                new AlertDialog.Builder(map.this)
                        .setTitle(marker.getTitle())
                        .setMessage(marker.getSnippet())
                        .setPositiveButton("OK",null)
                        .show();

                return false;
            }
        });
        LatLng house = new LatLng(22.976804559163007, 120.24677263878743);
        mMap.addMarker(new MarkerOptions().position(house).title("House").snippet("虎尾寮"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(house).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}