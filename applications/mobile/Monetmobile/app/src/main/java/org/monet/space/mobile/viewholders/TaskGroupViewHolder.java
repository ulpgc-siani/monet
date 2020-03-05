package org.monet.space.mobile.viewholders;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.monet.space.mobile.R;
import org.monet.space.mobile.stringclusterizer.Item;
import org.monet.space.mobile.stringclusterizer.NullItem;

public class TaskGroupViewHolder extends ViewHolder {

    private final TextView title;
    private final int indentationMargin;
    private OnOpenListener onOpenListener;
    private OnCloseListener onCloseListener;
    private boolean opened = false;

    public TaskGroupViewHolder(View view) {
        super(view);
        title = (TextView)view.findViewById(android.R.id.text1);
        indentationMargin = view.getResources().getInteger(R.integer.indentation_margin);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void toggle() {
        opened = !opened;
        if (opened)
            showChildren();
        else
            hideChildren();
    }

    private void showChildren() {
        if (onOpenListener != null) onOpenListener.open();
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_find_previous_holo_light, 0);
    }

    private void hideChildren() {
        if (onCloseListener != null) onCloseListener.close();
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_find_next_holo_light, 0);
    }

    public TaskGroupViewHolder group(Item group) {
        int level = calculateLevel(group);
        title.setText(removeLabelPreviousLevel(level,group.id()));
        styleIndentationLevel(level);
        title.setTextColor(colorIndentationLevel(level));
        title.setTypeface(title.getTypeface(), Typeface.BOLD_ITALIC);
        return this;
    }

    private int colorIndentationLevel(int level){
        if (level == 1)
            return Color.rgb(0, 26, 102);
        if (level == 2)
            return Color.rgb(0, 51, 204);
        if (level == 3)
            return Color.rgb(26, 83, 255);
        if (level == 4)
            return Color.rgb(102, 140, 255);
        if (level == 5)
            return Color.rgb(179, 198, 255);
        return Color.rgb(179, 198, 255);
    }

    private String removeLabelPreviousLevel(int level, String label){
        String[] titleLevel = label.split(": ");
        if (titleLevel.length > 0)
            return titleLevel[level-1];
        return label;
    }

    private void styleIndentationLevel(int level) {
        ((LinearLayout.LayoutParams)title.getLayoutParams()).leftMargin = indentationMargin * level;
    }

    public TaskGroupViewHolder setOnOpenListener(OnOpenListener onOpenListener) {
        this.onOpenListener = onOpenListener;
        return this;
    }

    public TaskGroupViewHolder setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
        return this;
    }

    public void isClosed() {
        opened = false;
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_find_next_holo_light, 0);
    }

    public void isOpen() {
        opened = true;
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_find_previous_holo_light, 0);
    }


    private int calculateLevel(Item group) {
        return group.parent() == NullItem.instance() ? 0 : (1 + calculateLevel(group.parent()));
    }

    public interface OnOpenListener {
        void open();
    }

    public interface OnCloseListener {
        void close();
    }
}
