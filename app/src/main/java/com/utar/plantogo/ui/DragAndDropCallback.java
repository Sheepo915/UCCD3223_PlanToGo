package com.utar.plantogo.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragAndDropCallback  extends ItemTouchHelper.Callback {

    private final ItemMoveCallback itemMoveCallback;

    public interface ItemMoveCallback {
        void onItemMove(int fromPosition, int toPosition);

    }

    public DragAndDropCallback(ItemMoveCallback callback) {
        this.itemMoveCallback = callback;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true; // Enable long press to drag
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; // Disable swipe
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // Notify the adapter that an item has moved
        itemMoveCallback.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
