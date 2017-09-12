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
 * Created by nazB on 8/27/2017.
 */

public class AddressList extends ArrayAdapter<Address> {


    public AddressList(@NonNull Context context, @LayoutRes int resource, @NonNull List<Address> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.address_list, null);
        }
        Address address = getItem(position);
        TextView txtHouseNo = (TextView) v.findViewById(R.id.txtHouse);
        TextView txtMainRoad = (TextView) v.findViewById(R.id.txtMainRoad);
        TextView txtSubRoad = (TextView) v.findViewById(R.id.txtSubRoad);
        TextView txtLandMark = (TextView) v.findViewById(R.id.txtLandmark);
        TextView txtCity = (TextView) v.findViewById(R.id.txtCity);
        TextView txtContact = (TextView) v.findViewById(R.id.txtContact);
        txtHouseNo.setText(address.getHouse_no());
        txtMainRoad.setText(address.getMain_road());
        txtSubRoad.setText(address.getSub_road());
        txtLandMark.setText(address.getLandmark());
        txtCity.setText(address.getCity());
        txtContact.setText(address.getContact_no());
        return v;
    }
}
