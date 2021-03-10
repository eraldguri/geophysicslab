package com.eraldguri.geophysicslab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.database.SignificantEarthquakes;

import java.util.List;

import static com.eraldguri.geophysicslab.adapter.SignificantEarthquakesAdapter.*;

public class SignificantEarthquakesAdapter extends
        RecyclerView.Adapter<SignificantEarthquakesViewHolder> {

    private final Context mContext;
    private final List<SignificantEarthquakes> mSignificantEarthquakes;

    public SignificantEarthquakesAdapter(Context context, List<SignificantEarthquakes> significantEarthquakes) {
        mContext = context;
        mSignificantEarthquakes = significantEarthquakes;
    }

    @NonNull
    @Override
    public SignificantEarthquakesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.significant_quakes_list_layout, null);
        return new SignificantEarthquakesAdapter.SignificantEarthquakesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SignificantEarthquakesViewHolder holder, int position) {
        SignificantEarthquakes significantEarthquakes = mSignificantEarthquakes.get(position);

        holder.magnitude.setText(String.valueOf(significantEarthquakes.getMagnitude()));
        holder.place.setText(significantEarthquakes.getLocation());
        holder.intensity.setText(String.valueOf(significantEarthquakes.getIntensity()));
    }

    @Override
    public int getItemCount() {
        return mSignificantEarthquakes.size();
    }

    public static class SignificantEarthquakesViewHolder extends RecyclerView.ViewHolder {

        TextView magnitude, place, dateTime, intensity;

        public SignificantEarthquakesViewHolder(@NonNull View itemView) {
            super(itemView);

            magnitude = itemView.findViewById(R.id.significant_quakes_magnitude);
            place     = itemView.findViewById(R.id.significant_quakes_place);
            dateTime  = itemView.findViewById(R.id.significant_quakes_date_time);
            intensity = itemView.findViewById(R.id.significant_quakes_intensity);
        }
    }
}
