package com.example.happybaby;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Baby> measurements;

    private BabyDAO measurementsDAO;

    Button add;

    PopupWindow popup;

    ListView listView;

    CustomMeasurementsList customMeasurementsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        measurementsDAO = new BabyDAO(getApplicationContext());
        listView = findViewById(R.id.list);
        add = findViewById(R.id.create_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPopup();
            }
        });
        measurements = (ArrayList<Baby>) measurementsDAO.readAll();
        //sukuriamas adapteris
        customMeasurementsList = new CustomMeasurementsList(MainActivity.this,measurements,measurementsDAO);
        listView.setAdapter(customMeasurementsList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You selected: "
                                +measurements.get(position).getBabyHeight()+" as height.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void addPopup() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.edit_popup,
                (ViewGroup) this.findViewById(R.id.popup));
        popup = new PopupWindow(layout, 600, 670, true);
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText height = layout.findViewById(R.id.edit_height);
        final EditText weight = layout.findViewById(R.id.edit_weight);
        Button save = layout.findViewById(R.id.save_popup);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getheight=height.getText().toString();
                String getweight=weight.getText().toString();
                Baby measurement1 = new Baby(Double.parseDouble(getheight), Double.parseDouble(getweight) );
                measurementsDAO.create(measurement1);

                if(customMeasurementsList==null){
                    customMeasurementsList =new CustomMeasurementsList((Activity) getApplicationContext(),
                            measurements, measurementsDAO);
                    listView.setAdapter(customMeasurementsList);
                }
                ArrayList<Baby> weathers1 = (ArrayList<Baby>) measurementsDAO.readAll();
                customMeasurementsList.setMeasurements(weathers1);

                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                popup.dismiss();
            }
        });
    }
}