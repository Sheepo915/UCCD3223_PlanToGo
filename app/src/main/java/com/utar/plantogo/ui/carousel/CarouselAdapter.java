package com.utar.plantogo.ui.carousel;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.attraction.AttractionCarouselComponent;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    private final Context context;
    private final List<Location> data;

    public CarouselAdapter(Context context, List<Location> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttractionCarouselComponent attractionCarouselComponent = new AttractionCarouselComponent(context);
        return new ViewHolder(attractionCarouselComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = data.get(position);
        String attractionName = location.getName();
        Double rating = location.getDetails().getRating();

        if (location.getPhotos() != null && !location.getPhotos().isEmpty()) {
            String imageUrl = location.getPhotos().get(0).getImages().getMedium().getUrl();

            holder.attractionCarouselComponent.setImage(imageUrl);
        } else {
            holder.attractionCarouselComponent.setImage(null); // Placeholder will be shown
        }

        holder.attractionCarouselComponent.setAttractionName(attractionName);
        holder.attractionCarouselComponent.setRating(rating);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AttractionCarouselComponent attractionCarouselComponent;

        public ViewHolder(@NonNull AttractionCarouselComponent itemView) {
            super(itemView);
            this.attractionCarouselComponent = itemView;
        }
    }
}
