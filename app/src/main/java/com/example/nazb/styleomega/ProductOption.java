package com.example.nazb.styleomega;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductOption extends AppCompatActivity {
    int qtySelected;
    EditText qtyEntered;
    String colourSelected;
    String sizeSelected;
    String item_name;
    TextView qty;
    int item_id;
    String username;
    String status;
    int cart_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_option);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");
        setPrefs();
    }

    public void setPrefs() {
        Intent intent = getIntent();
        item_name = intent.getExtras().getString("Prod_name");

        TextView item_title = (TextView) findViewById(R.id.itemName);
        TextView item_desc = (TextView) findViewById(R.id.itemDesc);
        TextView item_price = (TextView) findViewById(R.id.itemPrice);
        final Spinner colours = (Spinner) findViewById(R.id.colours);
        final Spinner sizes = (Spinner) findViewById(R.id.sizes);

        qtyEntered = (EditText) findViewById(R.id.qtys);
        ImageView item_image = (ImageView) findViewById(R.id.itemImage);

        String[] projection = {
                Contract.Products.Product_name,
                Contract.Products.Product_Description,
                Contract.Products.Product_Price,
                Contract.Products.Product_Image1,
                Contract.Products.Product_Image2,
                Contract.Products.Product_Image3
        };
        String selection = "name = '" + item_name + "'";
        Cursor cursor = getContentResolver().query(Contract.Products.CONTENT_URI, projection, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {


            item_title.setText(cursor.getString(cursor.getColumnIndex(Contract.Products.Product_name)));
            item_desc.setText(cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Description)));
            item_price.setText("LKR " + cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price)));
            String image1string = cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1));
            String url = image1string;
            Glide.with(ProductOption.this).load(url).into(item_image);


        }
        cursor.close();

        List<String> colourList = new ArrayList<String>();
        List<String> sizeList = new ArrayList<String>();
        colourList.add("");
        sizeList.add("");
        String[] projections = {
                Contract.ProdComponent.ProdComponent_colour,
                Contract.ProdComponent.ProdComponent_size,
        };
        String selections = "product_name = '" + item_name + "'";

        Cursor cursors = getContentResolver().query(Contract.ProdComponent.CONTENT_URI, projections, selections, null, null);
        if (cursors != null && cursors.moveToFirst()) {
            while (cursors.moveToNext()) {
                for (int i = 0; i < cursors.getCount(); i++) {
                    colourList.add(cursors.getString(cursors.getColumnIndex(Contract.ProdComponent.ProdComponent_colour)));
                    sizeList.add(cursors.getString(cursors.getColumnIndex(Contract.ProdComponent.ProdComponent_size)));
                    cursors.moveToPosition(i);
                }
            }
        }
        cursors.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, colourList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colours.setAdapter(dataAdapter);
        colours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                colourSelected = colours.getItemAtPosition(colours.getSelectedItemPosition()).toString();
                setQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sizeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizes.setAdapter(dataAdapter1);
        sizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sizeSelected = sizes.getItemAtPosition(sizes.getSelectedItemPosition()).toString();
                setQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void setQty() {
        qty = (TextView) findViewById(R.id.qty);
        if (sizeSelected != null && colourSelected != null) {
            String[] projectiond = {
                    Contract.ProdComponent.ProdComponent_qty
            };
            String selectiond = "size = '" + sizeSelected + "' and colour = '" + colourSelected
                    + "' and product_name ='" + item_name + "'";
            Cursor cursord = getContentResolver().query(Contract.ProdComponent.CONTENT_URI, projectiond, selectiond, null, null);
            if (cursord != null && cursord.moveToFirst()) {
                qty.setText(cursord.getString(cursord.getColumnIndex(Contract.ProdComponent.ProdComponent_qty)));
            }
            cursord.close();
        } else {
            // Snackbar.make(findViewById(R.id.prodOption), "Select colour and size", Snackbar.LENGTH_LONG).show();

        }
    }

    public boolean isPrefSelected() {
        boolean flag = false;
        if (sizeSelected == null || colourSelected == null || sizeSelected == "" || colourSelected == "") {
            Snackbar.make(findViewById(R.id.prodOption), "Select colour and size", Snackbar.LENGTH_LONG).show();
        } else if (qtyEntered.getText().toString() == null || qtyEntered.getText().toString() == "") {
            Snackbar.make(findViewById(R.id.prodOption), "Select quantity", Snackbar.LENGTH_LONG).show();
        } else {
            qtySelected = Integer.parseInt(qtyEntered.getText().toString());
            int qtys = Integer.parseInt(qty.getText().toString());
            if (qtySelected > qtys) {
                Snackbar.make(findViewById(R.id.prodOption), "Quantity not available", Snackbar.LENGTH_LONG).show();
            }
            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
            prefs.edit().putString("size", sizeSelected).apply();
            prefs.edit().putString("colour", colourSelected).apply();
            prefs.edit().putInt("qty", qtySelected).apply();
            flag = true;
        }
        return flag;
    }

    public void addToCart() {
        String[] projection = {
                Contract.ShoppingCart.ShoppingCart_id,
                Contract.ShoppingCart.ShoppingCart_status
        };
        String selection = "username = '" + username + "' and status = 'ongoing'";
        Cursor cursor = getContentResolver().query(Contract.ShoppingCart.CONTENT_URI, projection, selection, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            cart_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.ShoppingCart.ShoppingCart_id)));
        }

        if (cursor.getCount() == 0) {
            Cursor cursors = getContentResolver().query(Contract.ShoppingCart.CONTENT_URI, null, null, null, null);
            cursors.moveToLast();
            cart_id = Integer.parseInt(cursors.getString(cursors.getColumnIndex(Contract.ShoppingCart.ShoppingCart_id))) + 1;
            status = "ongoing";
            ContentValues values = new ContentValues();
            values.put(Contract.ShoppingCart.ShoppingCart_id, cart_id);
            values.put(Contract.ShoppingCart.ShoppingCart_username, username);
            values.put(Contract.ShoppingCart.ShoppingCart_status, "ongoing");
            getContentResolver().insert(Contract.ShoppingCart.CONTENT_URI, values);
        }
        getCartItemID();
        ContentValues values = new ContentValues();
        values.put(Contract.CartItem.CartItem_cartid, cart_id);
        values.put(Contract.CartItem.CartItem_productname, item_name);
        values.put(Contract.CartItem.CartItem_qty, qtySelected);
        values.put(Contract.CartItem.CartItem_ID, item_id);
        getContentResolver().insert(Contract.CartItem.CONTENT_URI, values);
        Toast.makeText(ProductOption.this,
                status + String.valueOf(cart_id), Toast.LENGTH_LONG).show();
    }


    public void onCartClick(View v) {
        if (isPrefSelected()) {
            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
            username = prefs.getString("username", null);
            if (username == null) {
                Intent intent = new Intent(ProductOption.this, Login.class);
                startActivity(intent);
            } else {
                addToCart();
                Intent intent = new Intent(ProductOption.this, ShoppingCart.class);
                intent.putExtra("Cart_ID", cart_id);
                startActivity(intent);
            }
        }

    }

    public void getCartItemID() {
        String[] projection = {
                Contract.CartItem.CartItem_ID
        };
        String selection = "cart_id = '" + cart_id + "'";
        Cursor cursor = getContentResolver().query(Contract.CartItem.CONTENT_URI, projection, selection, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToLast();
            item_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_ID))) + 1;
        } else if (cursor.getCount() == 0) {
            item_id = 1;
        }
    }

    public void onBuyNowClick(View v) {
        if (isPrefSelected()) {
            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
            username = prefs.getString("username", null);
            if (username == null) {
                Intent intent = new Intent(ProductOption.this, Login.class);
                startActivity(intent);
            } else {
                addToCart();
                Intent intent = new Intent(ProductOption.this, Checkout.class);
                intent.putExtra("Cart_ID", cart_id);
                startActivity(intent);
            }
        }
    }
}
