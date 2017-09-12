package com.example.nazb.styleomega;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ServerSync extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_sync);
    }

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());
    }

    public ArrayList<Customer> getAllUsers() {


        ArrayList<Customer> customers = new ArrayList<Customer>();



        String[] projection = {
                Contract.Customers._ID,
                Contract.Customers.Customer_fname,
                Contract.Customers.Customer_lname,
                Contract.Customers.Customer_email,
                Contract.Customers.Customer_pass

        };
        // String selection = "username = '" + username +"'";
        Cursor cursor = getContentResolver().query(Contract.Customers.CONTENT_URI, projection, null , null, null);

        //String selectQuery = "SELECT  * FROM Customers";


        // Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Customer customer = new Customer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                customers.add(customer);

            } while (cursor.moveToNext());

        }

        cursor.close();

        return customers;

    }

    public String composeJSONfromSQLite() {
        // MyDBHandler dbHelper = new MyDBHandler(this.getApplicationContext());

        ArrayList<Customer> customers = new ArrayList<Customer>();
        String status = "no";
        String[] projection = {
                Contract.Customers._ID,
                Contract.Customers.Customer_fname,
                Contract.Customers.Customer_lname,
                Contract.Customers.Customer_email,
                Contract.Customers.Customer_pass

        };
        String selection = "SyncStatus = '" + status +"'";
        Cursor cursor = getContentResolver().query(Contract.Customers.CONTENT_URI, projection, selection , null, null);

        //String selectQuery = "SELECT  * FROM Customers where SyncStatus = '" + "no" + "'";

        // SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Customer customer = new Customer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                customers.add(customer);

            } while (cursor.moveToNext());

        }

        cursor.close();

        Gson gson = new GsonBuilder().create();

        //Use GSON to serialize Array List to JSON

        return gson.toJson(customers);

    }


    public String getSyncStatus() {

        String msg = null;

        if (this.dbSyncCount() == 0) {
            msg = "SQLite and Remote MySQL DBs are in Sync!";

        } else {

            msg = "DB Sync needed";

        }

        return msg;

    }

    public int dbSyncCount() {

        int count = 0;

        // String selectQuery = "SELECT  * FROM Customers where SyncStatus = '" + "no" + "'";
        // MyDBHandler dbHelper = new MyDBHandler(this.getApplicationContext());

        // SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] projection = {
                Contract.Customers._ID,
                Contract.Customers.Customer_fname,
                Contract.Customers.Customer_lname,
                Contract.Customers.Customer_email,
                Contract.Customers.Customer_pass

        };
        // String selection = "username = '" + username +"'";
        Cursor cursor = getContentResolver().query(Contract.Customers.CONTENT_URI, projection, null , null, null);

        // Cursor cursor = database.rawQuery(selectQuery, null);

        count = cursor.getCount();

        cursor.close();

        return count;

    }


    public void updateSyncStatus(String id, String status) {
        //MyDBHandler dbHelper = new MyDBHandler(this.getApplicationContext());

        //SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SyncStatus", status);
        String selectiond = "email = '" + id +  "'";
        getContentResolver().update(Contract.Customers.CONTENT_URI, values, selectiond, null);

        // String updateQuery = "Update Customers set SyncStatus = '" + status + "' where email=" + "'" + id + "'";

        //Log.d("query", updateQuery);

        //  database.execSQL(updateQuery);

        // cursor.close();

    }


    public void syncSQLiteMySQLDB() {

        //Create AsycHttpClient object
        final ProgressDialog prgDialog = new ProgressDialog(this);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
      //  final MyDBHandler handler = new MyDBHandler(this);

        ArrayList<Customer> userList = getAllUsers();

        if (userList.size() != 0) {

            if (dbSyncCount() != 0) {

                prgDialog.show();

                params.put("usersJSON", composeJSONfromSQLite());

                client.post("http://10.0.2.2:10080/sqlitemysqlsync/insertuser.php", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println(responseBody);

                        prgDialog.hide();
                        String response = null;
                        try {
                            response = new String(responseBody, "UTF-8");
                            response = "{".concat(response).concat("}");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        try {


                            JSONArray arr = new JSONArray(response);

                            System.out.println(arr.length());

                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject obj = (JSONObject)arr.get(i);

                                System.out.println(obj.get("email"));

                                System.out.println(obj.get("status"));

                                updateSyncStatus(obj.get("email").toString(), obj.get("status").toString());

                            }

                            Toast.makeText(ServerSync.this, "DB Sync completed!", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {

                            // TODO Auto-generated catch block

                            Toast.makeText(ServerSync.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();

                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        prgDialog.hide();

                        if (statusCode == 404) {

                            Toast.makeText(ServerSync.this, "Requested resource not found", Toast.LENGTH_LONG).show();

                        } else if (statusCode == 500) {

                            Toast.makeText(ServerSync.this, "Something went wrong at server end", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(ServerSync.this, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();

                        }

                    }




                });

            } else {

                Toast.makeText(ServerSync.this, "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();

            }

        } else {

            Toast.makeText(ServerSync.this, "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();

        }

    }


    // Method to Sync MySQL to SQLite DB

    public void syncMySQLSQLiteDBcat() {
        final ProgressDialog prgDialog = new ProgressDialog(this);
        // Create AsycHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();

        // Http Request Params Object

        RequestParams params = new RequestParams();

        // Show ProgressBar

        prgDialog.show();

        // Make Http call to getusers.php

        client.post("http://192.168.8.101:10080/sqlitemysqlsync/getcategory.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
               try {
                    response = new String(responseBody, "UTF-8");
                   // response = "{".concat(response).concat("}");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Hide ProgressBar

                prgDialog.hide();

                // Update SQLite DB with response sent by getusers.php

                updateSQLitecat(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub

                // Hide ProgressBar

                prgDialog.hide();

                if (statusCode == 404) {

                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                } else if (statusCode == 500) {

                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",

                            Toast.LENGTH_LONG).show();

                }
            }


        });

    }

    public void updateSQLitecat(String response){

        ArrayList<HashMap<String, String>> usersynclist = new ArrayList<HashMap<String, String>>();


        // Create GSON object

        Gson gson = new GsonBuilder().create();

        try {

            // Extract JSON array from the response

            JSONArray arr = new JSONArray(response);

            System.out.println(arr.length());

            // If no of array elements is not zero

            if(arr.length() != 0){

                // Loop through each array element, get JSON object which has userid and username

                for (int i = 0; i < arr.length(); i++) {

                    // Get JSON object

                    JSONObject obj = (JSONObject) arr.get(i);

                    System.out.println(obj.get("id"));

                    System.out.println(obj.get("name"));

                    System.out.println(obj.get("description"));
                    System.out.println(obj.get("maincategory"));

                    // DB QueryValues Object to insert into SQLite

                    ContentValues values = new ContentValues();

                    values.put(Contract.Categories.Category_name, obj.get("name").toString());
                    values.put(Contract.Categories.Category_description, obj.get("description").toString());
                    values.put(Contract.Categories.Category_maincategory, obj.get("maincategory").toString());
                    getContentResolver().insert(Contract.Categories.CONTENT_URI,values);

                    HashMap<String, String> map = new HashMap<String, String>();

                    // Add status for each User in Hashmap

                    map.put("id", obj.get("id").toString());

                    map.put("status", "1");

                    usersynclist.add(map);

                }

                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users

                updateMySQLSyncStscat(gson.toJson(usersynclist));

                // Reload the Main Activity

                //reloadActivity();

            }

        } catch (JSONException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }



        // Method to inform remote MySQL DB about completion of Sync activity

    public void updateMySQLSyncStscat(String json) {

        System.out.println(json);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("syncsts", json);

        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
       //  10.0.2.2
        client.post("http://192.168.8.101:10080/sqlitemysqlsync/updatesyncstscat.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }



        });

    }

    public void syncMySQLSQLiteDB() {
        final ProgressDialog prgDialog = new ProgressDialog(this);
        // Create AsycHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();

        // Http Request Params Object

        RequestParams params = new RequestParams();

        // Show ProgressBar

        prgDialog.show();

        // Make Http call to getusers.php

        client.post("http://192.168.8.101:10080/sqlitemysqlsync/getusers.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    // response = "{".concat(response).concat("}");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Hide ProgressBar

                prgDialog.hide();

                // Update SQLite DB with response sent by getusers.php

                updateSQLite(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub

                // Hide ProgressBar

                prgDialog.hide();

                if (statusCode == 404) {

                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                } else if (statusCode == 500) {

                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",

                            Toast.LENGTH_LONG).show();

                }
            }


        });

    }

    public void updateSQLite(String response){

        ArrayList<HashMap<String, String>> usersynclist = new ArrayList<HashMap<String, String>>();


        // Create GSON object

        Gson gson = new GsonBuilder().create();

        try {

            // Extract JSON array from the response

            JSONArray arr = new JSONArray(response);

            System.out.println(arr.length());

            // If no of array elements is not zero

            if(arr.length() != 0){

                // Loop through each array element, get JSON object which has userid and username

                for (int i = 0; i < arr.length(); i++) {

                    // Get JSON object

                    JSONObject obj = (JSONObject) arr.get(i);

                    System.out.println(obj.get("id"));

                    System.out.println(obj.get("fname"));

                    System.out.println(obj.get("lname"));
                    System.out.println(obj.get("email"));
                    System.out.println(obj.get("pass"));

                    // DB QueryValues Object to insert into SQLite

                    ContentValues values = new ContentValues();

                    values.put(Contract.Customers.Customer_fname, obj.get("fname").toString());
                    values.put(Contract.Customers.Customer_lname, obj.get("lname").toString());
                    values.put(Contract.Customers.Customer_email, obj.get("email").toString());
                    values.put(Contract.Customers.Customer_pass, obj.get("pass").toString());
                    getContentResolver().insert(Contract.Customers.CONTENT_URI,values);

                    HashMap<String, String> map = new HashMap<String, String>();

                    // Add status for each User in Hashmap

                    map.put("id", obj.get("id").toString());

                    map.put("status", "1");

                    usersynclist.add(map);

                }

                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users

                updateMySQLSyncSts(gson.toJson(usersynclist));

                // Reload the Main Activity

                //reloadActivity();

            }

        } catch (JSONException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }


    // Method to inform remote MySQL DB about completion of Sync activity

    public void updateMySQLSyncSts(String json) {

        System.out.println(json);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("syncsts", json);

        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users

        client.post("http://10.0.2.2:10080/sqlitemysqlsync/updatesyncsts.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }



        });

    }


    public void Sync(View v){
        if(checkNetwork()){
            //syncSQLiteMySQLDB();
           // syncMySQLSQLiteDB();
          //  syncMySQLSQLiteDBcat();
            //syncMySQLSQLiteDBadd();
           syncMySQLSQLiteDBprod();
           // syncMySQLSQLiteDBprodComp();
            Toast.makeText(ServerSync.this, " Internet", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ServerSync.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void syncMySQLSQLiteDBadd() {
        final ProgressDialog prgDialog = new ProgressDialog(this);
        // Create AsycHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();

        // Http Request Params Object

        RequestParams params = new RequestParams();

        // Show ProgressBar

        prgDialog.show();

        // Make Http call to getusers.php

        client.post("http://10.0.2.2:10080/sqlitemysqlsync/getaddress.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    // response = "{".concat(response).concat("}");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Hide ProgressBar

                prgDialog.hide();

                // Update SQLite DB with response sent by getusers.php

                updateSQLiteadd(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub

                // Hide ProgressBar

                prgDialog.hide();

                if (statusCode == 404) {

                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                } else if (statusCode == 500) {

                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",

                            Toast.LENGTH_LONG).show();

                }
            }


        });

    }

    public void updateSQLiteadd(String response){

        ArrayList<HashMap<String, String>> usersynclist = new ArrayList<HashMap<String, String>>();


        // Create GSON object

        Gson gson = new GsonBuilder().create();

        try {

            // Extract JSON array from the response

            JSONArray arr = new JSONArray(response);

            System.out.println(arr.length());

            // If no of array elements is not zero

            if(arr.length() != 0){

                // Loop through each array element, get JSON object which has userid and username

                for (int i = 0; i < arr.length(); i++) {

                    // Get JSON object

                    JSONObject obj = (JSONObject) arr.get(i);

                    System.out.println(obj.get("id"));

                    System.out.println(obj.get("houseno"));

                    System.out.println(obj.get("mainroad"));

                    System.out.println(obj.get("subroad"));

                    System.out.println(obj.get("landmark"));

                    System.out.println(obj.get("city"));

                    System.out.println(obj.get("contactno"));

                    System.out.println(obj.get("username"));
                    // DB QueryValues Object to insert into SQLite

                    ContentValues values = new ContentValues();

                    values.put(Contract.Address.Address_HouseNo, obj.get("houseno").toString());
                    values.put(Contract.Address.Address_MainRoad, obj.get("mainroad").toString());
                    values.put(Contract.Address.Address_SubRoad, obj.get("subroad").toString());
                    values.put(Contract.Address.Address_LandMark, obj.get("landmark").toString());
                    values.put(Contract.Address.Address_City, obj.get("city").toString());
                    values.put(Contract.Address.Address_ContactNo, obj.get("contactno").toString());
                    values.put(Contract.Address.Address_Username, obj.get("username").toString());
                    getContentResolver().insert(Contract.Address.CONTENT_URI,values);

                    HashMap<String, String> map = new HashMap<String, String>();

                    // Add status for each User in Hashmap

                    map.put("id", obj.get("id").toString());

                    map.put("status", "yes");

                    usersynclist.add(map);

                }

                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users

                updateMySQLSyncStsadd(gson.toJson(usersynclist));

                // Reload the Main Activity

                //reloadActivity();

            }

        } catch (JSONException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }

    public void updateMySQLSyncStsadd(String json) {

        System.out.println(json);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("syncsts", json);

        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        //  10.0.2.2
        client.post("http://10.0.2.2:10080/sqlitemysqlsync/updatesyncstsadd.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }



        });

    }

    public void syncMySQLSQLiteDBprod() {
        final ProgressDialog prgDialog = new ProgressDialog(this);
        // Create AsycHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();

        // Http Request Params Object

        RequestParams params = new RequestParams();

        // Show ProgressBar

        prgDialog.show();

        // Make Http call to getusers.php 10.0.2.2

        client.post("http://192.168.8.106:10080/sqlitemysqlsync/getproduct.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    // response = "{".concat(response).concat("}");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Hide ProgressBar

                prgDialog.hide();

                // Update SQLite DB with response sent by getusers.php

                updateSQLiteprod(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub

                // Hide ProgressBar

                prgDialog.hide();

                if (statusCode == 404) {

                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                } else if (statusCode == 500) {

                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",

                            Toast.LENGTH_LONG).show();

                }
            }


        });

    }

    public void updateSQLiteprod(String response){

        ArrayList<HashMap<String, String>> usersynclist = new ArrayList<HashMap<String, String>>();


        // Create GSON object

        Gson gson = new GsonBuilder().create();

        try {

            // Extract JSON array from the response

            JSONArray arr = new JSONArray(response);

            System.out.println(arr.length());

            // If no of array elements is not zero

            if(arr.length() != 0){

                // Loop through each array element, get JSON object which has userid and username

                for (int i = 0; i < arr.length(); i++) {

                    // Get JSON object

                    JSONObject obj = (JSONObject) arr.get(i);

                    System.out.println(obj.get("id"));

                    System.out.println(obj.get("name"));

                    System.out.println(obj.get("description"));
                    System.out.println(obj.get("price"));
                    System.out.println(obj.get("image1"));
                    System.out.println(obj.get("image2"));
                    System.out.println(obj.get("image3"));
                    System.out.println(obj.get("category"));
                    System.out.println(obj.get("status"));

                    // DB QueryValues Object to insert into SQLite

                    ContentValues values = new ContentValues();

                    values.put(Contract.Products.Product_name, obj.get("name").toString());
                    values.put(Contract.Products.Product_Description, obj.get("description").toString());
                    values.put(Contract.Products.Product_Price, obj.get("price").toString());
                    values.put(Contract.Products.Product_Image1, obj.get("image1").toString());
                    values.put(Contract.Products.Product_Image2, obj.get("image2").toString());
                    values.put(Contract.Products.Product_Image3, obj.get("image3").toString());
                    values.put(Contract.Products.Product_Category, obj.get("category").toString());
                    values.put(Contract.Products.Product_Status, obj.get("status").toString());
                    getContentResolver().insert(Contract.Products.CONTENT_URI,values);

                    HashMap<String, String> map = new HashMap<String, String>();

                    // Add status for each User in Hashmap

                    map.put("id", obj.get("id").toString());

                    map.put("status", "yes");

                    usersynclist.add(map);

                }

                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users

                updateMySQLSyncStsprod(gson.toJson(usersynclist));

                // Reload the Main Activity

                //reloadActivity();

            }

        } catch (JSONException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }

    public void updateMySQLSyncStsprod(String json) {

        System.out.println(json);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("syncsts", json);

        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        //  10.0.2.2
        client.post("http://192.168.8.106:10080/sqlitemysqlsync/updatesyncstsprod.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }



        });

    }

    public void syncMySQLSQLiteDBprodComp() {
        final ProgressDialog prgDialog = new ProgressDialog(this);
        // Create AsycHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();

        // Http Request Params Object

        RequestParams params = new RequestParams();

        // Show ProgressBar

        prgDialog.show();

        // Make Http call to getusers.php

        client.post("http://10.0.2.2:10080/sqlitemysqlsync/getprodcomp.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    // response = "{".concat(response).concat("}");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Hide ProgressBar

                prgDialog.hide();

                // Update SQLite DB with response sent by getusers.php

                updateSQLiteprodComp(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub

                // Hide ProgressBar

                prgDialog.hide();

                if (statusCode == 404) {

                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                } else if (statusCode == 500) {

                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",

                            Toast.LENGTH_LONG).show();

                }
            }


        });

    }

    public void updateSQLiteprodComp(String response){

        ArrayList<HashMap<String, String>> usersynclist = new ArrayList<HashMap<String, String>>();


        // Create GSON object

        Gson gson = new GsonBuilder().create();

        try {

            // Extract JSON array from the response

            JSONArray arr = new JSONArray(response);

            System.out.println(arr.length());

            // If no of array elements is not zero

            if(arr.length() != 0){

                // Loop through each array element, get JSON object which has userid and username

                for (int i = 0; i < arr.length(); i++) {

                    // Get JSON object

                    JSONObject obj = (JSONObject) arr.get(i);

                    System.out.println(obj.get("id"));

                    System.out.println(obj.get("productname"));

                    System.out.println(obj.get("quantity"));
                    System.out.println(obj.get("colour"));
                    System.out.println(obj.get("size"));
                    // DB QueryValues Object to insert into SQLite

                    ContentValues values = new ContentValues();

                    values.put(Contract.ProdComponent.ProdComponent_ProductName, obj.get("productname").toString());
                    values.put(Contract.ProdComponent.ProdComponent_qty, obj.get("quantity").toString());
                    values.put(Contract.ProdComponent.ProdComponent_colour, obj.get("colour").toString());
                    values.put(Contract.ProdComponent.ProdComponent_size, obj.get("size").toString());
                    getContentResolver().insert(Contract.ProdComponent.CONTENT_URI,values);

                    HashMap<String, String> map = new HashMap<String, String>();

                    // Add status for each User in Hashmap

                    map.put("id", obj.get("id").toString());

                    map.put("status", "1");

                    usersynclist.add(map);

                }

                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users

                updateMySQLSyncStsprodComp(gson.toJson(usersynclist));

                // Reload the Main Activity

                //reloadActivity();

            }

        } catch (JSONException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }

    public void updateMySQLSyncStsprodComp(String json) {

        System.out.println(json);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("syncsts", json);

        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        //  10.0.2.2
        client.post("http://10.0.2.2:10080/sqlitemysqlsync/updatesyncstsprodcomp.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }



        });

    }
}
