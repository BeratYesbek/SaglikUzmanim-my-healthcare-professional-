package com.example.salikuzmanim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salikuzmanim.R;

import java.util.ArrayList;

public class AdapterGridViewForHomeFragment  extends BaseAdapter {
    private Context _context;
    private String []  _text;
    private int [] _imageNumber;
    private LayoutInflater layoutInflater;
    TextView textView;
    ImageView imageView;

    public AdapterGridViewForHomeFragment(String [] text,int [] _imageNumber,Context context){
        this._text = text;
        this._imageNumber=_imageNumber;
        this._context = context;
    }

    @Override
    public int getCount() {
        return _text.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = layoutInflater.inflate(R.layout.home_grid_view,null);
        }
        textView = view.findViewById(R.id.text_home);
        imageView = view.findViewById(R.id.image_view_gridView);
        imageView.setBackgroundResource(_imageNumber[i]);
        textView.setText(_text[i]);
        return  view;
   }
}
