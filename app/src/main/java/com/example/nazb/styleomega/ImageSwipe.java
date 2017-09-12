package com.example.nazb.styleomega;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import static java.security.AccessController.getContext;

/**
 * Created by nazB on 8/21/2017.
 */

public class ImageSwipe extends PagerAdapter {

    //private int[] image_resources = {R.drawable.image1, R.drawable.image2};
    private Context context;
    private LayoutInflater layoutInflater;


    public ImageSwipe(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return DetailScreen.image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.image_swiper, container, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.image_view);
        String url = DetailScreen.image_resources[position];
        Glide.with(context).load(url).into(imageView);
        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
