package com.ontraport.mobileapp.utils;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import com.ontraport.mobileapp.main.collection.views.CollectionViewHolder;

import java.util.Map;

public abstract class SelectableItemAdapter extends RecyclerView.Adapter<CollectionViewHolder> {

    protected SparseBooleanArray selected_items = new SparseBooleanArray();

    public void toggleSelection(int position) {
        selectView(position, !selected_items.get(position));
    }

    public void removeSelection() {
        selected_items = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selected_items.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selected_items;
    }

    public abstract Map<String, String> getDataAtPosition(int position);

    private void selectView(int position, boolean value) {
        if (value) {
            selected_items.put(position, true);
        }
        else {
            selected_items.delete(position);
        }

        notifyDataSetChanged();
    }

}
