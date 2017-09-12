package com.example.nazb.styleomega;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    private ListView listView;
    private AddressListCheckout listViewAdapter;
    private List<Address> addressList;
    int cart_id = 0;

    String username;
    List<String> productname = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirm & Checkout");
        setShippingAddress();
        Intent intent = getIntent();
        final SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        username = prefs.getString("username", null);
        String[] projections = {
                Contract.ShoppingCart.ShoppingCart_id,
        };
        String selections = "username = '" + prefs.getString("username", null) + "' and status = 'ongoing'";
        Cursor cursors = getContentResolver().query(Contract.ShoppingCart.CONTENT_URI, projections, selections, null, null);
        if (cursors != null && cursors.moveToFirst()) {
            cart_id = Integer.parseInt(cursors.getString(cursors.getColumnIndex(Contract.ShoppingCart.ShoppingCart_id)));
        }
        Toast.makeText(Checkout.this,
                prefs.getString("username", null) + " " + String.valueOf(cart_id), Toast.LENGTH_LONG).show();
        Button checkout = (Button) findViewById(R.id.checkoutButton);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(Contract.ShoppingCart.ShoppingCart_status, "processed");
                String selection = "cart_id = " + cart_id;
                getContentResolver().update(Contract.ShoppingCart.CONTENT_URI, values, selection, null);
                updateProductQty();
                addtoOrderList();
                prefs.edit().remove("colour").commit();
                prefs.edit().remove("size").commit();
                prefs.edit().remove("qty").commit();
                Intent intent = new Intent(Checkout.this, Orders.class);
                startActivity(intent);
            }
        });
    }

    public void setShippingAddress() {
        listView = (ListView) findViewById(R.id.addresslist);
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
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                // for(int i = 0; i < cursor.getCount(); i++){
                addressList.add(new Address(
                        1,
                        cursor.getString(cursor.getColumnIndex(Contract.Address.Address_HouseNo)),
                        cursor.getString(cursor.getColumnIndex(Contract.Address.Address_MainRoad)),
                        cursor.getString(cursor.getColumnIndex(Contract.Address.Address_SubRoad)),
                        cursor.getString(cursor.getColumnIndex(Contract.Address.Address_LandMark)),
                        cursor.getString(cursor.getColumnIndex(Contract.Address.Address_City)),
                        cursor.getString(cursor.getColumnIndex(Contract.Address.Address_ContactNo))
                ));
                //cursor.moveToPosition(i);
                //  }
            }
        }
        cursor.close();
        listViewAdapter = new AddressListCheckout(this, R.layout.address_list_checkout, addressList);
        listView.setAdapter(listViewAdapter);
    }

    public void updateProductQty() {
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String colourSelected = prefs.getString("colour", null);
        String sizeSelected = prefs.getString("size", null);
        int qtySelected = prefs.getInt("qty", 0);
        int newqty = 0;

        String[] projections = {
                Contract.CartItem.CartItem_qty,
                Contract.CartItem.CartItem_productname

        };
        String selections = "cart_id = " + cart_id;
        Cursor cursor = getContentResolver().query(Contract.CartItem.CONTENT_URI, projections, selections, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String item = cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_productname));
            String[] projection = {
                    Contract.ProdComponent.ProdComponent_qty,

            };
            String selection = "product_name = '" + item + "' and size = '" + sizeSelected + "' and colour = '" + colourSelected + "'";
            Cursor cursors = getContentResolver().query(Contract.ProdComponent.CONTENT_URI, projection, selection, null, null);
            if (cursors != null && cursors.moveToFirst()) {
                newqty = Integer.parseInt(cursors.getString(cursors.getColumnIndex(Contract.ProdComponent.ProdComponent_qty)))
                        - Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_qty)));
                ContentValues values = new ContentValues();
                values.put(Contract.ProdComponent.ProdComponent_qty, newqty);
                String selectiond = "product_name = '" + item + "' and size = '" + sizeSelected + "' and colour = '" + colourSelected + "'";
                getContentResolver().update(Contract.ProdComponent.CONTENT_URI, values, selectiond, null);
            }
        }


        Toast.makeText(Checkout.this, String.valueOf(newqty), Toast.LENGTH_LONG).show();

    }

    public void addtoOrderList() {
        String[] projection = {
                Contract.CartItem.CartItem_ID,
                Contract.CartItem.CartItem_productname,
                Contract.CartItem.CartItem_qty

        };
        String selection = "cart_id = " + cart_id;
        Cursor cursor = getContentResolver().query(Contract.CartItem.CONTENT_URI, projection, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    ContentValues values = new ContentValues();
                    values.put(Contract.Order.Order_Custuserame, username);
                    values.put(Contract.Order.Order_ID, cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_ID)));
                    values.put(Contract.Order.Order_item, cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_productname)));
                    values.put(Contract.Order.Order_status, "processing");
                    getContentResolver().insert(Contract.Order.CONTENT_URI, values);

                    productname.add(cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_productname)));

                    cursor.moveToPosition(i);
                }
            }
        }

    }
}
