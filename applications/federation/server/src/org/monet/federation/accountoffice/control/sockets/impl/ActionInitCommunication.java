package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.components.unitcomponent.BusinessUnitComponent;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public class ActionInitCommunication implements ActionAccountSocket {

	private Logger logger;
	private BusinessUnitComponent businessUnitComponent;

	@Inject
	public ActionInitCommunication(Logger logger, BusinessUnitComponent businessUnitComponent) {
		this.businessUnitComponent = businessUnitComponent;
		this.logger = logger;
	}

	@Override
	public SocketResponseMessage execute(SocketMessageModel socketMessage) {
		SocketResponseMessage message = new SocketResponseMessage();
		message.setId(socketMessage.getId());

		try {
			businessUnitComponent.enableBusinessUnit(socketMessage.getIdUnit());

			fillUnitInfo(businessUnitComponent, socketMessage);

			message.setResponse(SocketResponseMessage.RESPONSE_OK);
		} catch (Exception e) {
			message.setIsError(true);
			message.setResponse(SocketResponseMessage.RESPONSE_ERROR_UNKNOW);
			this.logger.error(e.getMessage(), e);
		}
		return message;
	}

	private void fillUnitInfo(BusinessUnitComponent businessUnitComponent, SocketMessageModel socketMessage) {
		SocketMessageModel.UnitInfo infoData = socketMessage.getPresentionLabel();

		if (infoData.getLogoUrl() != null)
			businessUnitComponent.setBusinessUnitLogo(socketMessage.getIdUnit(), infoData.getLogoUrl());
	}

}
