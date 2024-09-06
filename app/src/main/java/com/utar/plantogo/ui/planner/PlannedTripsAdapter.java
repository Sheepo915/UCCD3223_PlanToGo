package com.utar.plantogo.ui.planner;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;

import java.util.List;

public class PlannedTripsAdapter extends RecyclerView.Adapter<PlannedTripsAdapter.PlannedTripViewHolder> {
    private final List<PlannedTripsWithDetails> plannedTrips;
    private final Context context;
    private final FragmentManager fragmentManager;

    public PlannedTripsAdapter(List<PlannedTripsWithDetails> plannedTrips, Context context, FragmentManager fragmentManager) {
        this.plannedTrips = plannedTrips;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public PlannedTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlannerItemComponent plannerItemComponent = new PlannerItemComponent(context, fragmentManager);
        return new PlannedTripViewHolder(plannerItemComponent);
    }


    public void onBindViewHolder(@NonNull PlannedTripViewHolder holder, int position) {
        PlannedTripsWithDetails tripDetails = plannedTrips.get(position);
        holder.plannerItemComponent.setContent(null, tripDetails.plannedTrip.tripName, tripDetails.plannedTrip.tripLocation, tripDetails.plannedTrip.notes, tripDetails.plannedTrip.startDate);

    }

    @Override
    public int getItemCount() {
        return plannedTrips.size();
    }

    public static class PlannedTripViewHolder extends RecyclerView.ViewHolder {
        private final PlannerItemComponent plannerItemComponent;

        public PlannedTripViewHolder(@NonNull PlannerItemComponent plannerItemComponent) {
            super(plannerItemComponent);
            this.plannerItemComponent = plannerItemComponent;
        }
    }
}
