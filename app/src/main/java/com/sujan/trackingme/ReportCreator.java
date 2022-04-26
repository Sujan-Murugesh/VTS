package com.sujan.trackingme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.Date;

import static com.sujan.trackingme.R.*;

public class ReportCreator extends AppCompatActivity {

    Bundle bundle;
    String last_distance;

    Spinner spinner1,spinner2;
    EditText vehiclenumber,drivername,driverlicenceno,driveraddress,tripdistance,startaddress,endaddress,feul,reporttitle;

    TextView generate_pdf_btn;

    //array for spinners
    String[] vehiclelist;
    String[] feullist;

    String rtitle = "mileage_report";

    //adapter for assign spinner data
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    //database
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_report_creator);

        ActivityCompat.requestPermissions(ReportCreator.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        //TO assign all elements
        objectAssignment();

        //automatically get last distance
        bundle = getIntent().getExtras();
        last_distance = bundle.getString("DISTANCE");
        tripdistance.setText(last_distance);

        callOnClickListner();
    }

    private void callOnClickListner() {
        generate_pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()){
                    int tripnum = databaseHelper.getlastTrip();
                    rtitle = reporttitle.getText().toString();
                    String vnum = vehiclenumber.getText().toString();
                    String vtype = spinner1.getSelectedItem().toString();
                    String ftype = spinner2.getSelectedItem().toString();
                    String dname = drivername.getText().toString();
                    String lnum = driverlicenceno.getText().toString();
                    String dad = driveraddress.getText().toString();
                    String dis = tripdistance.getText().toString();
                    String fad = startaddress.getText().toString();
                    String tad = endaddress.getText().toString();
                    String fused = feul.getText().toString();
                    Date date = new Date();

                    //to insert report data
                    databaseHelper.insertReport(tripnum,rtitle,vnum,vtype,ftype,dname,lnum,dad,dis,fad,tad,fused,date.getTime());
                    Toast.makeText(getApplicationContext(),"Report saved successfully!",Toast.LENGTH_SHORT).show();

                    //get count of reports
                    int reportno = databaseHelper.getReportCount();

                    try {
                        new PrintPDF(reportno,tripnum,rtitle,vnum,vtype,ftype,dname,lnum,dad,dis,fad,tad,fused,date.getTime()).getPDF();
                        Toast.makeText(ReportCreator.this,"Report created Successfully!",Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(ReportCreator.this,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    //end of report
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please fill out fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkInputs() {
        if(!reporttitle.getText().toString().equals("") ||
                !vehiclenumber.getText().toString().equals("") ||
                !drivername.getText().toString().equals("") ||
                !driverlicenceno.getText().toString().equals("") ||
                !driveraddress.getText().toString().equals("") ||
                !tripdistance.getText().toString().equals("") ||
                !startaddress.getText().toString().equals("") ||
                !endaddress.getText().toString().equals("") ||
                !feul.getText().toString().equals("")
        ){
            return  true;
        }else
            return false;
    }

    private void objectAssignment() {
        //BUTTONS
        generate_pdf_btn = findViewById(id.save_pdf_btn);

        //SPINNERS
        spinner1 = findViewById(id.spinner1);
        spinner2 = findViewById(id.spinner2);

        //EDITTEXTS
        vehiclenumber = findViewById(id.vehicle_number);
        drivername = findViewById(id.driver_name);
        driverlicenceno = findViewById(id.driver_lic_number);
        driveraddress = findViewById(id.driver_address);

        tripdistance = findViewById(id.trip_distance);
        startaddress = findViewById(id.start_address);
        endaddress = findViewById(id.end_address);
        feul = findViewById(id.fuel_usage);
        reporttitle = findViewById(id.report_title);

        //to assign item list
        vehiclelist = new String[]{"Auto_Taxi","Car","Van","Bus","Three_Wheeler","Motorcycle","Scooter","Tractor","Crane","Bicycle"};
        feullist = new String[]{"Petrol","Diesel","Battery_electric","Plug_in_hybrid","Hybrid_electric","Natural_gas","Other"};

        //to add vehicles into spinner we need to use array  adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,vehiclelist);
        spinner1.setAdapter(adapter);

        //to add feuls into spinner we need to use array  adapter
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,feullist);
        spinner2.setAdapter(adapter2);

        databaseHelper = new DatabaseHelper(this);
    }

    public void openoldreport(View view) {
        //TODO CODE FOR OPEN OLD REPORTS
        Intent intent= new Intent(getApplicationContext(),OldReport.class);
        startActivity(intent);
    }

    public void autoLoadLast_TripData(View view) {
        //TODO AUTO LOAD LAST TRIP DATA
        Cursor rs = databaseHelper.getLastTripData();
        rs.moveToFirst();
        String starting_point = rs.getString(1);
        String end_point = rs.getString(2);
        String distance = rs.getString(3);

        tripdistance.setText(distance);
        startaddress.setText(starting_point);
        endaddress.setText(end_point);
        Toast.makeText(getApplicationContext(),"Data fetched successfully!",Toast.LENGTH_SHORT).show();
    }
}