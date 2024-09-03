package com.utar.plantogo.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private final int startMargin;
    private final int endMargin;
    private final int itemSpacing;
    private final Direction direction;

    public RecyclerViewItemDecoration(int startMargin, int endMargin, int itemSpacing, Direction direction) {
        this.startMargin = startMargin;
        this.endMargin = endMargin;
        this.itemSpacing = itemSpacing;
        this.direction = direction;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (direction == Direction.HORIZONTAL) {
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
        } else if (direction == Direction.VERTICAL) {
            if (endMargin == 0 && position == state.getItemCount() - 1) {
                outRect.bottom = 0;
            } else {
                outRect.bottom = itemSpacing;
            }
        }

    }

    public enum Direction {
        HORIZONTAL, VERTICAL
    }
}
