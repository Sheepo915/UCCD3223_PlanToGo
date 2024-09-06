package com.utar.plantogo.ui.bottomsheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.R;

import java.util.List;

public class PlannedTripBottomSheetAdapter extends RecyclerView.Adapter<PlannedTripBottomSheetAdapter.PlannedTripBottomSheetViewHolder> {

    private final Context context;
    private final List<String> plannedTrips;

    public PlannedTripBottomSheetAdapter(Context context, List<String> plannedTrips) {
        this.context = context;
        this.plannedTrips = plannedTrips;
    }

    @NonNull
    @Override
    public PlannedTripBottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new TextView programmatically
        View view = LayoutInflater.from(context).inflate(R.layout.component_trip_name, parent, false);
        return new PlannedTripBottomSheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannedTripBottomSheetViewHolder holder, int position) {
        String plannedTrip = plannedTrips.get(position);
        TextView tripName = holder.textView.findViewById(R.id.tv_trip_name);

        tripName.setText(plannedTrip);
    }

    @Override
    public int getItemCount() {
        return plannedTrips.size();
    }

    public static class PlannedTripBottomSheetViewHolder extends RecyclerView.ViewHolder {
        private final View textView;

        public PlannedTripBottomSheetViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView;
        }
    }
}
