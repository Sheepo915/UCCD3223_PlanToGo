package com.utar.plantogo.ui.carousel;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselItemDecoration extends RecyclerView.ItemDecoration {

    private final int startMargin;
    private final int endMargin;
    private final int itemSpacing;

    public CarouselItemDecoration(int startMargin, int endMargin, int itemSpacing) {
        this.startMargin = startMargin;
        this.endMargin = endMargin;
        this.itemSpacing = itemSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        // Apply margins and spacing
        if (position == 0) {
            // First item
            outRect.left = startMargin;
        } else {
            outRect.left = itemSpacing;
        }
        outRect.right = (position == state.getItemCount() - 1) ? endMargin : itemSpacing;
        outRect.top = 0;
        outRect.bottom = 0;
    }
}
