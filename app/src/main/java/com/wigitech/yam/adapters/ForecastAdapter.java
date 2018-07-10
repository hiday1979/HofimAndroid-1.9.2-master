package com.wigitech.yam.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wigitech.yam.R;
import com.wigitech.yam.model.DetailedCoastRecyclerView;

import java.util.List;

/**
 * Created by GolaNir on 11/06/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.myViewHolder> {

    Context context;
    private List<DetailedCoastRecyclerView> dbhf0;

    public ForecastAdapter(Context context, List<DetailedCoastRecyclerView> dbhf0) {
        this.context = context;
        this.dbhf0 = dbhf0;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detailed_coast_card, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        DetailedCoastRecyclerView dcrv = dbhf0.get(position);
        holder.tvName.setText(dcrv.getName());
        holder.tvValue.setText(dcrv.getValue());
        //holder.ivImage.setImageResource(dcrv.getImageName());
        Glide.with(context).load(dcrv.getImageName()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return dbhf0.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName;
        TextView tvValue;

        public myViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.dcImage);
            tvName = (TextView) itemView.findViewById(R.id.dcName);
            tvValue = (TextView) itemView.findViewById(R.id.dcValue);
        }
    }
}
