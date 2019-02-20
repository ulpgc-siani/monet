package org.monet.grided.ui.views.impl;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.configuration.ServerConfiguration;
import org.monet.grided.ui.templates.TemplatesEngine;
import org.monet.grided.ui.views.View;
import org.monet.grided.ui.views.ViewParams;

import com.google.inject.Inject;

public abstract class BaseView implements View {

    protected TemplatesEngine templatesEngine;
    protected ServerConfiguration serverConfiguration;
    protected Configuration configuration;
    protected Context context;
    
    @Inject
    public BaseView(TemplatesEngine templatesEngine) {
        this.templatesEngine = templatesEngine;                
    }
    
    @Inject
    public void injectServerConfiguration(ServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }
    
    @Inject
    public void injectConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
        
    public void setContext(Context context) {        
        this.context = context;
        initializeContext(context);
    }
    
    public void addContextParameter(String key, Object object) {
        this.context.put(key, object);    
    }    
          
    private void initializeContext(Context context) {
        this.context.put(ViewParams.STYLES_URL, "styles");
        this.context.put(ViewParams.JAVASCRIPT_URL, "javascript");
        this.context.put(ViewParams.LANGUAGE, this.configuration.getProperty(Configuration.LANGUAGE));        
    }

    public abstract String render();
}
