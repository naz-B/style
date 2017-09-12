package com.example.nazb.styleomega;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {


    static int cart_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");
        setContentView(R.layout.activity_shopping_cart);


    }

    public void onDeleteClick(View v) {
        String[] projection = {
                Contract.CartItem.CartItem_productname,
                Contract.CartItem.CartItem_qty,
                Contract.CartItem.CartItem_ID

        };
        String selection = "cart_id = '" + cart_id + "'";
        Cursor cursor = getContentResolver().query(Contract.CartItem.CONTENT_URI, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Toast.makeText(ShoppingCart.this,
                        cursor.getString(cursor.getColumnIndex(Contract.CartItem._ID)), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onCheckoutClick(View v) {
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) {
            Intent intent = new Intent(ShoppingCart.this, Login.class);
            startActivity(intent);
        } else {
            String[] projections = {
                    Contract.ShoppingCart.ShoppingCart_id,


            };
            String selections = "username = '" + username + "' and status = 'ongoing'";
            Cursor cursors = getContentResolver().query(Contract.ShoppingCart.CONTENT_URI, projections, selections, null, null);
            if (cursors != null && cursors.moveToFirst()) {
                cart_id = Integer.parseInt(cursors.getString(cursors.getColumnIndex(Contract.ShoppingCart.ShoppingCart_id)));

                Toast.makeText(ShoppingCart.this,
                        prefs.getString("username", null) + " " + String.valueOf(cart_id), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ShoppingCart.this, Checkout.class);
                intent.putExtra("cart_id", cart_id);
                startActivity(intent);
            }

        }

    }

}
