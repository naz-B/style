package com.example.nazb.styleomega;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;


public class MyDBHandler extends SQLiteOpenHelper{



    private static final int Database_Version = 29;
    private static final String Database_Name = "StyleOmega.db";




    MyDBHandler(Context context) {
        super(context, Database_Name, null, Database_Version);
    }



    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //MyDBHandler dbHelper;

        addCustomersTable(sqLiteDatabase);
        addCategoriesTable(sqLiteDatabase);
        addProductsTable(sqLiteDatabase);
        addAddressTable(sqLiteDatabase);
        addProdComponentTable(sqLiteDatabase);
        addInquiryTable(sqLiteDatabase);
        addOrderTable(sqLiteDatabase);
        addFavouriteTable(sqLiteDatabase);
        addShoppingCartTable(sqLiteDatabase);
        addCartItemTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Products.Table_Product);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.ProdComponent.Table_ProdComponent);
        addProductsTable(sqLiteDatabase);
        addProdComponentTable(sqLiteDatabase);
      /*  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Customers.Table_Customers);
       sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Categories.Table_Category);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Products.Table_Product);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Address.Table_Address);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.ProdComponent.Table_ProdComponent);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Inquiry.Table_Inquiry);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Order.Table_Order);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.Favourite.Table_Favourite);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.ShoppingCart.Table_ShoppingCart);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.CartItem.Table_CartItem);
        addCustomersTable(sqLiteDatabase);
       addCategoriesTable(sqLiteDatabase);
        addProductsTable(sqLiteDatabase);
        addAddressTable(sqLiteDatabase);
        addProdComponentTable(sqLiteDatabase);
        addInquiryTable(sqLiteDatabase);
        addOrderTable(sqLiteDatabase);
        addFavouriteTable(sqLiteDatabase);
        addShoppingCartTable(sqLiteDatabase);
        addCartItemTable(sqLiteDatabase);*/
    }

    public void addCustomersTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.Customers.Table_Customers + "("
                + Contract.Customers._ID + " INTEGER PRIMARY KEY, "
                + Contract.Customers.Customer_fname + " TEXT, "
                + Contract.Customers.Customer_lname + " TEXT, "
                + Contract.Customers.Customer_email + " TEXT, "
                + Contract.Customers.Customer_pass + " TEXT, "
                + "SyncStatus TEXT " + ");");
    }

    public void addCategoriesTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.Categories.Table_Category + "("
                + Contract.Categories._ID + " INTEGER PRIMARY KEY, "
                + Contract.Categories.Category_name + " TEXT, "
                + Contract.Categories.Category_description + " TEXT, "
                + Contract.Categories.Category_maincategory + " TEXT "
                + "SyncStatus TEXT " + ");");
    }

    public void addProductsTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.Products.Table_Product + "("
                + Contract.Products._ID + " INTEGER PRIMARY KEY, "
                + Contract.Products.Product_ID + "INTEGER, "
                + Contract.Products.Product_name + " TEXT, "
                + Contract.Products.Product_Description + " TEXT, "
                + Contract.Products.Product_Price + " REAL, "
                + Contract.Products.Product_Image1 + " TEXT, "
                + Contract.Products.Product_Image2 + " TEXT, "
                + Contract.Products.Product_Image3 + " TEXT, "
                + Contract.Products.Product_Category + " TEXT, "
                + Contract.Products.Product_Status + " TEXT "
                + "SyncStatus TEXT " + ");");
    }



    public void addAddressTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.Address.Table_Address + "("
                + Contract.Address._ID + " INTEGER PRIMARY KEY, "
                + Contract.Address.Address_HouseNo + " TEXT, "
                + Contract.Address.Address_MainRoad + " TEXT, "
                + Contract.Address.Address_SubRoad + " TEXT, "
                + Contract.Address.Address_LandMark + " TEXT, "
                + Contract.Address.Address_City + " TEXT, "
                + Contract.Address.Address_ContactNo + " INTEGER, "
                + Contract.Address.Address_Username + " TEXT "
                + "SyncStatus TEXT " + ");");
    }

    public void addProdComponentTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.ProdComponent.Table_ProdComponent + "("
                + Contract.ProdComponent._ID + "INTEGER PRIMARY KEY, "
                + Contract.ProdComponent.ProdComponent_ProductName + " TEXT, "
                + Contract.ProdComponent.ProdComponent_colour + " TEXT, "
                + Contract.ProdComponent.ProdComponent_size + " TEXT, "
                + Contract.ProdComponent.ProdComponent_qty + " TEXT "
                + "SyncStatus TEXT " + ");");
    }

    public void addShoppingCartTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.ShoppingCart.Table_ShoppingCart + "("
                + Contract.ShoppingCart._ID + " INTEGER PRIMARY KEY, "
                + Contract.ShoppingCart.ShoppingCart_id + " TEXT, "
                + Contract.ShoppingCart.ShoppingCart_username + " TEXT, "
                + Contract.ShoppingCart.ShoppingCart_status + " TEXT "
                + "SyncStatus TEXT " + ");");
    }

    public void addCartItemTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.CartItem.Table_CartItem + "("
                + Contract.CartItem._ID + " INTEGER PRIMARY KEY, "
                + Contract.CartItem.CartItem_ID + " INTEGER, "
                + Contract.CartItem.CartItem_productname + " TEXT, "
                + Contract.CartItem.CartItem_qty + " TEXT, "
                + Contract.CartItem.CartItem_cartid + " TEXT "
                + "SyncStatus TEXT " + ");");
    }

    public void addInquiryTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.Inquiry.Table_Inquiry + "("
                + Contract.Inquiry._ID + " INTEGER PRIMARY KEY, "
                + Contract.Inquiry.Inquiry_id + " INTEGER, "
                + Contract.Inquiry.Inquiry_item + " TEXT, "
                + Contract.Inquiry.Inquiry_date + " TEXT, "
                + Contract.Inquiry.Inquiry_username + " TEXT, "
                + Contract.Inquiry.Inquiry_description + " TEXT, "
                + Contract.Inquiry.Inquiry_reply_date + " TEXT, "
                + Contract.Inquiry.Inquiry_reply_description + " TEXT "
                + "SyncStatus TEXT " + ");");
    }

    public void addOrderTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.Order.Table_Order + "("
                + Contract.Order._ID + " INTEGER PRIMARY KEY, "
                + Contract.Order.Order_ID + " INTEGER, "
                + Contract.Order.Order_date + " TEXT, "
                + Contract.Order.Order_Custuserame + " TEXT, "
                + Contract.Order.Order_item + " TEXT, "
                + Contract.Order.Order_status + " TEXT "
                + "SyncStatus TEXT " + ");");
    }

    public void addFavouriteTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ Contract.Favourite.Table_Favourite + "("
                + Contract.Favourite._ID + " INTEGER PRIMARY KEY, "
                + Contract.Favourite.Favourite_ID + " INTEGER, "
                + Contract.Favourite.Favourite_item + " TEXT, "
                + Contract.Favourite.Favourite_Custusername + " TEXT "
                + "SyncStatus TEXT " + ");");
    }




}
