package com.example.nazb.styleomega;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class Inquiries extends AppCompatActivity {

    String item_name;
    List<Products> productList;
    List <Inquiry> inquiryList;
    int inquiryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiries);
        Intent intent = getIntent();
        item_name = intent.getExtras().getString("Prod_name");
        setProductDetails();
        setInquiryList();
        final SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String today = dateFormat.format(date);

        Button sendBtn = (Button)findViewById(R.id.inquiries_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Inquiries.this, today, Toast.LENGTH_LONG).show();

                EditText inquiry_text = (EditText)findViewById(R.id.inquiries_text);
                if(inquiry_text.getText().toString().equals(null) || inquiry_text.getText().toString().equals("")){
                    Snackbar.make(findViewById(R.id.inqruie), "Please write your inquiry", Snackbar.LENGTH_LONG).show();
                }
                else{
                    String inquiry_text_string = inquiry_text.getText().toString();
                    setInquiryID();
                    ContentValues values = new ContentValues();
                    values.put(Contract.Inquiry.Inquiry_id, inquiryID);
                    values.put(Contract.Inquiry.Inquiry_date, today);
                    values.put(Contract.Inquiry.Inquiry_item, item_name);
                    values.put(Contract.Inquiry.Inquiry_username, prefs.getString("username",null));
                    values.put(Contract.Inquiry.Inquiry_description, inquiry_text_string);
                    getContentResolver().insert(Contract.Inquiry.CONTENT_URI,values);
                    Intent intent = new Intent(Inquiries.this, Inquiries.class);
                    intent.putExtra("Prod_name", item_name);
                    startActivity(intent);
                }


            }
        });
    }

    public void setProductDetails(){
        productList = new ArrayList<>();

        String[] projection = {
                Contract.Products.Product_name,
                Contract.Products.Product_Description,
                Contract.Products.Product_Price,
                Contract.Products.Product_Image1

        };
        String selection = "name = '" + item_name +"'";
        Cursor cursor = getContentResolver().query(Contract.Products.CONTENT_URI, projection, selection , null, null);
        if(cursor != null && cursor.moveToFirst()){
           // while(cursor.moveToNext()){
              //  for(int i = 0; i < cursor.getCount(); i++){
                    //String imagepath = cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1));
                  //  int image_id = this.getResources().getIdentifier(imagepath, null, null);
                    productList.add(new Products(
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1)),
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_name)),
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Description)),
                            cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price))
                    ));
                   // cursor.moveToPosition(i);
                //}
                TextView txtTitle = (TextView)findViewById(R.id.inquiry_txtTitle);
                TextView txtDescription = (TextView)findViewById(R.id.inquiry_txtDescription);
                TextView txtPrice = (TextView)findViewById(R.id.inquiry_txtPrice);

                ImageView image = (ImageView)findViewById(R.id.inquiry_imageView);


               // txtTitle.setText(cursor.getString(cursor.getColumnIndex(Contract.Products.Product_name)));
                txtTitle.setText(item_name);
                txtDescription.setText(cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Description)));
                txtPrice.setText(cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Price)));

                String url = cursor.getString(cursor.getColumnIndex(Contract.Products.Product_Image1));

                Glide.with(Inquiries.this).load(url).into(image);
               // image.setImageResource(image_id);
           // }
        }
        cursor.close();


    }

    public void setInquiryList(){
        getInquiryList();
        ListView inquiry_list = (ListView)findViewById(R.id.inquiries_list);
        InquiryList listAdapter = new InquiryList(this, R.layout.inquiry_style, inquiryList);
        inquiry_list.setAdapter(listAdapter);
    }

    public List<Inquiry> getInquiryList(){
        inquiryList = new ArrayList<>();
        String[] projection = {
                Contract.Inquiry.Inquiry_id,
                Contract.Inquiry.Inquiry_date,
                Contract.Inquiry.Inquiry_username,
                Contract.Inquiry.Inquiry_description,
                Contract.Inquiry.Inquiry_reply_date,
                Contract.Inquiry.Inquiry_reply_description,
                Contract.Inquiry.Inquiry_item

        };
        String selection = "itemname = '" + item_name + "'";
        Cursor cursor = getContentResolver().query(Contract.Inquiry.CONTENT_URI, projection, selection , null, null);
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
          // while(cursor.moveToNext()){
                //for(int i = 0; i < cursor.getCount(); i++){
                    inquiryList.add(new Inquiry(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_id))),
                            cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_username)),
                            cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_item)),
                            cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_description)),
                            cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_date)),
                            cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_reply_description)),
                            cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_reply_date))
                    ));
                   // cursor.moveToPosition(i);
               // }
          // }
        }
        cursor.close();

        return inquiryList;
    }

    public void setInquiryID(){
        String[] projection = {
                Contract.Inquiry.Inquiry_id,
        };
        Cursor cursor = getContentResolver().query(Contract.Inquiry.CONTENT_URI, projection, null , null, null);
        if(cursor != null && cursor.moveToFirst()){
            cursor.moveToLast();
            inquiryID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.Inquiry.Inquiry_id)))+1;
         }


    }


}
