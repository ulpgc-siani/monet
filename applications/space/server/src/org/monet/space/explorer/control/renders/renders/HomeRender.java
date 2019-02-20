package org.monet.space.explorer.control.renders.renders;

import org.monet.space.explorer.control.renders.Render;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.model.BusinessUnit;
import org.siani.itrules.Frame;

import java.io.InputStream;

public class HomeRender extends Render {

	public static final String NAME = "HomeRender";

	@Override
	protected InputStream getTemplate() {
		return configuration.getTemplate("home");
	}

	@Override
	protected Frame createFrame() {
		Frame frame = createMainFrame("Home");
		frame.addFrame("Configuration", addConfigurationFrame(frame));
		return frame;
	}

	private Frame addConfigurationFrame(Frame mainFrame) {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Configuration kernelConfiguration = Configuration.getInstance();

		Frame frame = new Frame("Configuration");
		frame.addFrame("theme", configuration.getTheme());
		frame.addFrame("businessUnit", businessUnit.getName());
		frame.addFrame("domain", kernelConfiguration.getDomain());
		frame.addFrame("url", configuration.getUrl());
		frame.addFrame("port", configuration.getPort());
		frame.addFrame("apiUrl", configuration.getApiUrl());
		frame.addFrame("apiPort", configuration.getApiPort());
		frame.addFrame("pushApiUrl", configuration.getPushApiUrl());
		frame.addFrame("analyticsUrl", configuration.getAnalyticsUrl());
		frame.addFrame("imagesPath", configuration.getImagesPath());
		frame.addFrame("applicationFmsUrl", configuration.getFmsServletUrl());
		frame.addFrame("digitalSignatureUrl", configuration.getDigitalSignatureUrl());
		frame.addFrame("federationLogo", mainFrame.getFrames("federationLogo"));
		frame.addFrame("federationUrl", mainFrame.getFrames("federationUrl"));
		frame.addFrame("federationLabel", mainFrame.getFrames("federationLabel"));
		frame.addFrame("title", mainFrame.getFrames("title"));
		frame.addFrame("subTitle", mainFrame.getFrames("subTitle"));
		frame.addFrame("modelLogo", mainFrame.getFrames("modelLogo"));
		frame.addFrame("language", mainFrame.getFrames("language"));
		frame.addFrame("testCase", "false");

		return frame;
	}
}
