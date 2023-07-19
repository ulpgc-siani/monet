package org.monet.space.setupservice.control.actions;

import org.apache.http.entity.mime.content.InputStreamBody;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.AgentException;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.deployer.DeployLogger;
import org.monet.space.kernel.deployer.DeployerPipeline;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.utils.StreamHelper;
import org.monet.space.setupservice.control.constants.Parameter;
import org.monet.space.setupservice.core.constants.ErrorCode;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.UUID;

public class ActionUpdateModel extends Action {

	public ActionUpdateModel() {
	}

	@Override
	public String execute() {
		InputStream modelStream = (InputStream) this.parameters.get(Parameter.MODEL);
		String destination, businessModelZipLocation;
		Kernel kernel = Kernel.getInstance();
		org.monet.space.kernel.configuration.Configuration monetConfiguration = kernel.getConfiguration();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		File tempModelFile = null;

		try {
            DeployLogger deployLogger = new ServiceDeployLogger(this.response.getWriter());

            if (!ComponentDocuments.getInstance().ping()) {
                deployLogger.info("COULD NOT UPLOAD DISTRIBUTION. DOCSERVICE IS DOWN. CHECK SERVER WITH ADMINISTRATOR!");
                return null;
            }

			DeployerPipeline pipeline = new DeployerPipeline(deployLogger);

			destination = monetConfiguration.getTempDir() + Strings.BAR45 + UUID.randomUUID().toString();
			businessModelZipLocation = monetConfiguration.getBusinessModelZipLocation();

			kernel.stopApplications();

			tempModelFile = File.createTempFile("mml", ".zip");
			AgentFilesystem.writeFile(tempModelFile, modelStream);

			pipeline.setData(GlobalData.MODEL_ZIP_FILE, tempModelFile);
			pipeline.setData(GlobalData.MODEL_INSTALL_DIRECTORY, monetConfiguration.getBusinessModelDir());
			pipeline.setData(GlobalData.TEMP_DIRECTORY, monetConfiguration.getTempDir());
			pipeline.setData(GlobalData.WORKING_DIRECTORY, destination);
			pipeline.setData(GlobalData.COMPONENT_PERSISTENCE, ComponentPersistence.getInstance());
			pipeline.setData(GlobalData.COMPONENT_FEDERATION, ComponentFederation.getInstance());
			pipeline.setData(GlobalData.COMPONENT_DOCUMENTS, ComponentDocuments.getInstance());
			pipeline.setData(GlobalData.BUSINESS_UNIT, businessUnit);
			pipeline.setData(GlobalData.BUSINESS_MODEL_ZIP_LOCATION, businessModelZipLocation);

			pipeline.process();

            Distribution distribution = businessUnit.getDistribution();
			Project project = businessUnit.getBusinessModel().getProject();
			businessUnit.setLabel(BusinessUnit.getSubTitle(distribution, project));
			businessUnit.save();

			if (BusinessUnit.autoRun() && !BusinessUnit.isRunning())
				businessUnit.run();

		} catch (DataException exception) {
			AgentException.getInstance().error(exception);
			throw new DataException(ErrorCode.UPDATE_BUSINESS_MODEL, null, exception);
		} catch (Exception exception) {
			AgentException.getInstance().error(exception);
			throw new SystemException(ErrorCode.UPDATE_BUSINESS_MODEL, null, exception);
		} finally {
			kernel.runApplications();
			StreamHelper.close(modelStream);
			if (tempModelFile != null) tempModelFile.delete();
		}

		return null;
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
			writer.println("[INFO] " + String.format(message, args));
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

}
