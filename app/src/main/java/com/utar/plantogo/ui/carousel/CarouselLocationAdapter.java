package com.utar.plantogo.ui.carousel;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.attraction.AttractionCarouselComponent;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.util.List;

public class CarouselLocationAdapter extends RecyclerView.Adapter<CarouselLocationAdapter.ViewHolder> {

    private final Context context;
    private final List<Location> data;
    private final FragmentManager fragmentManager;
    private final FragmentViewModel fragmentViewModel;

    public CarouselLocationAdapter(Context context, List<Location> data, FragmentManager fragmentManager,FragmentViewModel fragmentViewModel) {
        this.context = context;
        this.data = data;
        this.fragmentManager = fragmentManager;
        this.fragmentViewModel = fragmentViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttractionCarouselComponent attractionCarouselComponent = new AttractionCarouselComponent(context, fragmentManager, fragmentViewModel);
        return new ViewHolder(attractionCarouselComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = data.get(position);

        holder.attractionCarouselComponent.setAttraction(location);
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
