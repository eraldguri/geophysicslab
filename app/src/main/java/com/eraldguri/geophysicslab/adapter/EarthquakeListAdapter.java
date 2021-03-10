package com.eraldguri.geophysicslab.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeListAdapter extends
        RecyclerView.Adapter<EarthquakeListAdapter.EarthquakeListViewHolder> implements Filterable {

    private final Context mContext;
    private final List<Features> features;
    private final OnItemClickListener onItemClickListener;
    private final List<Features> filteredFeatures;

    public EarthquakeListAdapter(Context context, List<Features> features, OnItemClickListener onItemClickListener) {
        mContext = context;
        this.features = features;
        this.onItemClickListener = onItemClickListener;

        filteredFeatures = new ArrayList<>(features);
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

    private final Filter magnitudeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Features> magList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                magList.addAll(filteredFeatures);
            } else {
                String input = constraint.toString();
                for (Features feature: filteredFeatures) {
                    String m_feature = String.valueOf(feature.getProperties().getMagnitude());
                    if (m_feature.contains(input)) {
                        magList.add(feature);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = magList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            features.clear();
            features.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return magnitudeFilter;
    }

    public static class EarthquakeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView magnitude, place, time, tsunami;
        OnItemClickListener onItemClickListener;
        List<Features> features;

        public EarthquakeListViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener, List<Features> features) {
            super(itemView);

            this.onItemClickListener = onItemClickListener;
            this.features = features;

            magnitude   = itemView.findViewById(R.id.significant_quakes_magnitude);
            place       = itemView.findViewById(R.id.significant_quakes_place);
            time        = itemView.findViewById(R.id.significant_quakes_date_time);
            tsunami     = itemView.findViewById(R.id.significant_quakes_intensity);

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
