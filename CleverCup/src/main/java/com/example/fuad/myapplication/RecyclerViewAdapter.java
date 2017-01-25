package com.example.fuad.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    int size;
    Context context1;

    public RecyclerViewAdapter(Context context2,int size){
        this.size = size;
        context1 = context2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageview;
        public ViewHolder(View v){
            super(v);
            imageview = (ImageView) v.findViewById(R.id.imageview1);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view1 = LayoutInflater.from(context1).inflate(R.layout.recycler_view_items,parent,false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        Vholder.imageview.setImageResource(R.drawable.coffee);
     ;
    }

    @Override
    public int getItemCount(){
        return size;
    }
}