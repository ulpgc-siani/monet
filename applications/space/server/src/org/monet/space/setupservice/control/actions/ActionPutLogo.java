package org.monet.space.setupservice.control.actions;

import org.monet.space.setupservice.core.constants.ErrorCode;
import org.monet.space.setupservice.core.constants.MessageCode;
import org.monet.space.kernel.agents.AgentException;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.utils.StreamHelper;
import org.monet.space.setupservice.control.constants.Parameter;

import java.io.InputStream;

public class ActionPutLogo extends Action {

	public ActionPutLogo() {
	}

	@Override
	public String execute() {
		InputStream originalLogo = (InputStream) this.parameters.get(Parameter.LOGO);
		Configuration configuration = Configuration.getInstance();

		try {
			StreamHelper.copyData(originalLogo, AgentFilesystem.getOutputStream(configuration.getModelLogoFile()));
		} catch (Exception exception) {
			AgentException.getInstance().error(exception);
			throw new DataException(ErrorCode.SET_BUSINESS_SPACE_MANIFEST, null, exception);
		}

		return MessageCode.LOGO_PUT;
	}

}
