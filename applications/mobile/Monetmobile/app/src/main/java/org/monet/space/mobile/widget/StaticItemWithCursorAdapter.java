package org.monet.space.mobile.widget;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.monet.space.mobile.R;

public class StaticItemWithCursorAdapter extends SimpleCursorAdapter {

    private SparseArray<String> staticItem;
    private LayoutInflater layoutInflater;
    private int layoutId;
    private Integer staticTitleResId = null;
    private int staticTitleViewId;

    public StaticItemWithCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, SparseArray<String> staticItem, int staticTitleViewId, int staticTitleResId) {
        this(context, layout, c, from, to, 0, staticItem);
        this.staticTitleResId = staticTitleResId;
        this.staticTitleViewId = staticTitleViewId;
    }

    public StaticItemWithCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, SparseArray<String> staticItem) {
        this(context, layout, c, from, to, 0, staticItem);
    }

    public StaticItemWithCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, SparseArray<String> staticItem) {
        super(context, layout, c, from, to, flags);

        this.staticItem = staticItem;
        this.layoutId = layout;
        this.layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0)
            return null;
        else
            return super.getItem(position - 1);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0)
            return -1;
        else
            return super.getItemId(position - 1);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private void bindStaticView(View view) {
        for (int id : this.mTo) {
            ((TextView) view.findViewById(id)).setText(staticItem.get(id));
        }
    }

    private void bindViewStaticTitle(View view, boolean visible, boolean isAllItem) {
        if (this.staticTitleResId == null) return;
        TextView titleView = ((TextView) view.findViewById(this.staticTitleViewId));
        TextView filterView = ((TextView) view.findViewById(android.R.id.text1));
        TextView subFilterView = ((TextView) view.findViewById(android.R.id.text2));

        if (visible) {
            titleView.setText(this.mContext.getString(this.staticTitleResId));
            titleView.setVisibility(View.VISIBLE);
            filterView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.navigation_menu_filter_text_size));
            subFilterView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.GONE);
            filterView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.navigation_menu_title_text_size));
            if (isAllItem)
                subFilterView.setVisibility(View.GONE);
            else
                subFilterView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            if (convertView == null)
                convertView = this.layoutInflater.inflate(this.layoutId, parent, false);
            bindStaticView(convertView);
        } else
            convertView = super.getView(position - 1, convertView, parent);
        bindViewStaticTitle(convertView, true, position == 0);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            if (convertView == null)
                convertView = newDropDownView(this.mContext, null, parent);
            bindStaticView(convertView);
        } else
            convertView = super.getDropDownView(position - 1, convertView, parent);

        bindViewStaticTitle(convertView, false, position == 0);
        return convertView;
    }

}
