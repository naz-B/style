package com.example.nazb.styleomega;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nazB on 8/19/2017.
 */

public class Contract {

    public static final String CONTENT_AUTHORITY  = "com.example.nazb.styleomega.styleomegadatabase";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CUSTOMERS = "Customers";
    public static final String PATH_GATEGORIES = "Category";
    public static final String PATH_PRODUCTS = "Products";
    public static final String PATH_ADDRESS = "Address";
    public static final String PATH_PRODCOMPONENT = "ProdComponent";
    public static final String PATH_SHOPPINGCART = "ShoppingCart";
    public static final String PATH_CARTITEMS = "CartItem";
    public static final String PATH_INQUIRIES = "Inquiry";
    public static final String PATH_ORDERS = "Order";
    public static final String PATH_FAVOURITE = "Facourite";

    public static final class Customers implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CUSTOMERS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_CUSTOMERS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CUSTOMERS;


        public static final String Table_Customers = "Customers";
        public static final String Customer_fname = "fname";
        public static final String Customer_lname = "lname";
        public static final String Customer_email = "email";
        public static final String Customer_pass = "pass";

        public static Uri getCustomerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class Categories implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GATEGORIES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_GATEGORIES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_GATEGORIES;

        public static final String Table_Category = "Category";
        public static final String Category_name = "name";
        public static final String Category_maincategory = "Maincategory";
        public static final String Category_description = "description";


        public static Uri getCategoryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class Products implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRODUCTS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_PRODUCTS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_PRODUCTS;

        public static final String Table_Product = "Product";
        public static final String Product_ID = "prod_id";
        public static final String Product_name = "name";
        public static final String Product_Description = "Description";
        public static final String Product_Price = "Price";
        public static final String Product_Image1 = "Image1";
        public static final String Product_Image2 = "Image2";
        public static final String Product_Image3 = "Image3";
        public static final String Product_Status = "Status";
        public static final String Product_Category = "Category";


        public static Uri getProductUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }



    public static final class Address implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ADDRESS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ADDRESS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ADDRESS;

        public static final String Table_Address = "Address";
        public static final String Address_Username = "username";
        public static final String Address_HouseNo = "house_no";
        public static final String Address_MainRoad = "main_road";
        public static final String Address_SubRoad= "sub_road";
        public static final String Address_LandMark = "landmark";
        public static final String Address_City = "city";
        public static final String Address_ContactNo = "contact_no";


        public static Uri getAddressUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ProdComponent implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRODCOMPONENT).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_PRODCOMPONENT;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_PRODCOMPONENT;

        public static final String Table_ProdComponent = "ProdComponent";
        public static final String ProdComponent_ProductName = "product_name";
        public static final String ProdComponent_size = "size";
        public static final String ProdComponent_colour = "colour";
        public static final String ProdComponent_qty= "qty";


        public static Uri getProdComponentUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class ShoppingCart implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHOPPINGCART).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_SHOPPINGCART;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_SHOPPINGCART;

        public static final String Table_ShoppingCart = "ShoppingCart";
        public static final String ShoppingCart_id= "cart_id";
        public static final String ShoppingCart_username = "username";
        public static final String ShoppingCart_status = "status";


        public static Uri getShoppingCartUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CartItem implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARTITEMS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_CARTITEMS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CARTITEMS;

        public static final String Table_CartItem = "CartItem";
        public static final String CartItem_ID = "item_id";
        public static final String CartItem_productname = "product_name";
        public static final String CartItem_qty = "qty";
        public static final String CartItem_cartid = "cart_id";

        public static Uri getCartItemUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class Inquiry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INQUIRIES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_INQUIRIES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_INQUIRIES;

        public static final String Table_Inquiry = "Inquiry";
        public static final String Inquiry_id = "inquiry_id";
        public static final String Inquiry_item = "itemname";
        public static final String Inquiry_username = "username";
        public static final String Inquiry_description = "description";
        public static final String Inquiry_date = "date";
        public static final String Inquiry_reply_description = "reply_description";
        public static final String Inquiry_reply_date = "reply_date";


        public static Uri getInquiryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class Order implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ORDERS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ORDERS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ORDERS;

        public static final String Table_Order = "Orders";
        public static final String Order_ID = "Order_id";
        public static final String Order_Custuserame = "username";
        public static final String Order_item = "itemname";
        public static final String Order_status = "status";
        public static final String Order_date = "date";


        public static Uri getOrderUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class Favourite implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_FAVOURITE;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_FAVOURITE;

        public static final String Table_Favourite = "Favourite";
        public static final String Favourite_ID = "Favourite_id";
        public static final String Favourite_item = "itemname";
        public static final String Favourite_Custusername = "username";


        public static Uri getFavouriteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
