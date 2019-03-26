package org.monet.space.kernel.agents;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.DeviceList;
import org.monet.federation.accountservice.client.FederationService;
import org.monet.mobile.service.PushOperations;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.configuration.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AgentMobilePushService {
	private static AgentMobilePushService instance;

	protected AgentMobilePushService() {
		Configuration configuration = Configuration.getInstance();

		if (configuration.getMobileFCMSettingsFile().isEmpty() || configuration.getMobileFCMProjectId().isEmpty()) return;

		try {
			FileInputStream serviceAccount = new FileInputStream(configuration.getMobileFCMSettingsFile());

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://" + configuration.getMobileFCMProjectId() + ".firebaseio.com/")
					.build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			AgentLogger.getInstance().error(e);
		}
	}

	public synchronized static AgentMobilePushService getInstance() {
		if (instance == null)
			instance = new AgentMobilePushService();
		return instance;
	}

	public synchronized void pushToAll(PushOperations operation, Map<String, String> parameters) {
		try {
			DeviceList deviceList = ComponentFederation.getInstance().getFederationService().getAllMobileDevices();
			for (String userId : deviceList.getUsers()) push(userId, operation, parameters);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
	}

	public synchronized void push(String userId, PushOperations operation, Map<String, String> parameters) {
		AgentLogger agentLogger = AgentLogger.getInstance();

		try {
			DeviceList deviceList = ComponentFederation.getInstance().getFederationService().getUserMobileDevices(userId);

			List<String> deviceIds = deviceList.getDevices();
			if (deviceIds.size() == 0)
				return;

			for (String deviceId : deviceIds) {
				String result = FirebaseMessaging.getInstance().send(buildMessage(operation, parameters, deviceId));
				processResult(userId, deviceId, result);
			}

		} catch (Exception e) {
			agentLogger.error(e);
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
		AgentLogger agentLogger = AgentLogger.getInstance();
		FederationService federationService = ComponentFederation.getInstance().getFederationService();

		try {
			if (result.equals("messaging/registration-token-not-registered")) {
				federationService.unRegisterMobileDevice(userId, deviceId);
				agentLogger.error("GCM Push Service: " + result, null);
			}
		} catch (Exception e) {
			agentLogger.error(e);
		}
	}

}
