package org.monet.space.mobile.viewholders;

import android.view.View;

public class ViewHolder {

    private final View view;

    public ViewHolder(View view) {
        this.view = view;
        view.setTag(this);
    }

    public View view() {
        return view;
    }
}
