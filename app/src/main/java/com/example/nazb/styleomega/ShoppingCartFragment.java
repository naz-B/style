package com.example.nazb.styleomega;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.ListFragment;


import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends ListFragment implements OnItemClickListener {

    public static List<CartItems> productList;
    double total_price;
    int cart_id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_item_fragment, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getProductList();
        ShoppingList listViewAdapter = new ShoppingList(getActivity(), R.layout.cart_items, productList);;
        setListAdapter(listViewAdapter);
        getListView().setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + cart_id, Toast.LENGTH_SHORT).show();
    }

    public List<CartItems> getProductList() {

        String[] projectiond = {
                Contract.ShoppingCart.ShoppingCart_id,

        };
        SharedPreferences prefs = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);

        String selectiond = "username = '" + prefs.getString("username",null) +"' and status = 'ongoing'";
        Cursor cursord = getActivity().getContentResolver().query(Contract.ShoppingCart.CONTENT_URI, projectiond, selectiond , null, null);
        if(cursord != null && cursord.moveToFirst()){
            cart_id = Integer.parseInt(cursord.getString(cursord.getColumnIndex(Contract.ShoppingCart.ShoppingCart_id)));
        }


        productList = new ArrayList<>();
        int image_id = 0;
        int price = 0;
        String[] projection = {
                Contract.CartItem.CartItem_productname,
                Contract.CartItem.CartItem_qty,
                Contract.CartItem.CartItem_ID

        };
        String selection = "cart_id = '" + cart_id +"'";
        Cursor cursor = getActivity().getContentResolver().query(Contract.CartItem.CONTENT_URI, projection, selection , null, null);


        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
            String[] projections = {
                    Contract.Products.Product_Price,
                    Contract.Products.Product_Image1

            };
            String selections = "name = '" + cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_productname))+"'";
            Cursor cursors = getActivity().getContentResolver().query(Contract.Products.CONTENT_URI, projections, selections , null, null);
            for( cursors.moveToFirst(); !cursors.isAfterLast(); cursors.moveToNext() ) {
                price = Integer.parseInt(cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Price)));
                total_price += price;
                productList.add(new CartItems(
                        cart_id,
                        cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_productname)),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_qty))),
                        cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Image1)),
                        price, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_ID)))
                ));
            }
        }
        Toast.makeText(getActivity(),
                String.valueOf(cart_id), Toast.LENGTH_LONG).show();
        cursor.close();
        TextView total_prices = (TextView)getActivity().findViewById(R.id.priceTotal);

        total_prices.setText(String.valueOf(total_price));

        return productList;
    }


}
