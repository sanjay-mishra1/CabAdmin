package com.example.sanjay.cabadmin.RecyclerUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sanjay.cabadmin.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class ViewHolder extends RecyclerView.ViewHolder {
    View view;
    ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view=itemView;
    }
    public void setTime(long time){
             try {
                 TextView textView=view.findViewById(R.id.trip_date);
                 Date date=new Date(time);
                 DateFormat format=new SimpleDateFormat("dd MMM yy HH:mm aaa",Locale.ENGLISH);
                 format.setTimeZone(TimeZone.getTimeZone("UTC"));
                 textView.setText(format.format(date));
             } catch (Exception ignored) {
             }

    }
    public  void setTripCost(int cost){
        TextView textView=view.findViewById(R.id.trip_cost);
        textView.append(getFormatedAmount(cost));
    }
    private String getFormatedAmount(long amount){
        return NumberFormat.getNumberInstance(Locale.UK).format(amount)+".00";
    }
}
