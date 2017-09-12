package com.example.nazb.styleomega;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by nazB on 8/28/2017.
 */

public class ShoppingList extends ArrayAdapter<CartItems> {



    public ShoppingList(@NonNull Context context, @LayoutRes int resource, @NonNull List<CartItems> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
     // View v = convertView;
       if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.cart_items, null);
        }
        CartItems cartItems = getItem(position);
        ImageView img = (ImageView) convertView.findViewById(R.id.shopImageView);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.shopTitle);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.shopPrice);
        TextView txtqty = (TextView) convertView.findViewById(R.id.shopQty);
        TextView txtitemid = (TextView)convertView.findViewById(R.id.item_id);
        String url = cartItems.getProduct_image();
        Glide.with(getContext()).load(url).into(img);


        //img.setImageResource(cartItems.getProduct_image());
        txtTitle.setText(cartItems.getProduct_name());
        txtPrice.setText("LKR "+cartItems.getProduct_price());
        txtqty.setText(String.valueOf(cartItems.getQty()));
        txtitemid.setText(String.valueOf(cartItems.getItem_id()));
        return convertView;
    }
}
