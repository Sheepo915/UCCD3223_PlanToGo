package com.utar.plantogo.ui.planner;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;

import java.util.ArrayList;
import java.util.List;

public class PlannedTripsAdapter extends RecyclerView.Adapter<PlannedTripsAdapter.PlannedTripViewHolder> implements Filterable {
    private final List<PlannedTripsWithDetails> plannedTrips;
    private List<PlannedTripsWithDetails> plannedTripsListFiltered;
    private final Context context;
    private final FragmentManager fragmentManager;

    public PlannedTripsAdapter(List<PlannedTripsWithDetails> plannedTrips, Context context, FragmentManager fragmentManager) {
        this.plannedTrips = plannedTrips;
        this.plannedTripsListFiltered = new ArrayList<>(plannedTrips);
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
        holder.plannerItemComponent.setContent(null, tripDetails.plannedTrips.tripName, tripDetails.plannedTrips.tripLocation, tripDetails.plannedTrips.notes, tripDetails.plannedTrips.startDate, tripDetails.plannedTrips.endDate);
        holder.plannerItemComponent.instantiateListener(tripDetails.plannedTrips.plannedTripId);
    }

    @Override
    public int getItemCount() {
        return plannedTripsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                List<PlannedTripsWithDetails> filteredList = new ArrayList<>();

                if (query.isEmpty()) {
                    filteredList.addAll(plannedTrips);
                } else {
                    for (PlannedTripsWithDetails trip : plannedTrips) {
                        if (trip.plannedTrips.tripName.toLowerCase().contains(query)) {
                            filteredList.add(trip);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                plannedTripsListFiltered.clear(); // Clear existing list
                if (results.values != null) {
                    plannedTripsListFiltered.addAll((List<PlannedTripsWithDetails>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }


    public static class PlannedTripViewHolder extends RecyclerView.ViewHolder {
        private final PlannerItemComponent plannerItemComponent;

        public PlannedTripViewHolder(@NonNull PlannerItemComponent plannerItemComponent) {
            super(plannerItemComponent);
            this.plannerItemComponent = plannerItemComponent;
        }
    }
}
