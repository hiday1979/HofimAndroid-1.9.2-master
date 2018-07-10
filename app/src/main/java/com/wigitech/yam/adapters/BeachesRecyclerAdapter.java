package com.wigitech.yam.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wigitech.yam.R;
import com.wigitech.yam.activities.CoastDetailsActivity2;
import com.wigitech.yam.activities.MapsActivity;
import com.wigitech.yam.model.Beach;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hidayeichler on 12/11/2017.
 */

public class BeachesRecyclerAdapter extends RecyclerView.Adapter<BeachesRecyclerAdapter.BeachViewHolder> {

    private final Context context;
    private List<Beach> beaches = new ArrayList<>();
    private LayoutInflater inflater;



    //Constructor that takes the inflater.
    public BeachesRecyclerAdapter(Context context, List<Beach> beaches) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.beaches = beaches;

    }

    //Create an instance of MovieViewHolder and return it.
    //take an xml convert to a view->use the inflater.
    @Override
    public BeachViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.result_item, parent, false);

        BeachViewHolder holder = new BeachViewHolder(v);

        return holder;
    }

    //Data Binding!
    @Override
    public void onBindViewHolder(BeachViewHolder h, int position) {
        Beach m = beaches.get(position);
        h.tvTitle.setText(m.getBeachName());

    }

    @Override
    public int getItemCount() {
        return beaches.size();
    }


    /**
     * A ViewHolder is a class that holds the Views of the itemView.
     * It's main job is to hold all the views as fields (class members)
     * And to store a reference to the Views.
     * findViewById helps us get a reference as usual.
     */

    public class BeachViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;


        //Constructor that matches super:
        public BeachViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tvItem);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final String text = tvTitle.getText().toString();



        }

    }


}