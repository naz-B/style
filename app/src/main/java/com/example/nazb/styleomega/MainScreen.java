package com.example.nazb.styleomega;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private String username = null;
    public final static String ID = "com.example.myfirstapp.MESSAGE";
    private List<Products> productList;
    private List<Products> saleList;

    private GridView gridView;
    private ProductsGridMain gridViewAdapter;

    private GridView gridView1;
    private ProductsGridMain gridViewAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        username = prefs.getString("username", null);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView != null) {
            View menu = navigationView.getHeaderView(0);
            TextView textView = (TextView)menu.findViewById(R.id.loginregister);
            if(prefs.getString("username", null) != null){
                textView.setText(username);
                textView.setOnClickListener(listener);
            }
            else{
                textView.setText("Login | Register");
                textView.setOnClickListener(listener1);
            }
        }

        gridView = (GridView) findViewById(R.id.maingrid1);
        getProductList();
        getSaleList();
        gridViewAdapter = new ProductsGridMain(this, R.layout.activity_product_grid_main,saleList );
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(onItemClick);

        gridView1 = (GridView) findViewById(R.id.maingrid2);


        gridViewAdapter1 = new ProductsGridMain(this, R.layout.activity_product_grid_main, productList);
        gridView1.setAdapter(gridViewAdapter1);
        gridView1.setOnItemClickListener(onItemClick);

    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Products name = (Products) parent.getItemAtPosition(position);
            String prodName = name.getProduct_name();
            Intent intent = new Intent(MainScreen.this, DetailScreen.class);
            intent.putExtra("Clicked_Item", prodName);
            startActivity(intent);

        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainScreen.this, ManageAccount.class);
            startActivity(intent);
        }
    };



    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         switch (item.getItemId()) {

           case R.id.my_home: {
                Intent intent = new Intent(MainScreen.this, ServerSync.class);
                startActivity(intent);
                break;
            }
            case R.id.my_account: {
                if (username != null) {
                    Intent intent = new Intent(MainScreen.this, ManageAccount.class);
                    startActivity(intent);
                    break;
                }
                else{
                    Intent intent = new Intent(MainScreen.this, Login.class);
                    startActivity(intent);
                    break;
                }

            }
             case R.id.favorites: {
                 Intent intent = new Intent(MainScreen.this, Favourites.class);
                 startActivity(intent);
                 break;
             }
             case R.id.orders: {
                 Intent intent = new Intent(MainScreen.this, Orders.class);
                 startActivity(intent);
                 break;
             }
             case R.id.cart:
                 Intent intent = new Intent(MainScreen.this, ShoppingCart.class);
                 startActivity(intent);
                 break;
             case R.id.logout:
                 final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyApp", MODE_PRIVATE);
               //  if (pref != null){
                     AlertDialog.Builder builder = new AlertDialog.Builder(this);
                     builder.setMessage("Are you sure you want to logout?").setTitle("Logout");
                     builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                             pref.edit().clear().apply();
                             Intent intent = new Intent(MainScreen.this, MainScreen.class);
                             startActivity(intent);
                             Snackbar.make(findViewById(R.id.drawerlayout), "Successfully logged out", Snackbar.LENGTH_LONG).show();

                         }
                     });
                     builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                         }
                     });
                     AlertDialog dialog = builder.create();
                     dialog.show();

              //   }
                 break;

        }
        return true;
    }
    public void onAllCatClick (View view){

        Intent intent = new Intent(MainScreen.this, AllCategory.class);
        int id = view.getId();
        String ids = getResources().getResourceEntryName(id);
        intent.putExtra(ID, ids);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(MainScreen.this, SearchResult.class);
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

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainScreen.this, Login.class);
            startActivity(intent);
        }

    };


    public List<Products> getProductList() {
        productList = new ArrayList<>();
        String status = "sale";
        String[] projection = {
                Contract.Products.Product_name,
                Contract.Products.Product_Description,
                Contract.Products.Product_Price,
                Contract.Products.Product_Image1,
                Contract.Products.Product_Image2

        };
        String selection = "Status = '" + status +"'";
        Cursor cursor = getContentResolver().query(Contract.Products.CONTENT_URI, projection, selection , null, null);
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
            productList.add(new Products(
                    cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1)),
                    cursor.getString(cursor.getColumnIndex(Contract.Products.Product_name)),
                    null,
                    cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price))
            ));
        }
        cursor.close();

        return productList;
    }

    public List<Products> getSaleList() {
        saleList = new ArrayList<>();
        String status = "featured";
        String[] projection = {
                Contract.Products.Product_name,
                Contract.Products.Product_Description,
                Contract.Products.Product_Price,
                Contract.Products.Product_Image1,
                Contract.Products.Product_Image2

        };
        String selection = "Status = '" + status +"'";
        Cursor cursor = getContentResolver().query(Contract.Products.CONTENT_URI, projection, selection , null, null);
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
            productList.add(new Products(
                    cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1)),
                    null,
                    null,
                    cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price))
            ));
        }
        cursor.close();

        return saleList;
    }


}
