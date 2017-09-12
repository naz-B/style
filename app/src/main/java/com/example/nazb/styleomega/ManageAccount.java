package com.example.nazb.styleomega;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ManageAccount extends AppCompatActivity {
    String fname, lname = null;
    private PopupWindow popwindow;
    private ListView listView;
    private AddressList listViewAdapter;
    private List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage Account");
        viewData();
        viewShippingAddresses();

    }

    public void viewData() {
        TextView fnameTitle = (TextView) findViewById(R.id.fnameTitle);
        fnameTitle.setText("First Name");
        TextView lnameTitle = (TextView) findViewById(R.id.lnameTitle);
        lnameTitle.setText("Last Name");
        TextView passTitle = (TextView) findViewById(R.id.passTitle);
        passTitle.setText("Password");
        TextView passData = (TextView) findViewById(R.id.passData);
        passData.setText("Change Password");
        TextView emailTitle = (TextView) findViewById(R.id.emailTitle);
        emailTitle.setText("Email");
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String[] projection = {
                Contract.Customers.Customer_fname,
                Contract.Customers.Customer_lname};
        String selection = "email = '" + prefs.getString("username", null) + "'";

        Cursor cursor = getContentResolver().query(Contract.Customers.CONTENT_URI, projection, selection, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            fname = cursor.getString(cursor.getColumnIndex(Contract.Customers.Customer_fname));
            lname = cursor.getString(cursor.getColumnIndex(Contract.Customers.Customer_lname));
            TextView fnameData = (TextView) findViewById(R.id.fnameData);
            TextView lnameData = (TextView) findViewById(R.id.lnameData);
            fnameData.setText(fname);
            lnameData.setText(lname);
        }
        TextView emailData = (TextView) findViewById(R.id.emailData);
        emailData.setText(prefs.getString("username", null));

    }

    public void setPopUpWindow(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_manageaccounts, null);
        popwindow = new PopupWindow(container, 800, 800, true);
        popwindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        EditText popText = (EditText) popwindow.getContentView().findViewById(R.id.popTextManageAccount);
        Button popbutton = (Button) popwindow.getContentView().findViewById(R.id.popButton);
        if (v.getId() == R.id.fnameData) {
            popText.setText(fname);
            popbutton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) popwindow.getContentView().findViewById(R.id.popTextManageAccount);
                    String popText = editText.getText().toString();
                    SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                    ContentValues values = new ContentValues();
                    values.put(Contract.Customers.Customer_fname, popText);
                    String selection = "email = '" + prefs.getString("username", null) + "'";
                    getContentResolver().update(Contract.Customers.CONTENT_URI, values, selection, null);
                    Intent intent = new Intent(ManageAccount.this, ManageAccount.class);
                    startActivity(intent);

                }
            });
        } else if (v.getId() == R.id.lnameData) {
            popText.setText(lname);
            popbutton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) popwindow.getContentView().findViewById(R.id.popTextManageAccount);
                    String popText = editText.getText().toString();
                    SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                    ContentValues values = new ContentValues();
                    values.put(Contract.Customers.Customer_lname, popText);
                    String selection = "email = '" + prefs.getString("username", null) + "'";
                    getContentResolver().update(Contract.Customers.CONTENT_URI, values, selection, null);
                    Intent intent = new Intent(ManageAccount.this, ManageAccount.class);
                    startActivity(intent);

                }
            });

        }
    }

    public void setPopUpWindowForPassword(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_changepass, null);
        popwindow = new PopupWindow(container, 900, 900, true);
        popwindow.setOutsideTouchable(true);
        popwindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        EditText popPass = (EditText) popwindow.getContentView().findViewById(R.id.popPass);
        EditText popRePass = (EditText) popwindow.getContentView().findViewById(R.id.popRePass);
        Button popbutton = (Button) popwindow.getContentView().findViewById(R.id.popButton1);
        if (v.getId() == R.id.passData) {

            popbutton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) popwindow.getContentView().findViewById(R.id.popPass);
                    EditText editText1 = (EditText) popwindow.getContentView().findViewById(R.id.popRePass);
                    String pass = editText.getText().toString();
                    String repass = editText1.getText().toString();
                    if (pass.equals(repass)) {
                        String popText = editText.getText().toString();
                        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                        ContentValues values = new ContentValues();
                        values.put(Contract.Customers.Customer_pass, popText);
                        String selection = "email = '" + prefs.getString("username", null) + "'";
                        getContentResolver().update(Contract.Customers.CONTENT_URI, values, selection, null);
                        Intent intent = new Intent(ManageAccount.this, ManageAccount.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ManageAccount.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                    }


                }
            });
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navmenu, menu);
        inflater.inflate(R.menu.search_menu, menu);
        inflater.inflate(R.menu.appbarbuttons, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(ManageAccount.this, SearchResult.class);
                intent.putExtra("Query", query);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        switch (item.getItemId()) {
            case R.id.action_share:

                break;
            case R.id.my_home: {
                Intent intent = new Intent(ManageAccount.this, MainScreen.class);
                startActivity(intent);
                break;
            }
            case R.id.my_account: {
                if (username != null) {
                    Intent intent = new Intent(ManageAccount.this, ManageAccount.class);
                    startActivity(intent);
                    break;
                } else {
                    Intent intent = new Intent(ManageAccount.this, Login.class);
                    startActivity(intent);
                    break;
                }

            }
            case R.id.favorites: {
                Intent intent = new Intent(ManageAccount.this, Favourites.class);
                startActivity(intent);
                break;
            }
            case R.id.orders: {
                Intent intent = new Intent(ManageAccount.this, Orders.class);
                startActivity(intent);
                break;
            }
            case R.id.cart:
                Intent intent = new Intent(ManageAccount.this, ShoppingCart.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    public void viewShippingAddresses() {
        listView = (ListView) findViewById(R.id.shipping_addresses);
        addressList = new ArrayList<>();
        String[] projection = {
                Contract.Address.Address_HouseNo,
                Contract.Address.Address_MainRoad,
                Contract.Address.Address_SubRoad,
                Contract.Address.Address_LandMark,
                Contract.Address.Address_City,
                Contract.Address.Address_ContactNo

        };
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String selection = "username = '" + prefs.getString("username", null) + "'";
        Cursor cursor = getContentResolver().query(Contract.Address.CONTENT_URI, projection, selection, null, null);
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                    addressList.add(new Address(
                            1,
                            cursor.getString(cursor.getColumnIndex(Contract.Address.Address_HouseNo)),
                            cursor.getString(cursor.getColumnIndex(Contract.Address.Address_MainRoad)),
                            cursor.getString(cursor.getColumnIndex(Contract.Address.Address_SubRoad)),
                            cursor.getString(cursor.getColumnIndex(Contract.Address.Address_LandMark)),
                            cursor.getString(cursor.getColumnIndex(Contract.Address.Address_City)),
                            cursor.getString(cursor.getColumnIndex(Contract.Address.Address_ContactNo))
                    ));

        }
        cursor.close();
        listViewAdapter = new AddressList(this, R.layout.address_list, addressList);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = (TextView) view.findViewById(R.id.txtHouse);
                final String text = textView.getText().toString();
                String[] projection = {
                        Contract.Address.Address_HouseNo,
                        Contract.Address.Address_MainRoad,
                        Contract.Address.Address_SubRoad,
                        Contract.Address.Address_LandMark,
                        Contract.Address.Address_City,
                        Contract.Address.Address_ContactNo

                };
                String selection = "house_no = '" + text + "'";
                Cursor cursor = getContentResolver().query(Contract.Address.CONTENT_URI, projection, selection, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_address, null);
                    popwindow = new PopupWindow(container, 1500, 1540, true);
                    popwindow.showAtLocation(container, Gravity.CENTER, 0, 0);
                    EditText houseno = (EditText)popwindow.getContentView().findViewById(R.id.houseNo);
                    EditText mainroad = (EditText)popwindow.getContentView().findViewById(R.id.mainRoad);
                    EditText subroad = (EditText)popwindow.getContentView().findViewById(R.id.subRoad);
                    EditText landmark = (EditText)popwindow.getContentView().findViewById(R.id.landMark);
                    EditText city = (EditText)popwindow.getContentView().findViewById(R.id.City);
                    EditText contactno = (EditText)popwindow.getContentView().findViewById(R.id.ContactNo);
                    houseno.setText(cursor.getString(cursor.getColumnIndex(Contract.Address.Address_HouseNo)));
                    mainroad.setText(cursor.getString(cursor.getColumnIndex(Contract.Address.Address_MainRoad)));
                    subroad.setText(cursor.getString(cursor.getColumnIndex(Contract.Address.Address_SubRoad)));
                    landmark.setText(cursor.getString(cursor.getColumnIndex(Contract.Address.Address_LandMark)));
                    city.setText(cursor.getString(cursor.getColumnIndex(Contract.Address.Address_City)));
                    contactno.setText(cursor.getString(cursor.getColumnIndex(Contract.Address.Address_ContactNo)));

                    Button popbutton = (Button) popwindow.getContentView().findViewById(R.id.changeAdd);
                    popbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText houseno = (EditText)popwindow.getContentView().findViewById(R.id.houseNo);
                            EditText mainroad = (EditText)popwindow.getContentView().findViewById(R.id.mainRoad);
                            EditText subroad = (EditText)popwindow.getContentView().findViewById(R.id.subRoad);
                            EditText landmark = (EditText)popwindow.getContentView().findViewById(R.id.landMark);
                            EditText city = (EditText)popwindow.getContentView().findViewById(R.id.City);
                            EditText contactno = (EditText)popwindow.getContentView().findViewById(R.id.ContactNo);

                            final String housenoString = houseno.getText().toString();
                            final String mainroadString = mainroad.getText().toString();
                            final String subroadString = subroad.getText().toString();
                            final String landmarkString = landmark.getText().toString();
                            final String cityString = city.getText().toString();
                            final String contactNoString = contactno.getText().toString();
                            ContentValues values = new ContentValues();
                            values.put(Contract.Address.Address_HouseNo, housenoString);
                            values.put(Contract.Address.Address_MainRoad, mainroadString);
                            values.put(Contract.Address.Address_SubRoad, subroadString);
                            values.put(Contract.Address.Address_LandMark, landmarkString);
                            values.put(Contract.Address.Address_City, cityString);
                            values.put(Contract.Address.Address_ContactNo, contactNoString);
                            String selection = "house_no = '" + text + "'";
                            getContentResolver().update(Contract.Address.CONTENT_URI, values, selection, null);
                            Intent intent = new Intent(ManageAccount.this, ManageAccount.class);
                            startActivity(intent);
                        }
                    });
                }




                Toast.makeText(ManageAccount.this, text, Toast.LENGTH_LONG).show();
            }
        });


    }

    public void addAddress(View v){
        Intent intent = new Intent(ManageAccount.this, AddAddress.class);
        startActivity(intent);
    }


}
