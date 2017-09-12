package com.example.nazb.styleomega;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAddress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Shipping Address");
        Button addButton = (Button)findViewById(R.id.add);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        final String username = prefs.getString("username", null);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText houseno = (EditText)findViewById(R.id.houseno);
                EditText mainroad = (EditText)findViewById(R.id.mainst);
                EditText subroad = (EditText)findViewById(R.id.subroad);
                EditText landmark = (EditText)findViewById(R.id.landmark);
                EditText city = (EditText)findViewById(R.id.city);
                EditText contactno = (EditText)findViewById(R.id.contact);

                String housenoString = houseno.getText().toString();
                String mainroadString = mainroad.getText().toString();
                String subroadString = subroad.getText().toString();
                String landmarkString = landmark.getText().toString();
                String cityString = city.getText().toString();
                String contactNoString = contactno.getText().toString();



                ContentValues values = new ContentValues();
                values.put(Contract.Address.Address_HouseNo, housenoString);
                values.put(Contract.Address.Address_MainRoad, mainroadString);
                values.put(Contract.Address.Address_SubRoad, subroadString);
                values.put(Contract.Address.Address_LandMark, landmarkString);
                values.put(Contract.Address.Address_City, cityString);
                values.put(Contract.Address.Address_ContactNo, contactNoString);
                values.put(Contract.Address.Address_Username, username);
                getContentResolver().insert(Contract.Address.CONTENT_URI, values);
                Intent intent = new Intent(AddAddress.this, ManageAccount.class);
                startActivity(intent);
            }
        });
    }

}
