package com.example.nazb.styleomega;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllCategory extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Categories");
        ListView allCatList = (ListView)findViewById(R.id.allcat);


        String[] projection = {
                Contract.Categories.Category_description,
                Contract.Categories.Category_name,

        };
        Cursor cursor = getContentResolver().query(Contract.Categories.CONTENT_URI, projection, null , null, "Maincategory");
        String [] cats = new String [cursor.getCount()];
        if(cursor != null && cursor.moveToFirst()){
            while(cursor.moveToNext()){
                for(int i = 0; i < cursor.getCount(); i++){
                    cats[i] = cursor.getString(cursor.getColumnIndex(Contract.Categories.Category_description));
                    cursor.moveToPosition(i);
                }
            }
        }
        cursor.close();
        ArrayList <String> mens = new ArrayList<>();
        ArrayList <String> womens = new ArrayList<>();
        ArrayList <String> kids = new ArrayList<>();
        for (String x : cats) {
            if (x.contains("Womens")) {
                womens.add(x);
            } else if (x.contains("Kids")) {
                kids.add(x);
            } else {
                mens.add(x);
            }
        }

        Intent intent = getIntent();
        String ids = intent.getStringExtra(MainScreen.ID);

        switch (ids) {
            case "all": {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, cats);
                allCatList.setAdapter(adapter);

                break;
            }
            case "men": {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, mens);
                allCatList.setAdapter(adapter);
                break;
            }
            case "women": {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, womens);
                allCatList.setAdapter(adapter);
                break;
            }
            default: {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, kids);
                allCatList.setAdapter(adapter);

                break;
            }
        }
        final ListView listView = (ListView)findViewById(R.id.allcat);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(AllCategory.this, ProductsView.class);
                intent.putExtra("Clicked_Category", name);
                startActivity(intent);

            }
        });


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

                Intent intent = new Intent(AllCategory.this, SearchResult.class);
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


}
