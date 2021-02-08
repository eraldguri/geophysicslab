package com.eraldguri.geophysicslab;

import android.content.Context;
import android.os.Build;
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
import java.util.Date;
import java.util.List;

public class EarthquakeListAdapter extends RecyclerView.Adapter<EarthquakeListAdapter.EarthquakeListViewHolder> {

    private final Context mContext;
    private final List<Features> features;

    public EarthquakeListAdapter(Context context, List<Features> features) {
        mContext = context;
        this.features = features;
    }

    @NonNull
    @Override
    public EarthquakeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.earthquake_data_list_view, null);
        return new EarthquakeListViewHolder(mView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull EarthquakeListViewHolder holder, int position) {
        final Features earthquakes = features.get(position);
        double magnitude = earthquakes.getProperties().getMagnitude();
        String title = earthquakes.getProperties().getTitle();
        String dateTime = earthquakes.getProperties().getTime();

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
        holder.title.setText(title);
        holder.time.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return features == null ? 0 : features.size();
    }

    public static class EarthquakeListViewHolder extends RecyclerView.ViewHolder {

        TextView magnitude, title, time;

        public EarthquakeListViewHolder(@NonNull View itemView) {
            super(itemView);

            magnitude   = itemView.findViewById(R.id.tv_magnitude);
            title       = itemView.findViewById(R.id.tv_earthquake_title);
            time        = itemView.findViewById(R.id.tv_earthquake_date_time);
        }
    }
}
