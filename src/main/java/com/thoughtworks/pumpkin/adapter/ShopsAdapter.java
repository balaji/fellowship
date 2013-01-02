package com.thoughtworks.pumpkin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.thoughtworks.pumpkin.R;

import java.util.List;

public class ShopsAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private List<T> objects;

    public ShopsAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.spin_shop_rowitem, parent, false);
        TextView textView = (TextView) v.findViewById(R.id.text1);
        textView.setTextColor(Color.BLACK);
        textView.setText((String) objects.get(position));
        parent.setVerticalScrollBarEnabled(false);
        return v;
    }
}
