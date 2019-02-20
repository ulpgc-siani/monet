package org.monet.space.analytics.renders;

import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.model.DatawareHouseLink;
import org.monet.space.analytics.model.DatawareHouseLinkProvider;
import org.monet.space.analytics.model.Language;
import org.monet.space.analytics.utils.KeyUtil;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Federation;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;

import java.util.HashMap;

public abstract class DatawareHouseRender extends Render {
	protected RendersFactory rendersFactory;
	private DatawareHouseLink link = null;

	protected static final Integer ELEMENTS_PER_PAGE = 10;

	public static final class Parameter {
		public static final String COMMAND = "command";
		public static final String SELECTED_INDICATORS = "selectedIndicators";
		public static final String REPORT = "report";
	}

	public static final class Templates {
		public static final String PREVIEW = "preview";
	}

	public static final class Mode {
		public static final String PAGE = "page";
		public static final String VIEW = "view";
	}

	protected static class Logger implements CanvasLogger {
		@Override
		public void debug(String message, Object... args) {
			AgentLogger.getInstance().debug(message, args);
		}
	}

	public DatawareHouseRender() {
		this(Configuration.getInstance().getTemplatesWebDir(Language.getCurrent()));
	}

	public DatawareHouseRender(String templatesDir) {
		super(new Logger(), templatesDir);
		this.rendersFactory = RendersFactory.getInstance();
	}

	public void setTemplate(String template) {
		Integer pos = template.lastIndexOf("?");
		if (pos == -1) pos = template.length();
		this.template = template.substring(0, pos);
		this.template = this.template.replaceAll(".html", "");
		this.template = this.template.replaceAll(".js", "");
		if (pos < template.length()) this.setParameters(template.substring(pos + 1));
	}

	protected void addCommonMarks(HashMap<String, Object> map) {
		Long thread = Thread.currentThread().getId();
		Context context = Context.getInstance();
		Configuration configuration = Configuration.getInstance();
		String url = configuration.getUrl();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Federation federation = businessUnit.getFederation();
		org.monet.space.kernel.configuration.Configuration kernelConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();

		map.put("enterpriseLoginUrl", String.valueOf(configuration.getEnterpriseLoginUrl()));
		map.put("domain", context.getDomain(thread));
		map.put("url", configuration.getServletUrl());
		map.put("imagesUrl", url + "/images");
		map.put("stylesUrl", url + "/styles");
		map.put("javascriptUrl", url + "/javascript");
		map.put("officeUrl", configuration.getOfficeUrl());
		map.put("port", String.valueOf(context.getPort(thread)));
		map.put("language", Language.getCurrent());
		map.put("keyTemplate", KeyUtil.KEY_TEMPLATE);
		map.put("keySeparator", KeyUtil.KEY_SEPARATOR);
		map.put("businessUnit", BusinessUnit.getInstance().getName());
		map.put("googleApiKey", kernelConfiguration.getGoogleApiKey());
		map.put("httpProtocol", kernelConfiguration.useSSL() ? "https" : "http");

        Distribution distribution = businessUnit.getDistribution();
        Project project = businessUnit.getBusinessModel().getProject();
        map.put("title", BusinessUnit.getTitle(distribution, project));
		map.put("subtitle", businessUnit.getLabel());
        map.put("pageTitle", businessUnit.getTitle() + " - " + businessUnit.getSubTitle());

		String federationLogoUrl;
		if (AgentFilesystem.existFile(kernelConfiguration.getFederationLogoFile()))
			federationLogoUrl = configuration.getFederationLogoUrl();
		else
			federationLogoUrl = federation.getLogoUrl();

		map.put("federationLabel", federation.getLabel());
		map.put("federationUrl", federation.getUri());
		map.put("federationLogoUrl", federationLogoUrl);
		map.put("federationLogoOriginalUrl", federation.getLogoUrl());

		map.put("currentYear", LibraryDate.getCurrentYear());
	}

	protected DatawareHouseLink getLink() {

		if (this.link == null)
			this.link = new DatawareHouseLinkProvider();

		return this.link;
	}

}
