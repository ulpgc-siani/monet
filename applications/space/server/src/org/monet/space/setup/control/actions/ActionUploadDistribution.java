/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.setup.control.actions;

import org.apache.commons.fileupload.FileItem;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.space.applications.library.LibraryResponse;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.deployer.DeployLogger;
import org.monet.space.kernel.deployer.DeployerPipeline;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;
import org.monet.space.setup.ApplicationSetup;
import org.monet.space.setup.configuration.Configuration;
import org.monet.space.setup.control.constants.Parameter;
import org.monet.space.setup.control.constants.SessionVariable;
import org.monet.space.setup.library.LibraryFileUploader;
import org.monet.space.setup.library.LibraryMultipartRequest;

import java.io.File;
import java.io.PrintWriter;
import java.util.UUID;

public class ActionUploadDistribution extends Action {

	public ActionUploadDistribution() {
		super();
	}

	private class ServiceDeployLogger implements DeployLogger {

		private PrintWriter writer;
		private AgentLogger logger = AgentLogger.getInstance();

		public ServiceDeployLogger(PrintWriter writer) {
			this.writer = writer;
		}

		@Override
		public void info(String message, Object... args) {
			logger.infoInModel(message, args);
			writer.println("[INFO] " + String.format(message, args) + "<br/>");
			writer.flush();
		}

		@Override
		public void error(Throwable exception) {
			logger.errorInModel(exception);
		}

		@Override
		public void error(String message, Object... args) {
			String errorMsg = (message != null && args != null) ? String.format(message, args) : message;
			logger.errorInModel(errorMsg, null);
			writer.println("[ERROR] " + errorMsg);
			writer.flush();
		}

		@Override
		public void debug(String message, Object... args) {
			logger.debugInModel(message, args);
			writer.println("[DEBUG] " + String.format(message, args));
			writer.flush();
		}
	}

	;

	public String execute() {

		if (!this.kernel.isLogged()) {
			this.launchAuthenticateAction();
			return null;
		}

		LibraryMultipartRequest multipartRequest = new LibraryMultipartRequest(request);
		LibraryFileUploader uploader = new LibraryFileUploader();
		String uploadDirectory = Configuration.getInstance().getUploadsDir();
		String destination;
		Kernel kernel = Kernel.getInstance();
		org.monet.space.kernel.configuration.Configuration monetConfiguration = kernel.getConfiguration();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		FileItem fileItem = multipartRequest.getFileParameter(Parameter.DISTRIBUTION);

		try {
            DeployLogger deployLogger = new ServiceDeployLogger(this.response.getWriter());

            if (!ComponentDocuments.getInstance().ping()) {
                deployLogger.info("COULD NOT UPLOAD DISTRIBUTION. DOCSERVICE IS DOWN. CHECK SERVER WITH ADMINISTRATOR!");
                return null;
            }

			File modelFile = uploader.uploadFile(fileItem, uploadDirectory, "businessmodel.zip");
			destination = monetConfiguration.getTempDir() + Strings.BAR45 + UUID.randomUUID().toString();
			kernel.stopApplications();

			DeployerPipeline pipeline = new DeployerPipeline(deployLogger);
			pipeline.setData(GlobalData.MODEL_ZIP_FILE, modelFile);
			pipeline.setData(GlobalData.MODEL_INSTALL_DIRECTORY, monetConfiguration.getBusinessModelDir());
			pipeline.setData(GlobalData.TEMP_DIRECTORY, monetConfiguration.getTempDir());
			pipeline.setData(GlobalData.WORKING_DIRECTORY, destination);
			pipeline.setData(GlobalData.COMPONENT_PERSISTENCE, ComponentPersistence.getInstance());
			pipeline.setData(GlobalData.COMPONENT_FEDERATION, ComponentFederation.getInstance());
			pipeline.setData(GlobalData.COMPONENT_DOCUMENTS, ComponentDocuments.getInstance());
			pipeline.setData(GlobalData.BUSINESS_UNIT, businessUnit);
			pipeline.setData(GlobalData.BUSINESS_MODEL_ZIP_LOCATION, monetConfiguration.getBusinessModelZipLocation());

			pipeline.process();

            Distribution distribution = businessUnit.getDistribution();
			Project project = businessUnit.getBusinessModel().getProject();
			businessUnit.setLabel(BusinessUnit.getSubTitle(distribution, project));
			businessUnit.save();

			if (pipeline.getProblems().size() > 0)
				Context.getInstance().getCurrentSession().setVariable(SessionVariable.ERROR, "ERR_UPLOAD_DISTRIBUTION");
			else {
				if (BusinessUnit.autoRun() && !BusinessUnit.isRunning())
					businessUnit.run();
			}
		} catch (Throwable ex) {
			this.agentException.error(ex);
			return "Select a distribution file from your computer";
		} finally {
			kernel.runApplications();
		}

		LibraryResponse.sendRedirect(this.response, ApplicationSetup.getConfiguration().getUrl() + "?tab=distribution");

		return null;
	}

}