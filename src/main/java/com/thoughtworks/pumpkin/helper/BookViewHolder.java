package com.thoughtworks.pumpkin.helper;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class BookViewHolder {
    public TextView rating;
    public TextView title;
    public TextView description;
    public TextView authors;
    public ImageButton wishListButton;
    public ImageView image;
    public ProgressBar spinner;
    public List<String> wishListBooks;
    public int position;
}
