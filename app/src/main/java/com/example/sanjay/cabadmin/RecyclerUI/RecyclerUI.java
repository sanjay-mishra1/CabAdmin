package com.example.sanjay.cabadmin.RecyclerUI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sanjay.cabadmin.R;
import com.google.firebase.database.DataSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerUI {
    private   FilterAdapter adapter;
    private Context context;
     private List<Journey> journeyList;
    public RecyclerUI(DataSnapshot dataSnapshot, Context context, RecyclerView recyclerView, TextView costText,TextView totalTripText){
         this.context=context;
         journeyList=new ArrayList<>();
        adapter= new FilterAdapter(journeyList);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        totalTripText.setText("From "+dataSnapshot.getChildrenCount()+" trips");
        setRecyclerUI(dataSnapshot,costText);

      }
        private void setRecyclerUI(DataSnapshot dataSnapshot, TextView costText){
        long cost=0;
            for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren())
            {
                Journey  journey=dataSnapshot2.getValue(Journey.class);
                cost+=journey.getTrip_cost();
                journeyList.add(journey);
                 adapter.notifyDataSetChanged();
            }
            costText.setText(getFormatedAmount(cost));
        }
    private String getFormatedAmount(long amount){
        return "₹ "+NumberFormat.getNumberInstance(Locale.UK).format(amount)+".00";
    }
      class FilterAdapter extends RecyclerView.Adapter<ViewHolder> {
          List<Journey> dataList;
          FilterAdapter(List<Journey> data){
              this.dataList=data;
          }
          @NonNull
          @Override
          public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
              return new ViewHolder( LayoutInflater.from(context).inflate(R.layout.user_layout,viewGroup,false));

          }

          @Override
          public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
              //set all data
              Log.e("Recycle","Inside onbindviewholder");
              Journey journey=journeyList.get(position);
                   Log.e("Recycle","setting time "+journey.getTime());
                  viewHolder.setTime(journey.getTime());
                    viewHolder.setTripCost(journey.getTrip_cost());
          }

          @Override
          public int getItemCount() {
              if (dataList==null)
                  return 0;
              else
              return (int) dataList.size();
          }
      }
    }
