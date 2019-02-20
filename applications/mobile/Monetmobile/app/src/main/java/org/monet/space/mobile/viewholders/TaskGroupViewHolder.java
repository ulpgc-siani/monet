package org.monet.space.mobile.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.monet.space.mobile.R;
import org.siani.cluster.Item;
import org.siani.cluster.NullItem;

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
        title.setText(group.id());
        styleIndentationLevel(calculateLevel(group));
        return this;
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
