package com.sujan.trackingme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocationActivity extends AppCompatActivity {

    //initializing variables
    TextView btLocation,start_btn,stoptrip_btn,buttonreport;
    TextView textView1, textView2, textView3, textView4,details_header;
    EditText textView5,display;
    FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseHelper databaseHelper;
    double latti1=0, long1 = 0, latti2 =0, long2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        databaseHelper = new DatabaseHelper(this);

        //assign variables
        btLocation = findViewById(R.id.btn_location);
        start_btn = findViewById(R.id.startbtn);
        stoptrip_btn = findViewById(R.id.stoptripbtn);
        buttonreport = findViewById(R.id.button_report);
        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        textView4 = findViewById(R.id.text_view4);
        textView5 = findViewById(R.id.text_view5);
        display = findViewById(R.id.output_display);
        details_header = findViewById(R.id.details_head);

        //initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //100% working
        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permissions
                if (ActivityCompat.checkSelfPermission(CurrentLocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //where permission granded
                    getLocation();
                    details_header.setText("Starting point details");
                } else {
                    ActivityCompat.requestPermissions(CurrentLocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView1.getText().equals("") || textView2.getText().equals("")){
                    //to do empty text
                    Toast.makeText(getApplicationContext(),"Please Get the location first",Toast.LENGTH_SHORT).show();
                }
                else{

                    String latitudeValue = textView1.getText().toString();
                    String longitudeValue = textView2.getText().toString();
                    String addressValue = textView5.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("latitude",latitudeValue);
                    contentValues.put("longitude",longitudeValue);
                    contentValues.put("address",addressValue);

                    databaseHelper.insertLocation1(contentValues);
                    Toast.makeText(getApplicationContext(),"Start poit details saved",Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),ReportCreator.class);
                Bundle bundle = new Bundle();
                bundle.putString("DISTANCE",display.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        stoptrip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.get end point location
                //check permissions
                if (ActivityCompat.checkSelfPermission(CurrentLocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //where permission granded
                    getLocation();
                    details_header.setText("End point details");

                    //store the details on  db
                    if(textView1.getText().equals("") || textView2.getText().equals("")){
                        //to do empty text
                        Toast.makeText(getApplicationContext(),"Sorry something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String latitudeValue = textView1.getText().toString();
                        String longitudeValue = textView2.getText().toString();
                        String addressValue = textView5.getText().toString();

                        try {
                            latti2 = Double.parseDouble(textView1.getText().toString());
                            long2 = Double.parseDouble(textView2.getText().toString());
                        }catch (NumberFormatException e){
                            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("latitude",latitudeValue);
                        contentValues.put("longitude",longitudeValue);
                        contentValues.put("address",addressValue);
                        databaseHelper.insertLocation2(contentValues);
                        Toast.makeText(getApplicationContext(),"End point data saved",Toast.LENGTH_SHORT).show();

                        //to get start point data from the db
                        Cursor rs = databaseHelper.getLocation1();
                        rs.moveToFirst();
                        String lt1 = rs.getString(1);
                        String lg1 = rs.getString(2);
                        String adres1 = rs.getString(3);

                        //convert data to double
                        try {
                            latti1 = Double.parseDouble(lt1);
                            long1 = Double.parseDouble(lg1);
                        }catch (NumberFormatException e){
                            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        //calculate the distance
                        distance(latti1,latti2,long1,long2);

                        //insertTrips
                        String dis = display.getText().toString();

                        databaseHelper.inserting_tripdata(adres1,addressValue,dis);
                        //Toast.makeText(getApplicationContext(),adres1+dis+" "+addressValue,Toast.LENGTH_LONG).show();
                    }

                } else {
                    ActivityCompat.requestPermissions(CurrentLocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }



    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //nitialize location
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(CurrentLocationActivity.this, Locale.getDefault());

                    //initialize
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        //set latitude on text view
                        textView1.setText(String.valueOf(addresses.get(0).getLatitude()));
//                        textView1.setText(Html.fromHtml(
//                                "<font color='#6200EE'><b>Latitude :</b><br></font>"
//                                        + addresses.get(0).getLatitude()
//
//                        ));

                        //set longitude
                        textView2.setText(String.valueOf(addresses.get(0).getLongitude()));
//                        textView2.setText(Html.fromHtml(
//                                "<font color='#6200EE'><b>Longitude :</b><br></font>"
//                                        + addresses.get(0).getLongitude()
//
//                        ));
                        //set country name
                        textView3.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Country Name:</b><br></font>"
                                        + addresses.get(0).getCountryName()

                        ));

                        //set locality
                        textView4.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Locality:</b><br></font>"
                                        + addresses.get(0).getLocality()

                        ));

                        //set Address
                        textView5.setText(addresses.get(0).getAddressLine(0));
//                        textView5.setText(Html.fromHtml(
//                                "<font color='#6200EE'><b>Address:</b><br></font>"
//                                        + addresses.get(0).getAddressLine(0)
//
//                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //calculate distance
    private void distance(double latti1, double latti2, double long1, double long2) {

        double longDiff = long1-long2;

        //calculate distance
        double distance = Math.sin(deg2rad(latti1))
                * Math.sin(deg2rad(latti2))
                + Math.cos(deg2rad(latti1))
                * Math.cos(deg2rad(latti2))
                * Math.cos(deg2rad(longDiff));

        distance = Math.acos(distance);
        //convert distance radian to degree
        distance = rag2deg(distance);

        //distance in miles
        distance = distance * 60 * 1.1515;

        //distance in kilometers
        distance = distance * 1.609344;

        //t5.setText(String.valueOf(distance));
        display.setText(String.format(Locale.US,"%2f ",distance));

    }

    private double rag2deg(double distance) {
        return  (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double latti1) {
        return (latti1*Math.PI/180.0);
    }



}
