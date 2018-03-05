package com.ontraport.mobileapp.adapters.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.fragments.RecordFragment;
import com.ontraport.sdk.http.RequestParams;

public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public View view;
    public CardView card;
    private Context context;
    private TextView[] labels;
    private TextView[] values;
    private FragmentManager manager;
    private RequestParams params;

    public CollectionViewHolder(View view, RequestParams params, FragmentManager manager, int textview_count) {
        super(view);
        this.view = view;
        this.context = view.getContext();
        this.params = params;
        this.manager = manager;
        values = new TextView[textview_count];
        labels = new TextView[textview_count];
        card = view.findViewById(R.id.card);
        LinearLayout layout_view = view.findViewById(R.id.card_layout);
        for (int i = 0; i < textview_count; i++) {
            layout_view.addView(createInnerLayout(i));
        }

        layout_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String object_id = params.getVal("objectID");
        String id = (String) v.getTag();

        RecordFragment fragment = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("objectID", Integer.valueOf(object_id));
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("record_" + object_id + "_" + id)
                .commit();
    }

    public int getCount() {
        return labels.length;
    }

    public void setLabelText(int pos, String text) {
        String t = text + ":";
        labels[pos].setText(t);
    }

    public void setValueText(int pos, String text) {
        values[pos].setText(text);
    }

    private LinearLayout createInnerLayout(int pos) {
        LinearLayout inner = new LinearLayout(context);
        inner.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        );
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setBackground(context.getResources().getDrawable(R.drawable.border_bottom));

        TextView label = createFieldLabel();
        inner.addView(label);
        labels[pos] = label;

        TextView value = createFieldValue();
        inner.addView(value);
        values[pos] = value;

        return inner;
    }

    private TextView createFieldLabel() {
        TextView temp = new TextView(context);
        temp.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        .8f
                )
        );
        temp.setTextSize(TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimension(R.dimen.card_text));
        temp.setTypeface(null, Typeface.BOLD);
        return temp;
    }

    private TextView createFieldValue() {
        TextView temp = new TextView(context);
        temp.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.2f
                )
        );
        temp.setTextSize(TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimension(R.dimen.card_text));
        return temp;
    }
}
