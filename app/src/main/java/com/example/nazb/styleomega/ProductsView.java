package com.example.nazb.styleomega;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ProductsView extends AppCompatActivity {
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ProductsList listViewAdapter;
    private ProductsGrid gridViewAdapter;
    private List<Products> productList;
    private int currentViewMode = 0;
    private String username = null;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        username = prefs.getString("username", null);
        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubGrid = (ViewStub) findViewById(R.id.stub_grid);
        stubList.inflate();
        stubGrid.inflate();
        listView = (ListView) findViewById(R.id.mylistview);
        gridView = (GridView) findViewById(R.id.mygridview);
        getProductList();
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);
        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);
        switchView();
    }


    private void switchView() {

        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            stubList.setVisibility(View.VISIBLE);
            stubGrid.setVisibility(View.GONE);
        } else {
            stubList.setVisibility(View.GONE);
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            listViewAdapter = new ProductsList(this, R.layout.activity_product_list, productList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new ProductsGrid(this, R.layout.activity_product_grid, productList);
            gridView.setAdapter(gridViewAdapter);
        }
    }

    public List<Products> getProductList() {

        Intent intent = getIntent();
        String category = intent.getExtras().getString("Clicked_Category");
        productList = new ArrayList<>();
        String[] projection = {
                Contract.Products.Product_name,
                Contract.Products.Product_Description,
                Contract.Products.Product_Price,
                Contract.Products.Product_Image1,
                Contract.Products.Product_Image2
        };
        String selection = "Category = '" + category +"'";
        Cursor cursor = getContentResolver().query(Contract.Products.CONTENT_URI, projection, selection , null, null);
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                    productList.add(new Products(
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1)),
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_name)),
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Description)),
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price))
                            ));
        }
        cursor.close();
        return productList;
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Products name = (Products) parent.getItemAtPosition(position);
            String prodName = name.getProduct_name();
            Intent intent = new Intent(ProductsView.this, DetailScreen.class);
            intent.putExtra("Clicked_Item", prodName);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navmenu, menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_home: {
                Intent intent = new Intent(ProductsView.this, MainScreen.class);
                startActivity(intent);
                break;
            }
            case R.id.my_account: {
                if (username != null) {
                    Intent intent = new Intent(ProductsView.this, ManageAccount.class);
                    startActivity(intent);
                    break;
                }
                else{
                    Intent intent = new Intent(ProductsView.this, Login.class);
                    startActivity(intent);
                    break;
                }
            }
            case R.id.favorites: {
                Intent intent = new Intent(ProductsView.this, Favourites.class);
                startActivity(intent);
                break;
            }
            case R.id.orders: {
                Intent intent = new Intent(ProductsView.this, Orders.class);
                startActivity(intent);
                break;
            }
            case R.id.cart:
                Intent intent = new Intent(ProductsView.this, ShoppingCart.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void switch_view(View v){
        ImageButton view_switch = (ImageButton)findViewById(R.id.switch_view);
        view_switch.setImageResource(R.drawable.ic_view_list);
        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            view_switch.setImageResource(R.drawable.ic_view_list);
            currentViewMode = VIEW_MODE_GRIDVIEW;
        } else {
            view_switch.setImageResource(R.drawable.ic_view_grid);
            currentViewMode = VIEW_MODE_LISTVIEW;
        }
        switchView();
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentViewMode", currentViewMode);
        editor.apply();
    }
}
