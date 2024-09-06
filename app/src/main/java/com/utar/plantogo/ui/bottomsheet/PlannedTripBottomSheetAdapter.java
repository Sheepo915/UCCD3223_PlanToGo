package com.utar.plantogo.ui.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.utar.plantogo.R;
import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.Location;
import com.utar.plantogo.internal.db.model.PlannedTripsDetails;
import com.utar.plantogo.internal.db.pojo.TripIdName;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PlannedTripBottomSheetAdapter extends RecyclerView.Adapter<PlannedTripBottomSheetAdapter.PlannedTripBottomSheetViewHolder> {

    private final Context context;
    private final List<TripIdName> plannedTrips;
    private final FragmentViewModel viewModel;
    private final BottomSheetDialog bottomSheetFragment;

    public PlannedTripBottomSheetAdapter(Context context, List<TripIdName> plannedTrips, FragmentViewModel viewModel, BottomSheetDialog bottomSheetFragment) {
        this.context = context;
        this.plannedTrips = plannedTrips;
        this.viewModel = viewModel;
        this.bottomSheetFragment = bottomSheetFragment;
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
        TripIdName plannedTrip = plannedTrips.get(position);
        TextView tripName = holder.view.findViewById(R.id.tv_trip_name);

        tripName.setText(plannedTrip.getTripName());
        tripName.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(context);

            new Thread(() -> {
                Integer maxIndex = db.plannedTripsDao().getMaxIndex(plannedTrip.getId());
                int nextIndex = (maxIndex == null) ? 0 : maxIndex + 1;

                PlannedTripsDetails plannedTripsDetails = new PlannedTripsDetails(plannedTrip.getId(), Integer.parseInt(Objects.requireNonNull(viewModel.getSelectedLocation().getValue()).getLocationId()), nextIndex, "", "", new Date().toString());

                db.plannedTripsDao().insertPlannedTripDetails(plannedTripsDetails);
                db.locationDao().insertLocation(new Location(viewModel.getSelectedLocation().getValue()));

                ((Activity) context).runOnUiThread(() -> {
                    if (bottomSheetFragment != null) {
                        bottomSheetFragment.dismiss();
                    }
                });
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return plannedTrips.size();
    }

    public static class PlannedTripBottomSheetViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public PlannedTripBottomSheetViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
