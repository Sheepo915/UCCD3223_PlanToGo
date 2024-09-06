package com.utar.plantogo.ui.planner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.db.model.PlannedTripsDetails;
import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;

import java.util.List;

public class PlannerDetailsOverviewAdapter extends RecyclerView.Adapter<PlannerDetailsOverviewAdapter.PlannerDetailsOverviewViewHolder> {

    private List<PlannedTripsDetails> plannedTripsDetails;
    private final Context context;

    public PlannerDetailsOverviewAdapter(Context context, List<PlannedTripsDetails> plannedTripsWithDetails) {
        this.plannedTripsDetails = plannedTripsWithDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public PlannerDetailsOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlannerEditComponent plannerItemComponent = new PlannerEditComponent(context);
        return new PlannerDetailsOverviewAdapter.PlannerDetailsOverviewViewHolder(plannerItemComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerDetailsOverviewViewHolder holder, int position) {
        PlannedTripsDetails tripDetails = plannedTripsDetails.get(position);
        holder.plannerEditComponent.setContent(tripDetails);
    }

    @Override
    public int getItemCount() {
        return plannedTripsDetails.size();
    }

    public static class PlannerDetailsOverviewViewHolder extends RecyclerView.ViewHolder {
        private final PlannerEditComponent plannerEditComponent;

        public PlannerDetailsOverviewViewHolder(@NonNull PlannerEditComponent itemView) {
            super(itemView);
            this.plannerEditComponent = itemView;
        }
    }
}
