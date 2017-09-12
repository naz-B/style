package com.example.nazb.styleomega;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by nazB on 8/29/2017.
 */

public class ShoppingListCheckout extends ArrayAdapter<CartItems> {


    public ShoppingListCheckout(@NonNull Context context, @LayoutRes int resource, @NonNull List<CartItems> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.cart_items_final, null);
        }
        CartItems cartItems = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.shopImageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.shopTitle);
        TextView txtPrice = (TextView) v.findViewById(R.id.shopPrice);
        TextView txtqty = (TextView) v.findViewById(R.id.shopQty);
        TextView txtitemid = (TextView)v.findViewById(R.id.item_id);
        String url = cartItems.getProduct_image();
        Glide.with(getContext()).load(url).into(img);

       // img.setImageResource(cartItems.getProduct_image());
        txtTitle.setText(cartItems.getProduct_name());
        txtPrice.setText("LKR "+cartItems.getProduct_price());
        txtqty.setText(String.valueOf(cartItems.getQty()));
        txtitemid.setText(String.valueOf(cartItems.getItem_id()));
        return v;
    }
}
