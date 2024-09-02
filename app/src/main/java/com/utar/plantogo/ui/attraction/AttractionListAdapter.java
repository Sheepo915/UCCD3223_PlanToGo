package com.utar.plantogo.ui.attraction;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.tripadvisor.model.Location;

import java.util.List;


public class AttractionListAdapter extends RecyclerView.Adapter<AttractionListAdapter.ViewHolder> {

    private final Context context;
    private final List<Location> data;

    public AttractionListAdapter(Context context, List<Location> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom view for each item
        AttractionListComponent view = new AttractionListComponent(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = data.get(position);

        // Set the image, name, and rating for each item
        if (location.getPhotos() != null && !location.getPhotos().isEmpty()) {
            String imageUrl = location.getPhotos().get(0).getImages().getMedium().getUrl();

            holder.component.setImage(imageUrl);
        } else {
            holder.component.setImage(null); // Placeholder will be shown
        }

        holder.component.setAttractionName(location.getName());
        holder.component.setRating(location.getDetails().getRating());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AttractionListComponent component;

        public ViewHolder(@NonNull AttractionListComponent itemView) {
            super(itemView);
            component = itemView;
        }
    }
}