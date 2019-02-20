package org.monet.space.backservice.control.actions;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.configuration.Configuration;

import java.io.IOException;

public class ActionDownloadDistribution extends Action {

	public ActionDownloadDistribution() {
	}

	@Override
	public String execute() {
		String businessModelZipLocation = Configuration.getInstance().getBusinessModelZipLocation();
		byte[] output = AgentFilesystem.getBytesFromFile(businessModelZipLocation);
		String contentType = "application/zip";

		try {
			this.response.setContentLength(output.length);
			this.response.setContentType(contentType);
			this.response.setHeader("Content-Disposition", "attachment; filename=distribution.zip");
			this.response.getOutputStream().write(output);
			this.response.getOutputStream().flush();
		} catch (IOException exception) {
			this.agentException.error(exception);
		}

		return null;
	}

}
