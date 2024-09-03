package com.utar.plantogo.ui.carousel;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.tripadvisor.model.Image;
import com.utar.plantogo.internal.tripadvisor.model.Photo;
import com.utar.plantogo.ui.attraction.AttractionCarouselComponent;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.util.List;

public class CarouselPhotoAdapter extends RecyclerView.Adapter<CarouselPhotoAdapter.ViewHolder> {
    private final Context context;
    private final List<Photo> data;
    private final FragmentManager fragmentManager;
    private final FragmentViewModel fragmentViewModel;

    public CarouselPhotoAdapter(Context context, List<Photo> data, FragmentManager fragmentManager, FragmentViewModel fragmentViewModel) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.data = data;
        this.fragmentViewModel = fragmentViewModel;
    }

    @NonNull
    @Override
    public CarouselPhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttractionCarouselComponent attractionCarouselComponent = new AttractionCarouselComponent(context, fragmentManager, fragmentViewModel);
        return new CarouselPhotoAdapter.ViewHolder(attractionCarouselComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselPhotoAdapter.ViewHolder holder, int position) {
        Photo photo = data.get(position);
        Image image = photo.getImages().getFallbackImage();

        if (!data.isEmpty()) {
            String imageUrl = image.getUrl();

            holder.attractionCarouselComponent.setImage(imageUrl);
        } else {
            holder.attractionCarouselComponent.setImage(null); // Placeholder will be shown
        }
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
