package com.example.nazb.styleomega;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {

    List<Products> order_prod_list;
    ProductsList listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Orders");
        ListView fav_list = (ListView)findViewById(R.id.order_list);
        getProductList();
        listViewAdapter = new ProductsList(this, R.layout.activity_product_list, order_prod_list);
        fav_list.setAdapter(listViewAdapter);
    }

    public List<Products> getProductList() {

        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        order_prod_list = new ArrayList<>();
        String[] projection = {
                Contract.Order.Order_item,
                Contract.Order.Order_status

        };
        String selection = "username = '" + username +"'";
        Cursor cursor = getContentResolver().query(Contract.Order.CONTENT_URI, projection, selection , null, null);
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                    String[] projections = {
                            Contract.Products.Product_name,
                            Contract.Products.Product_Image1,
                            Contract.Products.Product_Price

                    };
                    String selections = "name = '" + cursor.getString(cursor.getColumnIndex(Contract.Order.Order_item)) +"'";
                    Cursor cursors = getContentResolver().query(Contract.Products.CONTENT_URI, projections, selections , null, null);
                    if(cursors != null && cursors.moveToFirst()) {
                       // String imagepath = cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Image1));
                       // int image_id = this.getResources().getIdentifier(imagepath, null, null);
                        order_prod_list.add(new Products(
                                cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Image1)),
                                cursors.getString(cursors.getColumnIndex(Contract.Products.Product_name)),
                                cursor.getString(cursor.getColumnIndex(Contract.Order.Order_status)),
                                cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Price))
                        ));

                    }


        }
        cursor.close();

        return order_prod_list;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navmenu, menu);
        inflater.inflate(R.menu.search_menu, menu);
        inflater.inflate(R.menu.appbarbuttons, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(Orders.this, SearchResult.class);
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
                Intent intent = new Intent(Orders.this, MainScreen.class);
                startActivity(intent);
                break;
            }
            case R.id.my_account: {
                if (username != null) {
                    Intent intent = new Intent(Orders.this, ManageAccount.class);
                    startActivity(intent);
                    break;
                } else {
                    Intent intent = new Intent(Orders.this, Login.class);
                    startActivity(intent);
                    break;
                }

            }
            case R.id.favorites: {
                Intent intent = new Intent(Orders.this, Favourites.class);
                startActivity(intent);
                break;
            }
            case R.id.orders: {
                Intent intent = new Intent(Orders.this, Orders.class);
                startActivity(intent);
                break;
            }
            case R.id.cart:
                Intent intent = new Intent(Orders.this, ShoppingCart.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}
