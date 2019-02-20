package org.monet.grided.ui.templates;

import org.monet.grided.ui.views.impl.Context;

public interface TemplatesEngine {

    public String merge(String filename, Context context);

}

