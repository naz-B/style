package com.example.nazb.styleomega;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static java.security.AccessController.getContext;


/**
 * Created by nazB on 9/4/2017.
 */

public class SyncMySQL extends AppCompatActivity {



   /* public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());
    }*/

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
      Cursor cursor = getContentResolver().query(Contract.Categories.CONTENT_URI, projection, null , null, null);

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



    }


    public void syncSQLiteMySQLDB(final Context context) {

        //Create AsycHttpClient object
        final ProgressDialog prgDialog = new ProgressDialog(context);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        final MyDBHandler handler = new MyDBHandler(this);

        ArrayList<Customer> userList = getAllUsers();

        if (userList.size() != 0) {

            if (dbSyncCount() != 0) {

                prgDialog.show();

                params.put("usersJSON", composeJSONfromSQLite());

                client.post("http://127.0.0.1:10080/sqlitemysqlsync/insertuser.php", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println(responseBody);

                        prgDialog.hide();

                        try {

                            JSONArray arr = new JSONArray(responseBody);

                            System.out.println(arr.length());

                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject obj = (JSONObject) arr.get(i);

                                System.out.println(obj.get("id"));

                                System.out.println(obj.get("status"));

                                updateSyncStatus(obj.get("id").toString(), obj.get("status").toString());

                            }

                            Toast.makeText(context, "DB Sync completed!", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {

                            // TODO Auto-generated catch block

                            Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();

                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        prgDialog.hide();

                        if (statusCode == 404) {

                            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();

                        } else if (statusCode == 500) {

                            Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();

                        }

                    }




                });

            } else {

                Toast.makeText(context, "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();

            }

        } else {

            Toast.makeText(context, "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();

        }

    }


}
