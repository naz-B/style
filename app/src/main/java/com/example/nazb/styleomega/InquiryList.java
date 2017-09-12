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

import java.util.List;

/**
 * Created by nazB on 8/30/2017.
 */

public class InquiryList extends ArrayAdapter<Inquiry> {

    public InquiryList(@NonNull Context context, @LayoutRes int resource, @NonNull List<Inquiry> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.inquiry_style, null);
        }
        Inquiry inquiry = getItem(position);
        TextView user_name = (TextView) v.findViewById(R.id.inquiry_username);
        TextView date = (TextView) v.findViewById(R.id.inquiry_date);
        TextView txtDesc = (TextView) v.findViewById(R.id.inquiry_desc);
        TextView date1 = (TextView) v.findViewById(R.id.inquiry_date1);
        TextView txtDesc1 = (TextView) v.findViewById(R.id.inquiry_desc1);
        TextView admin = (TextView)v.findViewById(R.id.inquiry_admin);
        user_name.setText(inquiry.getUser_name());
        date.setText(inquiry.getDate());
        txtDesc.setText(inquiry.getDescription());
        if(inquiry.getReply_date() != null && inquiry.getReply_description() != null){
            date1.setText(inquiry.getReply_date());
            txtDesc1.setText(inquiry.getReply_description());
            admin.setText("StyleOmega");
        }

        return v;
    }
}
