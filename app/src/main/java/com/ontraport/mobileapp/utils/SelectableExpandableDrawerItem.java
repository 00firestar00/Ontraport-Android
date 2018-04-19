package com.ontraport.mobileapp.utils;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.BaseDescribeableDrawerItem;
import com.mikepenz.materialdrawer.model.BaseViewHolder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ontraport.mobileapp.R;

import java.util.List;

public class SelectableExpandableDrawerItem
        extends BaseDescribeableDrawerItem<SelectableExpandableDrawerItem, SelectableExpandableDrawerItem.ViewHolder>
        implements IClickable {

    private FastAdapter<IDrawerItem> adapter;
    private Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener;
    private Drawer.OnDrawerItemClickListener mOnArrowDrawerItemClickListener;
    private ColorHolder arrowColor;
    private int arrowRotationAngleStart = 0;
    private int arrowRotationAngleEnd = 180;

    public SelectableExpandableDrawerItem(FastAdapter<IDrawerItem> adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_expandable;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_selectable_expandable;
    }

    @Override
    public SelectableExpandableDrawerItem withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    @Override
    public Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return mOnArrowDrawerItemClickListener;
    }

    @Override
    public void bindView(SelectableExpandableDrawerItem.ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        //bind the basic view parts
        bindViewHelper(viewHolder);

        //make sure all animations are stopped
        if (viewHolder.arrow.getDrawable() instanceof IconicsDrawable) {
            ((IconicsDrawable) viewHolder.arrow.getDrawable()).color(Color.BLACK);
        }
        viewHolder.arrow.clearAnimation();
        if (!isExpanded()) {
            viewHolder.arrow.setRotation(this.arrowRotationAngleStart);
        }
        else {
            viewHolder.arrow.setRotation(this.arrowRotationAngleEnd);
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v, this);
    }

    @Override
    public IItem withOnItemPreClickListener(@NonNull OnClickListener onItemPreClickListener) {
        return null;
    }

    @Override
    public OnClickListener getOnPreItemClickListener() {
        return new OnClickListener() {
            @Override
            public boolean onClick(@Nullable View v, @NonNull IAdapter adapter, @NonNull IItem item, int position) {
                return false;
            }
        };
    }

    @Override
    public IItem withOnItemClickListener(@NonNull OnClickListener onItemClickListener) {
        return null;
    }

    @Override
    public OnClickListener getOnItemClickListener() {
        return new OnClickListener() {
            @Override
            public boolean onClick(@Nullable View v, @NonNull IAdapter adapter, @NonNull IItem item, int position) {
                return mOnDrawerItemClickListener != null
                        && mOnDrawerItemClickListener.onItemClick(v, position, SelectableExpandableDrawerItem.this);
            }
        };
    }

    public void collapse() {
        ExpandableExtension<IDrawerItem> extension = adapter.getExtension(ExpandableExtension.class);
        if (extension != null) {
            extension.collapse();
        }
    }

    private void toggleExpandable(int pos) {
        ExpandableExtension<IDrawerItem> extension = adapter.getExtension(ExpandableExtension.class);
        if (extension != null) {
            extension.toggleExpandable(pos);
        }
    }

    public static class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        LinearLayout arrow_layout;
        ImageView arrow;
        SelectableExpandableDrawerItem select_expand;

        ViewHolder(View view, SelectableExpandableDrawerItem item) {
            super(view);
            select_expand = item;
            arrow = view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_arrow);
            arrow.setImageDrawable(new IconicsDrawable(view.getContext(), MaterialDrawerFont.Icon.mdf_expand_more).sizeDp(16).paddingDp(2).color(Color.BLACK));
            arrow_layout = view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_arrow_container);
            arrow_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SelectableExpandableDrawerItem item = (SelectableExpandableDrawerItem) view.getTag();
            if (!item.isExpanded()) {
                ViewCompat.animate(arrow).rotation(180).start();
            }
            else {
                ViewCompat.animate(arrow).rotation(0).start();
            }
            item.toggleExpandable(getAdapterPosition());
        }
    }

}
