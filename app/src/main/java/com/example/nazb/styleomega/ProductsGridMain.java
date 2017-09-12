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

public class ProductsGridMain extends ArrayAdapter<Products> {

    public ProductsGridMain(@NonNull Context context, @LayoutRes int resource, @NonNull List<Products> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_product_grid_main, null);
        }
        Products product = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);


        TextView txtPrice = (TextView) v.findViewById(R.id.txtPrice);
        String url = product.getProd_image();

        Glide.with(getContext()).load(url).into(img);

        // img.setImageResource(product.getProd_image());


        txtPrice.setText("LKR "+product.getProduct_price());

        return v;
    }
}
