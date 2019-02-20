package org.monet.space.kernel.agents;

import com.google.android.gcm.server.*;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.DeviceList;
import org.monet.federation.accountservice.client.FederationService;
import org.monet.mobile.service.PushOperations;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.configuration.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AgentMobilePushService {
	private static AgentMobilePushService instance;

	private final Sender sender;

	protected AgentMobilePushService() {
        sender = new Sender(Configuration.getInstance().getMobilePushAPIKey());

	}

	public synchronized static AgentMobilePushService getInstance() {
		if (instance == null)
			instance = new AgentMobilePushService();
		return instance;
	}

	public synchronized void pushToAll(PushOperations operation, Map<String, String> parameters) {
		Message message = this.buildMessage(operation, parameters);

		try {
			DeviceList deviceList = ComponentFederation.getInstance().getFederationService().getAllMobileDevices();

			for (String userId : deviceList.getUsers()) {
				List<String> registrationIds = deviceList.getDevices(userId);
				if (registrationIds.size() == 0)
					continue;
				MulticastResult results = sender.send(message, registrationIds, 5);
				processResult(userId, registrationIds, results);
			}
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
	}

	public synchronized void push(String userId, PushOperations operation, Map<String, String> parameters) {
		AgentLogger agentLogger = AgentLogger.getInstance();
		Message message = buildMessage(operation, parameters);

		try {
			DeviceList deviceList = ComponentFederation.getInstance().getFederationService().getUserMobileDevices(userId);

			List<String> registrationIds = deviceList.getDevices();
			if (registrationIds.size() == 0)
				return;

			MulticastResult results = sender.send(message, registrationIds, 5);
			processResult(userId, registrationIds, results);
		} catch (Exception e) {
			agentLogger.error(e);
		}
	}

	private Message buildMessage(PushOperations operation, Map<String, String> parameters) {
		Message.Builder messageBuilder = new Message.Builder();
		messageBuilder.addData("operation", operation.toString());

		if (parameters != null) {
			for (Entry<String, String> entry : parameters.entrySet())
				messageBuilder.addData(entry.getKey(), entry.getValue());
		}

		return messageBuilder.build();
	}

	private void processResult(String userId, List<String> registrationIds, MulticastResult results) {
		AgentLogger agentLogger = AgentLogger.getInstance();
		FederationService federationService = ComponentFederation.getInstance().getFederationService();

		try {
			int index = 0;
			for (Result result : results.getResults()) {
				String deviceId = registrationIds.get(index);

				if (result.getMessageId() == null) {
					if (result.getErrorCodeName().equals(Constants.ERROR_NOT_REGISTERED))
						federationService.unRegisterMobileDevice(userId, deviceId);
					agentLogger.error("GCM Push Service: " + result.getErrorCodeName(), null);
				} else {
					if (result.getCanonicalRegistrationId() != null) {
						federationService.unRegisterMobileDevice(userId, deviceId);
						federationService.registerMobileDevice(userId, result.getCanonicalRegistrationId());
					}
				}
				index++;
			}
		} catch (Exception e) {
			agentLogger.error(e);
		}
	}

}
