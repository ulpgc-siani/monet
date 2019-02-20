package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.ServerConfiguration;

public class ActionSubscribe extends Action {

	public ActionSubscribe() {
	}

	@Override
	public String execute() {
		String type = (String) this.parameters.get(Parameter.TYPE);
		String data = (String) this.parameters.get(Parameter.DATA);
		ServerConfiguration serverConfiguration = new ServerConfiguration();

		serverConfiguration.deserializeFromXML(LibraryEncoding.decode(data), null);
		ComponentPersistence.getInstance().getNodeLayer().addNodeSubscriber(serverConfiguration, Integer.valueOf(type));

		return MessageCode.SUBSCRIBED;
	}

}
