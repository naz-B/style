package com.example.nazb.styleomega;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLoginClick(View v){
        EditText username = (EditText)findViewById(R.id.username);
        EditText pass = (EditText)findViewById(R.id.pass);

        String usernameS = username.getText().toString();
        String passS = pass.getText().toString();

        String[] projection = {
                Contract.Customers.Customer_pass
                };
        String selection = "email = '" + usernameS +"'";
        String checkPass = null;
        Cursor cursor = getContentResolver().query(Contract.Customers.CONTENT_URI, projection, selection , null, null);
        if (cursor != null && cursor.moveToFirst()) {
            checkPass = cursor.getString(cursor.getColumnIndex(Contract.Customers.Customer_pass));
            cursor.close();
        }
            if (passS.equals(checkPass)) {
                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                prefs.edit().putString("username", usernameS).apply();
                Intent intent = new Intent(Login.this, MainScreen.class);
                startActivity(intent);
            }

        else{
            Snackbar.make(findViewById(R.id.login), "Unsuccessful login", Snackbar.LENGTH_LONG).show();
        }
    }

    public void onRegisterClick(View v){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }


}
