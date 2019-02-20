package org.monet.federation.accountoffice.core.agents;

import com.google.android.gcm.server.*;
import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.DeviceList;
import org.monet.mobile.service.PushOperations;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AgentMobilePushService {
	private final Configuration configuration;
	private final DataRepository dataRepository;
	private final Logger logger;

	private Sender sender = null;

	@Inject
	public AgentMobilePushService(Configuration configuration, DataRepository dataRepository, Logger logger) {
		this.configuration = configuration;
		this.dataRepository = dataRepository;
		this.logger = logger;
		if (!configuration.getMobilePushAPIKey().isEmpty())
        	sender = new Sender(configuration.getMobilePushAPIKey());
	}

	public synchronized void pushToAll(PushOperations operation, Map<String, String> parameters) {
		if (sender == null)
			return;

		Message message = this.buildMessage(operation, parameters);

		try {
			DeviceList deviceList = dataRepository.loadMobileDevices();

			for (String userId : deviceList.getUsers()) {
				List<String> registrationIds = deviceList.getDevices(userId);
				if (registrationIds.size() == 0)
					continue;
				MulticastResult results = sender.send(message, registrationIds, 5);
				processResult(userId, registrationIds, results);
			}
		} catch (Exception e) {
			logger.error("Error sending message to all devices", e);
		}
	}

	public synchronized void push(String userId, PushOperations operation, Map<String, String> parameters) {
		if (sender == null)
			return;

		Message message = buildMessage(operation, parameters);

		try {
			DeviceList deviceList = dataRepository.loadMobileDevices(userId);

			List<String> registrationIds = deviceList.getDevices();
			if (registrationIds.size() == 0)
				return;

			MulticastResult results = sender.send(message, registrationIds, 5);
			processResult(userId, registrationIds, results);
		} catch (Exception e) {
			logger.error(String.format("Error sending message to all devices of user %s", userId), e);
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

		try {
			int index = 0;
			for (Result result : results.getResults()) {
				String deviceId = registrationIds.get(index);

				if (result.getMessageId() == null) {
					if (result.getErrorCodeName().equals(Constants.ERROR_NOT_REGISTERED))
						dataRepository.unRegisterMobileDevice(userId, deviceId);
					logger.error("GCM Push Service: " + result.getErrorCodeName());
				} else {
					if (result.getCanonicalRegistrationId() != null) {
						dataRepository.unRegisterMobileDevice(userId, deviceId);
						dataRepository.registerMobileDevice(userId, result.getCanonicalRegistrationId());
					}
				}
				index++;
			}
		} catch (Exception e) {
			logger.error("Process mobile result", e);
		}
	}

}
