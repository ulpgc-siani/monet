package org.monet.space.frontservice.presentation.user.renders;

import org.monet.space.frontservice.core.model.Language;
import org.monet.space.frontservice.configuration.Configuration;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;

public abstract class FrontserviceRender extends Render {
	protected RendersFactory rendersFactory;
	protected Dictionary dictionary;

	private static class Logger implements CanvasLogger {
		@Override
		public void debug(String message, Object... args) {
			AgentLogger.getInstance().debugInModel(message, args);
		}
	}

	public FrontserviceRender() {
		super(new Logger(), Configuration.getInstance().getTemplatesDir(Language.getCurrent()));
		this.rendersFactory = RendersFactory.getInstance();
		this.dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
	}

	public void setTemplate(String template) {
		Integer pos = template.lastIndexOf("?");
		if (pos == -1) pos = template.length();
		this.template = template.substring(0, pos);
		this.template = this.template.replaceAll(".html", "");
		this.template = this.template.replaceAll(".js", "");
		if (pos < template.length()) this.setParameters(template.substring(pos + 1));
	}

}
