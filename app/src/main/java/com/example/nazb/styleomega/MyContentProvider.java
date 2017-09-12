package com.example.nazb.styleomega;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static java.nio.channels.DatagramChannel.open;

/**
 * Created by nazB on 8/19/2017.
 */

public class MyContentProvider extends ContentProvider {

    private MyDBHandler dbHelper;

    private static final int CUSTOMER = 100;
    private static final int CUSTOMER_ID = 101;
    private static final int CATEGORY = 200;
    private static final int CATEGORY_ID = 201;
    private static final int PRODUCT = 300;
    private static final int PRODUCT_ID = 301;
    private static final int ADDRESS = 400;
    private static final int ADDRESS_ID = 401;
    private static final int PRODCOMPONENT = 500;
    private static final int PRODCOMPONENT_ID = 501;
    private static final int SHOPPINGCART = 600;
    private static final int SHOPPINGCART_ID = 601;
    private static final int CARTITEM = 700;
    private static final int CARTITEM_ID = 701;
    private static final int INQUIRY = 800;
    private static final int INQUIRY_ID = 801;
    private static final int ORDER = 900;
    private static final int ORDER_ID = 901;
    private static final int FAVOURITE = 110;
    private static final int FAVOURITE_ID = 111;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        dbHelper = new MyDBHandler(getContext());
        return true;

    }

    public static UriMatcher buildUriMatcher(){
        String content = Contract.CONTENT_AUTHORITY;
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, Contract.PATH_CUSTOMERS, CUSTOMER);
        matcher.addURI(content, Contract.PATH_CUSTOMERS + "/#", CUSTOMER_ID);
        matcher.addURI(content, Contract.PATH_GATEGORIES, CATEGORY);
        matcher.addURI(content, Contract.PATH_GATEGORIES + "/#", CATEGORY_ID);
        matcher.addURI(content, Contract.PATH_PRODUCTS, PRODUCT);
        matcher.addURI(content, Contract.PATH_PRODUCTS + "/#", PRODUCT_ID);
        matcher.addURI(content, Contract.PATH_ADDRESS, ADDRESS);
        matcher.addURI(content, Contract.PATH_ADDRESS + "/#", ADDRESS_ID);
        matcher.addURI(content, Contract.PATH_PRODCOMPONENT, PRODCOMPONENT);
        matcher.addURI(content, Contract.PATH_PRODCOMPONENT + "/#", PRODCOMPONENT_ID);
        matcher.addURI(content, Contract.PATH_SHOPPINGCART, SHOPPINGCART);
        matcher.addURI(content, Contract.PATH_SHOPPINGCART + "/#", SHOPPINGCART_ID);
        matcher.addURI(content, Contract.PATH_CARTITEMS, CARTITEM);
        matcher.addURI(content, Contract.PATH_CARTITEMS + "/#", CARTITEM_ID);
        matcher.addURI(content, Contract.PATH_INQUIRIES, INQUIRY);
        matcher.addURI(content, Contract.PATH_INQUIRIES + "/#", INQUIRY_ID);
        matcher.addURI(content, Contract.PATH_ORDERS, ORDER);
        matcher.addURI(content, Contract.PATH_ORDERS + "/#", ORDER_ID);
        matcher.addURI(content, Contract.PATH_FAVOURITE, FAVOURITE);
        matcher.addURI(content, Contract.PATH_FAVOURITE + "/#", FAVOURITE_ID);
        return matcher;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = null;

        switch (uriMatcher.match(uri)) {
            case CUSTOMER:
                c = db.query(
                        Contract.Customers.Table_Customers,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CUSTOMER_ID:
                long _id = ContentUris.parseId(uri);
                c = db.query(
                        Contract.Customers.Table_Customers,
                        projection,
                        Contract.Customers.Customer_email + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY:
                c = db.query(
                        Contract.Categories.Table_Category,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY_ID:
                long _iid = ContentUris.parseId(uri);
                c = db.query(
                        Contract.Categories.Table_Category,
                        projection,
                        Contract.Categories.Category_maincategory + " = ?",
                        new String[]{String.valueOf(_iid)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT:
                c = db.query(
                        Contract.Products.Table_Product,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT_ID:
                long _iid1 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.Products.Table_Product,
                        projection,
                        Contract.Products.Product_Category + " = ?",
                        new String[]{String.valueOf(_iid1)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case ADDRESS:
                c = db.query(
                        Contract.Address.Table_Address,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ADDRESS_ID:
                long _iid12 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.Address.Table_Address,
                        projection,
                        Contract.Address.Address_Username + " = ?",
                        new String[]{String.valueOf(_iid12)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODCOMPONENT:
                c = db.query(
                        Contract.ProdComponent.Table_ProdComponent,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODCOMPONENT_ID:
                long _iid13 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.ProdComponent.Table_ProdComponent,
                        projection,
                        Contract.ProdComponent.ProdComponent_ProductName + " = ?",
                        new String[]{String.valueOf(_iid13)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case SHOPPINGCART:
                c = db.query(
                        Contract.ShoppingCart.Table_ShoppingCart,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SHOPPINGCART_ID:
                long _iid14 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.ShoppingCart.Table_ShoppingCart,
                        projection,
                        Contract.ShoppingCart.ShoppingCart_id + " = ?",
                        new String[]{String.valueOf(_iid14)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CARTITEM:
                c = db.query(
                        Contract.CartItem.Table_CartItem,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CARTITEM_ID:
                long _iid15 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.CartItem.Table_CartItem,
                        projection,
                        Contract.CartItem.CartItem_productname + " = ?",
                        new String[]{String.valueOf(_iid15)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case INQUIRY:
                c = db.query(
                        Contract.Inquiry.Table_Inquiry,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case INQUIRY_ID:
                long _iid16 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.Inquiry.Table_Inquiry,
                        projection,
                        Contract.Inquiry.Inquiry_item + " = ?",
                        new String[]{String.valueOf(_iid16)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case ORDER:
                c = db.query(
                        Contract.Order.Table_Order,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ORDER_ID:
                long _iid17 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.Order.Table_Order,
                        projection,
                        Contract.Order.Order_Custuserame + " = ?",
                        new String[]{String.valueOf(_iid17)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVOURITE:
                c = db.query(
                        Contract.Favourite.Table_Favourite,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVOURITE_ID:
                long _iid18 = ContentUris.parseId(uri);
                c = db.query(
                        Contract.Favourite.Table_Favourite,
                        projection,
                        Contract.Favourite.Favourite_Custusername + " = ?",
                        new String[]{String.valueOf(_iid18)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(uriMatcher.match(uri)){
            case CUSTOMER:
                return Contract.Customers.CONTENT_TYPE;
            case CUSTOMER_ID:
                return Contract.Customers.CONTENT_ITEM_TYPE;
            case CATEGORY:
                return Contract.Categories.CONTENT_TYPE;
            case CATEGORY_ID:
                return Contract.Categories.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id;
        Uri _uri;

        switch(uriMatcher.match(uri)){
            case CUSTOMER:
                _id = db.insert(Contract.Customers.Table_Customers, null, contentValues);
                if(_id > 0){
                    _uri =  Contract.Customers.getCustomerUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case CATEGORY:
                _id = db.insert(Contract.Categories.Table_Category, null, contentValues);
                if(_id > 0){
                    _uri = Contract.Categories.getCategoryUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case PRODUCT:
                _id = db.insert(Contract.Products.Table_Product, null, contentValues);
                if(_id > 0){
                    _uri = Contract.Products.getProductUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case ADDRESS:
                _id = db.insert(Contract.Address.Table_Address, null, contentValues);
                if(_id > 0){
                    _uri = Contract.Address.getAddressUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case PRODCOMPONENT:
                _id = db.insert(Contract.ProdComponent.Table_ProdComponent, null, contentValues);
                if(_id > 0){
                    _uri = Contract.ProdComponent.getProdComponentUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case SHOPPINGCART:
                _id = db.insert(Contract.ShoppingCart.Table_ShoppingCart, null, contentValues);
                if(_id > 0){
                    _uri = Contract.ShoppingCart.getShoppingCartUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case CARTITEM:
                _id = db.insert(Contract.CartItem.Table_CartItem, null, contentValues);
                if(_id > 0){
                    _uri = Contract.CartItem.getCartItemUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case INQUIRY:
                _id = db.insert(Contract.Inquiry.Table_Inquiry, null, contentValues);
                if(_id > 0){
                    _uri = Contract.Inquiry.getInquiryUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case ORDER:
                _id = db.insert(Contract.Order.Table_Order, null, contentValues);
                if(_id > 0){
                    _uri = Contract.Order.getOrderUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case FAVOURITE:
                _id = db.insert(Contract.Favourite.Table_Favourite, null, contentValues);
                if(_id > 0){
                    _uri = Contract.Favourite.getFavouriteUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return _uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;

        switch (uriMatcher.match(uri)) {
            case CUSTOMER:
                rows = db.delete(Contract.Customers.Table_Customers, selection, selectionArgs);
                break;
            case CATEGORY:
                rows = db.delete(Contract.Categories.Table_Category, selection, selectionArgs);
                break;
            case PRODUCT:
                rows = db.delete(Contract.Products.Table_Product, selection, selectionArgs);
                break;
            case ADDRESS:
                rows = db.delete(Contract.Address.Table_Address, selection, selectionArgs);
                break;
            case FAVOURITE:
                rows = db.delete(Contract.Favourite.Table_Favourite, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;

        switch(uriMatcher.match(uri)){
            case CUSTOMER:
                rows = db.update(Contract.Customers.Table_Customers, contentValues, selection, selectionArgs);
                break;
            case ADDRESS:
                rows = db.update(Contract.Address.Table_Address, contentValues, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                rows = db.update(Contract.Customers.Table_Customers, contentValues, Contract.Customers.Customer_email + " = ?", selectionArgs);
                break;
            case CATEGORY:
                rows = db.update(Contract.Categories.Table_Category, contentValues, selection, selectionArgs);
                break;
            case SHOPPINGCART:
                rows = db.update(Contract.ShoppingCart.Table_ShoppingCart, contentValues, selection, selectionArgs);
                break;
            case INQUIRY:
                rows = db.update(Contract.Inquiry.Table_Inquiry, contentValues, selection, selectionArgs);
                break;
            case ORDER:
                rows = db.update(Contract.Order.Table_Order, contentValues, selection, selectionArgs);
                break;
            case PRODCOMPONENT:
                rows = db.update(Contract.ProdComponent.Table_ProdComponent, contentValues, selection, selectionArgs);
                break;
            case FAVOURITE:
                rows = db.update(Contract.Favourite.Table_Favourite, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rows;
    }
}
