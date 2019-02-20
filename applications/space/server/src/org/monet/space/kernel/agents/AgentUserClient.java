package org.monet.space.kernel.agents;

import org.monet.bpi.types.File;
import org.monet.space.kernel.model.ClientOperation;

import java.util.HashMap;
import java.util.Map;

public class AgentUserClient {
	private static AgentUserClient instance;
	private final Map<Long, ClientOperation> userOperations;
	private final Map<Long, String> userMessages;
	private final Map<Long, File> userFiles;

	protected AgentUserClient() {
		this.userOperations = new HashMap<>();
		this.userMessages = new HashMap<>();
		this.userFiles = new HashMap<>();
	}

	public synchronized static AgentUserClient getInstance() {
		if (instance == null) instance = new AgentUserClient();
		return instance;
	}

	public void sendOperationToUser(Long threadId, ClientOperation operation) {
		this.userOperations.put(threadId, operation);
	}

	public ClientOperation getOperationForUser(Long threadId) {
		if (!this.userOperations.containsKey(threadId)) return null;
		return this.userOperations.get(threadId);
	}

	public void deleteOperationToUser(Long threadId) {
		this.userOperations.remove(threadId);
	}

	public void sendMessageToUser(Long threadId, String message) {
		this.userMessages.put(threadId, message);
	}

	public String getMessageForUser(Long threadId) {
		if (!this.userMessages.containsKey(threadId)) return null;
		return this.userMessages.get(threadId);
	}

	public void deleteMessageToUser(Long threadId) {
		this.userMessages.remove(threadId);
	}

	public void sendFileToUser(Long threadId, File file) {
		this.userFiles.put(threadId, file);
	}

	public File getFileForUser(Long threadId) {
		if (!this.userFiles.containsKey(threadId)) return null;
		return this.userFiles.get(threadId);
	}

	public void deleteFileToUser(Long threadId) {
		this.userFiles.remove(threadId);
	}

	public void clear(Long threadId) {
		this.deleteMessageToUser(threadId);
		this.deleteOperationToUser(threadId);
		this.deleteFileToUser(threadId);
	}

}