package com.example.nazb.styleomega;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazB on 8/29/2017.
 */

public class CheckoutFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public static List<CartItems> productList;
    double total_price;
    int cart_id = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_items_final, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        getProductList();
        ShoppingListCheckout listViewAdapter = new ShoppingListCheckout(getActivity(), R.layout.cart_items_final, productList);;
        setListAdapter(listViewAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

    }

    public List<CartItems> getProductList() {

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
        if(cursor != null && cursor.moveToFirst()){
            while(cursor.moveToNext()){
                for(int i = 0; i < cursor.getCount(); i++){
                    String[] projections = {
                            Contract.Products.Product_Price,
                            Contract.Products.Product_Image1

                    };
                    String selections = "name = '" + cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_productname))+"'";
                    Cursor cursors = getActivity().getContentResolver().query(Contract.Products.CONTENT_URI, projections, selections , null, null);
                    if(cursors != null && cursors.moveToFirst()) {
                      //  String imagepath = cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Image1));
                      //  image_id = this.getResources().getIdentifier(imagepath, null, null);
                        price = Integer.parseInt(cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Price)));
                        total_price += price;

                        productList.add(new CartItems(

                                cart_id,
                                cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_productname)),
                                Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_qty))),
                                cursors.getString(cursors.getColumnIndex(Contract.Products.Product_Image1)),
                                price, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.CartItem.CartItem_ID)))
                        ));
                        cursor.moveToPosition(i);
                    }
                }
            }
        }
        cursor.close();
        TextView total_prices = (TextView)getActivity().findViewById(R.id.priceTotal);

        total_prices.setText(String.valueOf(total_price));

        return productList;
    }
}
