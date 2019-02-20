package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.FederationUnitList;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public class ActionLoadMembers implements ActionAccountSocket {
	private Logger logger;
	private DataRepository dataRepository;

	@Inject
	public ActionLoadMembers(Logger logger, DataRepository dataRepository) {
		this.logger = logger;
		this.dataRepository = dataRepository;
	}

	@Override
	public SocketResponseMessage execute(SocketMessageModel socketMessage) {
		FederationUnitList federationUnitList = this.dataRepository.loadMemberBusinessUnitsWithServicesOrFeeders();
		SocketResponseMessage rep = new SocketResponseMessage();

		try {
			rep.setResponse(federationUnitList.serialize());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return rep;
	}

}
