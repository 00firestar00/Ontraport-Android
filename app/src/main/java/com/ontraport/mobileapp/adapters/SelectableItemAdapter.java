package com.ontraport.mobileapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import com.ontraport.mobileapp.adapters.views.CollectionViewHolder;

import java.util.Map;

public abstract class SelectableItemAdapter extends RecyclerView.Adapter<CollectionViewHolder> {

    private SparseBooleanArray selected_items;

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
