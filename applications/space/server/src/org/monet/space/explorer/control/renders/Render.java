package org.monet.space.explorer.control.renders;

import com.google.inject.Inject;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.space.explorer.configuration.Configuration;
import org.monet.space.explorer.model.Label;
import org.monet.space.explorer.model.Language;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Federation;
import org.siani.itrules.Document;
import org.siani.itrules.Frame;
import org.siani.itrules.RuleEngine;
import org.siani.itrules.TemplateReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

public abstract class Render {
	protected Language language;
	protected Configuration configuration;

	@Inject
	public void inject(Language language) {
		this.language = language;
	}

	public void execute(Writer writer) {
		Frame frame = createFrame();
		write(frame, getTemplate(), writer);
	}

	protected abstract Frame createFrame();
	protected abstract InputStream getTemplate();

	@Inject
	public void inject(Configuration configuration) {
		this.configuration = configuration;
	}

	protected Frame createMainFrame(final String type) {
		org.monet.space.kernel.configuration.Configuration kernelConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Federation federation = businessUnit.getFederation();
		Distribution distribution = businessUnit.getDistribution();
		Project project = businessUnit.getBusinessModel().getProject();

		String federationLogoUrl, federationSplashLogoUrl;
		if (AgentFilesystem.existFile(kernelConfiguration.getFederationLogoFile())) {
			federationLogoUrl = configuration.getFederationLogoUrl();
			federationSplashLogoUrl = configuration.getFederationSplashLogoUrl();
		} else {
			federationLogoUrl = federation.getLogoUrl();
			federationSplashLogoUrl = federation.getLogoUrl();
		}

		String subTitle = businessUnit.getLabel();
		Frame frame = new Frame(type);
		frame.addFrame("Header", createHeaderFrame());
		frame.addFrame("pageTitle", businessUnit.getTitle() + ((subTitle != null && !subTitle.isEmpty()) ? " - " + subTitle : ""));
		frame.addFrame("title", BusinessUnit.getTitle(distribution, project));
		frame.addFrame("subTitle", subTitle);
		frame.addFrame("language", Language.getCurrent());
		frame.addFrame("theme", configuration.getTheme());
		frame.addFrame("apiUrl", configuration.getApiUrl());
		frame.addFrame("modelLogo", configuration.getBusinessModelLogoUrl());
		frame.addFrame("federationLabel", federation.getLabel());
		frame.addFrame("federationUrl", federation.getUri());
		frame.addFrame("federationLogo", federationLogoUrl);
		frame.addFrame("federationLogoOriginal", federation.getLogoUrl());
		frame.addFrame("federationLogoSplash", federationSplashLogoUrl);
		frame.addFrame("modelLogoSplash", configuration.getBusinessModelSplashLogoUrl());
		frame.addFrame("monetLogoSplash", configuration.getMonetSplashLogoUrl());
		frame.addFrame("enterpriseLoginUrl", configuration.getEnterpriseLoginUrl());
		frame.addFrame("currentYear", LibraryDate.getCurrentYear());
		frame.addFrame("allRightsReserved", language.getLabel(Label.ALL_RIGHTS_RESERVED));

		return frame;
	}

	protected void write(Frame frame, InputStream template, Writer writer) {
		Document document = new Document();
		TemplateReader reader = new TemplateReader(configuration.getTemplate("header", "shared"), template);

		RuleEngine engine = new RuleEngine(reader);
		engine.render(frame, document);

		try {
			writer.append(document.content());
		} catch (IOException e) {
			AgentLogger.getInstance().error(e);
		}
	}

	private Frame createHeaderFrame() {
		Frame frame = new Frame("Header");
		frame.addFrame("enterpriseLoginUrl", configuration.getEnterpriseLoginUrl());
		return frame;
	}

}
