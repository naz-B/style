package com.example.nazb.styleomega;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.example.nazb.styleomega.ShoppingCart.cart_id;


public class DetailScreen extends AppCompatActivity {

    String item;
    static int image1;
    static int image2;
    static int image3;
    private String username = null;
    public static String[] image_resources = new String[3];
    String sizeSelected;
    String colourSelected;
    int qtySelected;
    int item_id;
    int cart_id;
    String item_name;
    String status;
    ViewPager viewPager;
    ImageSwipe swiper;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");
        setScreen();
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        username = prefs.getString("username", null);
        colourSelected = prefs.getString("colour", null);
        sizeSelected = prefs.getString("size", null);
        qtySelected = prefs.getInt("qty", 0);
        Toast.makeText(DetailScreen.this, getIntent().getExtras().getString("Clicked_Item")+"colour: " + colourSelected + " size: " + sizeSelected + " qty: " + String.valueOf(qtySelected), Toast.LENGTH_LONG).show();


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

                Intent intent = new Intent(DetailScreen.this, SearchResult.class);
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
        switch (item.getItemId()) {
            case R.id.action_share:
                Bitmap bm = screenShot(findViewById(R.id.rl));
                File file = saveBitmap(bm, "image.png");
                Log.i("chase", "filepath: " + file.getAbsolutePath());
                Uri screenshotUri = Uri.fromFile(new File(file.getAbsolutePath()));
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Check out my new app StyleOmega!");
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.my_home: {
                Intent intent = new Intent(DetailScreen.this, MainScreen.class);
                startActivity(intent);
                break;
            }
            case R.id.my_account: {
                if (username != null) {
                    Intent intent = new Intent(DetailScreen.this, ManageAccount.class);
                    startActivity(intent);
                    break;
                } else {
                    Intent intent = new Intent(DetailScreen.this, Login.class);
                    startActivity(intent);
                    break;
                }

            }
            case R.id.favorites: {
                Intent intent = new Intent(DetailScreen.this, Favourites.class);
                startActivity(intent);
                break;
            }
            case R.id.orders: {
                Intent intent = new Intent(DetailScreen.this, Orders.class);
                startActivity(intent);
                break;
            }
            case R.id.cart:
                Intent intent = new Intent(DetailScreen.this, ShoppingCart.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void setScreen() {
        TextView title = (TextView) findViewById(R.id.Title);
        TextView description = (TextView) findViewById(R.id.description);
        TextView price = (TextView) findViewById(R.id.price);
        Intent intent = getIntent();
        item = intent.getExtras().getString("Clicked_Item");
        String[] projection = {
                Contract.Products.Product_name,
                Contract.Products.Product_Description,
                Contract.Products.Product_Price,
                Contract.Products.Product_Image1,
                Contract.Products.Product_Image2,
                Contract.Products.Product_Image3
        };
        String selection = "name = '" + item + "'";
        Cursor cursor = getContentResolver().query(Contract.Products.CONTENT_URI, projection, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {


            title.setText(cursor.getString(cursor.getColumnIndex(Contract.Products.Product_name)));
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Description)));
            price.setText("LKR " + cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price)));
            String image1string = cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1));
            String image2string = cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image2));
            String image3string = cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image3));
            image_resources[0] = image1string;
            image_resources[1] = image2string;
            image_resources[2] = image3string;
        }
        cursor.close();
        viewPager = (ViewPager) findViewById(R.id.img_swiper);
        swiper = new ImageSwipe(this);
        viewPager.setAdapter(swiper);
    }

    public void onPrefClick(View v) {
        Intent intent = new Intent(DetailScreen.this, ProductOption.class);
        intent.putExtra("Prod_name", item);
        startActivity(intent);
    }

    public void onInquiriesClick(View v) {
        Intent intent = new Intent(DetailScreen.this, Inquiries.class);
        intent.putExtra("Prod_name", item);
        startActivity(intent);
    }

    public void onFavClick(View v) {
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        ContentValues values = new ContentValues();
        values.put(Contract.Favourite.Favourite_Custusername, username);
        values.put(Contract.Favourite.Favourite_ID, 1);
        values.put(Contract.Favourite.Favourite_item, item);
        getContentResolver().insert(Contract.Favourite.CONTENT_URI, values);
        Intent intent = new Intent(DetailScreen.this, Favourites.class);
        startActivity(intent);
    }

    public boolean isPrefSelected() {
        boolean flag = false;
        if (sizeSelected == null || colourSelected == null || sizeSelected == "" || colourSelected == "") {
            Intent intent = new Intent(DetailScreen.this, ProductOption.class);
            intent.putExtra("Prod_name", item);
            startActivity(intent);
        } else if (qtySelected == 0) {
            Intent intent = new Intent(DetailScreen.this, ProductOption.class);
            intent.putExtra("Prod_name", item);
            startActivity(intent);
        } else {
            flag = true;
        }
        return flag;
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
        cursor.close();
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
        Toast.makeText(DetailScreen.this,
                status + String.valueOf(cart_id), Toast.LENGTH_LONG).show();
    }

    public void onAddtoCartClickFromDetailScreen(View v) {
        if (isPrefSelected()) {
            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
            username = prefs.getString("username", null);
            if (username == null) {
                Intent intent = new Intent(DetailScreen.this, Login.class);
                startActivity(intent);
            } else {
                addToCart();
                Intent intent = new Intent(DetailScreen.this, ShoppingCart.class);
                intent.putExtra("Cart_ID", cart_id);
                startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(DetailScreen.this, ProductOption.class);
            intent.putExtra("Prod_name", item);
            startActivity(intent);
        }
    }

    public void onBuyNowClickFromDetailScreen(View v) {
        if (isPrefSelected()) {
            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
            username = prefs.getString("username", null);
            if (username == null) {
                Intent intent = new Intent(DetailScreen.this, Login.class);
                startActivity(intent);
            } else {
                addToCart();
                Intent intent = new Intent(DetailScreen.this, Checkout.class);
                intent.putExtra("Cart_ID", cart_id);
                startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(DetailScreen.this, ProductOption.class);
            intent.putExtra("Prod_name", item);
            startActivity(intent);
        }

    }


}
