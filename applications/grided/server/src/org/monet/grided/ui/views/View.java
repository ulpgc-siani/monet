package org.monet.grided.ui.views;

import org.monet.grided.ui.views.impl.Context;

public interface View {
    public void setContext(Context context);
    public void addContextParameter(String key, Object object);
    public String render();
}

