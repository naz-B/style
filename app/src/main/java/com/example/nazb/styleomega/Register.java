package com.example.nazb.styleomega;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
    }

    public void onCreateAccount(View v){
        if (v.getId() == R.id.register){
            EditText fname = (EditText)findViewById(R.id.fname);
            EditText lname = (EditText)findViewById(R.id.lname);
            EditText username = (EditText)findViewById(R.id.username);
            EditText pass = (EditText)findViewById(R.id.pass);
            EditText repass = (EditText)findViewById(R.id.repass);

            String fnameS = fname.getText().toString();
            String lnameS = lname.getText().toString();
            String usernameS = username.getText().toString();
            String passS = pass.getText().toString();
            String repassS = repass.getText().toString();

            if(fnameS.equals(null) || fnameS.equals("") || lnameS.equals(null) || lnameS.equals("")
                    || usernameS.equals(null) || usernameS.equals("") || passS.equals(null) || passS.equals("") ){
                Snackbar.make(findViewById(R.id.reg), "Please enter valid details", Snackbar.LENGTH_LONG).show();
            }
            if(!passS.equals(repassS)){
                Snackbar.make(findViewById(R.id.reg), "Passwords don't match", Snackbar.LENGTH_LONG).show();
            }
            else{
                ContentValues values = new ContentValues();
                values.put(Contract.Customers.Customer_fname, fnameS);
                values.put(Contract.Customers.Customer_lname, lnameS);
                values.put(Contract.Customers.Customer_email, usernameS);
                values.put(Contract.Customers.Customer_pass, passS);
                getContentResolver().insert(Contract.Customers.CONTENT_URI,values);


            }
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        }
    }


}
