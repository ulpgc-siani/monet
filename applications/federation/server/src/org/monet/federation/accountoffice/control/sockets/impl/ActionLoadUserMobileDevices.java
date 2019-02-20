package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.DeviceList;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public class ActionLoadUserMobileDevices implements ActionAccountSocket {
	private Logger logger;
	private DataRepository dataRepository;

	@Inject
	public ActionLoadUserMobileDevices(Logger logger, DataRepository dataRepository) {
		this.logger = logger;
		this.dataRepository = dataRepository;
	}

	@Override
	public SocketResponseMessage execute(SocketMessageModel socketMessage) {
		String userId = socketMessage.getId();

		DeviceList deviceList = this.dataRepository.loadMobileDevices(userId);
		SocketResponseMessage rep = new SocketResponseMessage();

		try {
			rep.setResponse(deviceList.serialize());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return rep;
	}

}
