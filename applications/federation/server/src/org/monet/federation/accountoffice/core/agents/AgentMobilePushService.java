package org.monet.federation.accountoffice.core.agents;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.DeviceList;
import org.monet.mobile.service.PushOperations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AgentMobilePushService {
	private final Configuration configuration;
	private final DataRepository dataRepository;
	private final Logger logger;

	@Inject
	public AgentMobilePushService(Configuration configuration, DataRepository dataRepository, Logger logger) {
		this.configuration = configuration;
		this.dataRepository = dataRepository;
		this.logger = logger;

		if (configuration.getMobileFCMSettingsFile().isEmpty() || configuration.getMobileFCMProjectId().isEmpty()) return;

		try {
			FileInputStream serviceAccount = new FileInputStream(configuration.getMobileFCMSettingsFile());

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://" + configuration.getMobileFCMProjectId() + ".firebaseio.com/")
					.build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			logger.error("Could not load FCM configuration", e);
		}
	}

	public synchronized void pushToAll(PushOperations operation, Map<String, String> parameters) {
		try {
			DeviceList deviceList = dataRepository.loadMobileDevices();
			for (String userId : deviceList.getUsers()) push(userId, operation, parameters);
		} catch (Exception e) {
			logger.error("Error sending message to all devices", e);
		}
	}

	public synchronized void push(String userId, PushOperations operation, Map<String, String> parameters) {

		try {
			DeviceList deviceList = dataRepository.loadMobileDevices(userId);

			List<String> registrationIds = deviceList.getDevices();
			if (registrationIds.size() == 0)
				return;

			for (String registrationId : registrationIds) {
				String result = FirebaseMessaging.getInstance().send(buildMessage(operation, parameters, registrationId));
				processResult(userId, registrationId, result);
			}

		} catch (Exception e) {
			logger.error(String.format("Error sending message to all devices of user %s", userId), e);
		}
	}

	private Message buildMessage(PushOperations operation, Map<String, String> parameters, String deviceId) {
		Message.Builder messageBuilder = Message.builder();
		messageBuilder.setToken(deviceId);
		messageBuilder.putData("operation", operation.toString());

		if (parameters != null) {
			for (Entry<String, String> entry : parameters.entrySet())
				messageBuilder.putData(entry.getKey(), entry.getValue());
		}

		return messageBuilder.build();
	}

	private void processResult(String result, String userId, String deviceId) {

		try {
			if (result.equals("messaging/registration-token-not-registered")) {
				dataRepository.unRegisterMobileDevice(userId, deviceId);
				logger.error("GCM Push Service: " + result);
			}
		} catch (Exception e) {
			logger.error("Process mobile result", e);
		}
	}

}
