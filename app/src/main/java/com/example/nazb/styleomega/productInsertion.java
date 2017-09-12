package com.example.nazb.styleomega;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class productInsertion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_insertion);
        Button add =  (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                String username = prefs.getString("username",null);

                EditText prodname  = (EditText)findViewById(R.id.catName);
                EditText prodDesc  = (EditText)findViewById(R.id.catDesc);
                EditText price = (EditText)findViewById(R.id.catMain);
                EditText category = (EditText)findViewById(R.id.editText4);

                EditText image1 = (EditText)findViewById(R.id.editText5);
                EditText image2 = (EditText)findViewById(R.id.editText6);
                EditText image3 = (EditText)findViewById(R.id.editText9);
                EditText status = (EditText)findViewById(R.id.editText);
                EditText id = (EditText)findViewById(R.id.editText10);

                String catnamestring = prodname.getText().toString();
                String catdescstring = prodDesc.getText().toString();
                String catmainstring = price.getText().toString();
                String qty1 = category.getText().toString();
                String image1string = image1.getText().toString();
                String image2string = image2.getText().toString();
                String image3string = image3.getText().toString();
                String statusstring = status.getText().toString();
                String statussid = id.getText().toString();


                ContentValues values = new ContentValues();
                values.put(Contract.ProdComponent.ProdComponent_ProductName, catnamestring);
                values.put(Contract.ProdComponent.ProdComponent_size, catdescstring);
                values.put(Contract.ProdComponent.ProdComponent_colour, catmainstring);
                values.put(Contract.ProdComponent.ProdComponent_qty, qty1);
                //values.put(Contract.Products.Product_Image1, "https://firebasestorage.googleapis.com/v0/b/myapplication2-14514.appspot.com/o/image3.jpg?alt=media&token=0ee4fa80-9657-4a09-9f2b-f3c582692c16");
               // values.put(Contract.Products.Product_Image2, "https://firebasestorage.googleapis.com/v0/b/myapplication2-14514.appspot.com/o/image4.jpg?alt=media&token=b057182a-b43c-4784-be8d-9d34f5ee30ff");
               // values.put(Contract.Products.Product_Image3, image3string);
               // values.put(Contract.Products.Product_Status, statusstring);

                getContentResolver().insert(Contract.ProdComponent.CONTENT_URI,values);

                Intent intent = new Intent(productInsertion.this, productInsertion.class);
                startActivity(intent);*/

                SyncMySQL sqlmysql = new SyncMySQL();
              //  if(sqlmysql.checkNetwork()){
                    sqlmysql.syncSQLiteMySQLDB(productInsertion.this);
              //  }
              //  else{
             //      // Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
              //  }

            }
        });
    }

    public void test(View v){

    }

}
