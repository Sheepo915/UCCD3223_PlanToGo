package com.utar.plantogo.ui.attraction;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.util.List;


public class AttractionListAdapter extends RecyclerView.Adapter<AttractionListAdapter.ViewHolder> {

    private final Context context;
    private final List<Location> data;
    private final FragmentManager fragmentManager;
    private final FragmentViewModel fragmentViewModel;

    public AttractionListAdapter(Context context, List<Location> data, FragmentManager fragmentManager, FragmentViewModel fragmentViewModel) {
        this.context = context;
        this.data = data;
        this.fragmentManager = fragmentManager;
        this.fragmentViewModel = fragmentViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom view for each item
        AttractionListComponent view = new AttractionListComponent(context, fragmentManager, fragmentViewModel);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = data.get(position);

        holder.attractionListComponent.setAttraction(location);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AttractionListComponent attractionListComponent;

        public ViewHolder(@NonNull AttractionListComponent itemView) {
            super(itemView);
            attractionListComponent = itemView;
        }
    }
}