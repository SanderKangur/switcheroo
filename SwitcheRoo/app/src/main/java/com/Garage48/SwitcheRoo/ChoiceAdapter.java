package com.Garage48.SwitcheRoo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

class ChoiceAdapter extends ArrayAdapter<String> {

    Context context;
    String[] names;
    int[] images;
    LayoutInflater inflater;

    public ChoiceAdapter(Context context, String[] name, int[] images){
        super(context, R.layout.item_row, name);
        Log.i("pask", "constructor");
        this.context = context;
        this.names = name;
        this.images = images;

    }


    public class ViewHolder{
        TextView desc;
        ImageView pic;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Log.i("pask", "kek1");

        if(view == null){
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_row, null);
        }

        ViewHolder holder = new ViewHolder();
        holder.desc = (TextView) view.findViewById(R.id.itemDesc);
        holder.pic = (ImageView) view.findViewById(R.id.itemPic);

        holder.desc.setText(names[i]);
        holder.pic.setImageResource(images[i]);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable background = v.getBackground();
                if (background instanceof ColorDrawable) {
                    if (((ColorDrawable) background).getColor() == Color.GREEN) {
                        v.setBackgroundColor(Color.WHITE);
                    } else {
                        v.setBackgroundColor(Color.GREEN);
                    }
                }
                else{
                    v.setBackgroundColor(Color.GREEN);
                }
            }
        });


        return view;
    }
}
