package com.eraldguri.geophysicslab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.util.DateTimeUtil;

import java.text.ParseException;
import java.util.List;

public class EarthquakeListAdapter extends RecyclerView.Adapter<EarthquakeListAdapter.EarthquakeListViewHolder> {

    private final Context mContext;
    private final List<Features> features;
    private final OnItemClickListener onItemClickListener;

    public EarthquakeListAdapter(Context context, List<Features> features, OnItemClickListener onItemClickListener) {
        mContext = context;
        this.features = features;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EarthquakeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.earthquake_data_list_view, null);
        return new EarthquakeListViewHolder(mView, onItemClickListener, features);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull EarthquakeListViewHolder holder, int position) {
        final Features earthquakes = features.get(position);
        //double magnitudeToPrint = earthquakes.getProperties().getMagnitude();
        String place = earthquakes.getProperties().getPlace();
        String dateTime = earthquakes.getProperties().getTime();
        int tsunami = earthquakes.getProperties().getTsunami();

        String title = earthquakes.getProperties().getTitle();
        String mag = title.substring(2, 6);

        double magnitude = 0;
        try {
            magnitude = Double.parseDouble(mag);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String formattedTime = DateTimeUtil.parseDateTimeFromString(dateTime);

        if (magnitude >= 0.0 && magnitude < 2) {
            holder.magnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
        } else if (magnitude >= 2.0 && magnitude < 3) {
            holder.magnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray2));
        } else if (magnitude >= 3.0 && magnitude < 4) {
            holder.magnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow));
        } else if (magnitude >= 4.0 && magnitude <= 5.9) {
            holder.magnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        } else if (magnitude >= 6) {
            holder.magnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        }

        holder.magnitude.setText(String.valueOf(magnitude));
        holder.place.setText(place);
        holder.time.setText(formattedTime);

        if (tsunami != 0) {
            holder.tsunami.setVisibility(View.VISIBLE);
            holder.tsunami.setText("Tsunami: " + tsunami);
        } else {
            holder.tsunami.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return features == null ? 0 : features.size();
    }

    public static class EarthquakeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView magnitude, place, time, tsunami;
        OnItemClickListener onItemClickListener;
        List<Features> features;

        public EarthquakeListViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener, List<Features> features) {
            super(itemView);

            this.onItemClickListener = onItemClickListener;
            this.features = features;

            magnitude   = itemView.findViewById(R.id.tv_magnitude);
            place       = itemView.findViewById(R.id.tv_earthquake_place);
            time        = itemView.findViewById(R.id.tv_earthquake_date_time);
            tsunami     = itemView.findViewById(R.id.tv_earthquake_tsunami);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition(), features);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<Features> features);
    }
}
