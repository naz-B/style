package com.example.nazb.styleomega;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    private List<Products> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Result");
        handleIntent(getIntent());
        ViewStub stubGrid = (ViewStub) findViewById(R.id.stub_grid3);
        stubGrid.inflate();
        GridView gridView = (GridView) findViewById(R.id.mygridview);

        stubGrid.setVisibility(View.VISIBLE);
        ProductsGrid gridViewAdapter = new ProductsGrid(this, R.layout.activity_product_grid_main, productList);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(onItemClick);
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Products name = (Products) parent.getItemAtPosition(position);
            String prodName = name.getProduct_name();
            Intent intent = new Intent(SearchResult.this, DetailScreen.class);
            intent.putExtra("Clicked_Item", prodName);
            startActivity(intent);
        }
    };
    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        productList = new ArrayList<>();
        String query = intent.getStringExtra("Query");
        String[] projection = {
                Contract.Products.Product_name,
                Contract.Products.Product_Description,
                Contract.Products.Product_Price,
                Contract.Products.Product_Image1,
        };
        Cursor cursor = getContentResolver().query(Contract.Products.CONTENT_URI, projection, null , null, null);

        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
            String x = cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Description));
            if(x.contains(query)){
                productList.add(new Products(
                        cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1)),
                        cursor.getString(cursor.getColumnIndex(Contract.Products.Product_name)),
                        cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Description)),
                        cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price))
                ));
            }
        }
        cursor.close();

    }

}
