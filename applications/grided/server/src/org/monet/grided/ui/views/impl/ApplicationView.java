package org.monet.grided.ui.views.impl;

import org.monet.grided.ui.templates.TemplatesEngine;

import com.google.inject.Inject;

public class ApplicationView extends BaseView {
    
    @Inject
    public ApplicationView(TemplatesEngine templatesEngine) {
        super(templatesEngine);        
        this.context = new Context();        
    }
            
    public String render() {
        super.setContext(this.context);
        return this.templatesEngine.merge("application.tpl", context);
    }
}
