package org.monet.grided.ui.templates.impl;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.monet.grided.constants.ErrorCode;
import org.monet.grided.exceptions.SystemException;
import org.monet.grided.ui.templates.TemplatesEngine;
import org.monet.grided.ui.views.impl.Context;


public class TemplatesEngineImpl implements TemplatesEngine {
    private VelocityEngine         velocityEngine;
    
    public TemplatesEngineImpl(String templatesPath) {
        try {
            this.velocityEngine = new VelocityEngine();
            this.init(templatesPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }

    private boolean init(String templatesPath) throws Exception {
        this.velocityEngine.addProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templatesPath);
        //this.velocityEngine.addProperty(Velocity.RUNTIME_LOG, logPath + "velocity.log");
        this.velocityEngine.addProperty(Velocity.INPUT_ENCODING, "UTF-8");
        this.velocityEngine.addProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        this.velocityEngine.init();

        return true;
    }
    
    
    public String merge(String sFilename, Context oContext) {
        VelocityContext velocityContext;
        StringWriter    writer = new StringWriter();
        Template        template;

        velocityContext = new VelocityContext(oContext.get());  
        try {
            template = this.velocityEngine.getTemplate(sFilename);
            template.merge(velocityContext, writer);
        }
        catch (Exception ex) {
            throw new SystemException(ErrorCode.TEMPLATE_MERGING, ex);
        }
        return writer.toString().trim();
    }
}