package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public class ActionUnRegisterMobileDevice implements ActionAccountSocket {
	private Logger logger;
	private DataRepository dataRepository;

	@Inject
	public ActionUnRegisterMobileDevice(Logger logger, DataRepository dataRepository) {
		this.logger = logger;
		this.dataRepository = dataRepository;
	}

	@Override
	public SocketResponseMessage execute(SocketMessageModel socketMessage) {
		String userId = socketMessage.getId();
		String deviceId = socketMessage.getDeviceId();

		this.dataRepository.unRegisterMobileDevice(userId, deviceId);

		SocketResponseMessage response = new SocketResponseMessage();
		response.setResponse(SocketResponseMessage.RESPONSE_OK);

		return response;
	}

}
