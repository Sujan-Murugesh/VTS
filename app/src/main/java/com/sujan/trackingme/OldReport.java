package com.sujan.trackingme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class OldReport extends AppCompatActivity {

    Button printbtn;
    EditText editText;
    DataTable dataTable;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_report);

        printbtn = findViewById(R.id.old_printbtn);
        editText = findViewById(R.id.oldPrintEdit_text);
        dataTable = findViewById(R.id.data_table);
        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        DataTableHeader header = new DataTableHeader.Builder()
                .item("Rep.No",6)
                .item("Driver Name",5)
                .item("Date",5)
                .build();

        String[] columns ={"id","driv_name","date"};
        Cursor cursor = sqLiteDatabase.query("report",columns,null,null,null,null,null);

        ArrayList<DataTableRow> rows = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            for (int i=0; i<cursor.getCount(); i++){
                cursor.moveToNext();
                DataTableRow row = new DataTableRow.Builder()
                        .value(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))))
                        .value(cursor.getString(cursor.getColumnIndex("driv_name")))
                        .value(dateFormat.format(cursor.getLong(cursor.getColumnIndex("date"))))
                        .build();
                rows.add(row);
            }
        }

        dataTable.setHeader(header);
        dataTable.setRows(rows);
        dataTable.inflate(this);

        //selected report
        printSelectedReport();

    }

    private void printSelectedReport() {
        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int repNoforPrint = Integer.parseInt(editText.getText().toString());
                Cursor c = sqLiteDatabase.rawQuery("select * from report where id ="+repNoforPrint,null);
                c.moveToNext();

                try {
                    new PrintPDF(c.getInt(0),c.getInt(1),c.getString(2),c.getString(3),
                            c.getString(4),c.getString(5),c.getString(6),c.getString(7),
                            c.getString(8),c.getString(9),c.getString(10),c.getString(11),
                            c.getString(12),c.getLong(13)).getPDF();

                    Toast.makeText(getApplicationContext(),c.getString(2)+" is recreated!",Toast.LENGTH_LONG).show();
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void openDialog(View view) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Please Select any number from the table and type into the field. after typing the report number, You can just click print button to re generate the report!");
        dialog.setTitle("How to print old report?");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(),"Thank you, Have a good job!",Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}