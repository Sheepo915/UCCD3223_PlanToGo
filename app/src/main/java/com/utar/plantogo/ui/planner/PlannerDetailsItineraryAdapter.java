package com.utar.plantogo.ui.planner;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.db.model.PlannedTripsDetails;
import com.utar.plantogo.ui.DragAndDropCallback;

import java.util.Collections;
import java.util.List;

public class PlannerDetailsItineraryAdapter extends RecyclerView.Adapter<PlannerDetailsItineraryAdapter.PlannerDetailsItineraryViewHolder> implements DragAndDropCallback.ItemMoveCallback {

    private final Context context;
    private final List<PlannedTripsDetails> plannedTripsDetails;

    public PlannerDetailsItineraryAdapter(Context context, List<PlannedTripsDetails> plannedTripsDetails) {
        this.plannedTripsDetails = plannedTripsDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public PlannerDetailsItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlannerItineraryComponent plannerItineraryComponent = new PlannerItineraryComponent(context);
        return new PlannerDetailsItineraryAdapter.PlannerDetailsItineraryViewHolder(plannerItineraryComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerDetailsItineraryViewHolder holder, int position) {
        PlannedTripsDetails tripsDetails = plannedTripsDetails.get(position);
        holder.bind(tripsDetails);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(plannedTripsDetails, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return plannedTripsDetails.size();
    }

    public static class PlannerDetailsItineraryViewHolder extends RecyclerView.ViewHolder {
        PlannerItineraryComponent plannerItineraryComponent;

        public PlannerDetailsItineraryViewHolder(@NonNull PlannerItineraryComponent plannerItineraryComponent) {
            super(plannerItineraryComponent);
            this.plannerItineraryComponent = plannerItineraryComponent;
        }

        public void bind(PlannedTripsDetails plannedTrip) {
            plannerItineraryComponent.setContent(plannedTrip);
        }
    }

}
