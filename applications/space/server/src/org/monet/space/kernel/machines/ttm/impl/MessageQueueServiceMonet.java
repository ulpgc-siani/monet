package org.monet.space.kernel.machines.ttm.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.agents.AgentServiceClient;
import org.monet.space.kernel.agents.AgentServiceClient.ServiceResponse;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.MailBoxLayer;
import org.monet.space.kernel.components.layers.MessageQueueLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.library.LibraryZip;
import org.monet.space.kernel.machines.ttm.MessageQueueService;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.model.MessageQueueItem;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.TaskOrder;
import org.monet.space.kernel.model.WorkQueueItem;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;
import org.monet.space.kernel.workqueue.WorkQueueAction;

import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static org.monet.space.kernel.machines.ttm.persistence.PersistenceService.MonetReferenceFileExtension;

@Singleton
public class MessageQueueServiceMonet implements MessageQueueService {

	private static final int SECOND = 1000;
	private static final int MINUTE = SECOND * 60;
	private static final int HOUR = MINUTE * 60;
	private static final int DAY = HOUR * 24;

	@Inject
	private MessageQueueLayer messageQueueLayer;

	@Override
	public long push(String orderId, Message message) {
		File messageFile = null;
		InputStream messageStream = null;
		String mailBox = message.getTo();

		try {
			String messageHash = null;

			boolean isZIP = message.getAttachments().size() > 0;
			if (isZIP) {
				messageFile = File.createTempFile("Message", "", new File(Configuration.getInstance().getTempDir()));
				ZipOutputStream outputStream = null;
				try {
					outputStream = new ZipOutputStream(new FileOutputStream(messageFile));

					if (message.getContent() != null)
						LibraryZip.addZipEntry(".content", null, new ByteArrayInputStream(message.getContent().getBytes("UTF-8")), outputStream);

					int i = 0;
					for (MessageAttach attach : message.getAttachments()) {
						LibraryZip.addZipEntry(String.format("attach_%d.%s", i, MimeTypes.getInstance().getExtension(attach.getContentType())), attach.getKey(), attach.getInputStream(), outputStream);
						if (attach.isDocument()) LibraryZip.addZipEntry(String.format("attach_%d" + MonetReferenceFileExtension, i), String.format("%s;%s", attach.getKey(), MimeTypes.TEXT), attach.getInputStream(), outputStream);
						i++;
					}
				} finally {
					StreamHelper.close(outputStream);
				}

				try {
					messageStream = new FileInputStream(messageFile);
					messageHash = StreamHelper.calculateHashToHexString(messageStream);
				} finally {
					StreamHelper.close(messageStream);
				}
				messageStream = new FileInputStream(messageFile);
			} else {
				byte[] content = message.getContent() != null ? message.getContent().getBytes("UTF-8") : new byte[0];
				messageStream = new ByteArrayInputStream(content);
				messageHash = StreamHelper.calculateHashToHexString(messageStream);
				messageStream.reset();
			}

			return this.messageQueueLayer.create(orderId, mailBox, messageStream, message.getSubject(), message.getType(), messageHash);
		} catch (Exception ex) {
			throw new RuntimeException("Can't queue message", ex);
		} finally {
			StreamHelper.close(messageStream);
			if (messageFile != null)
				messageFile.delete();
		}
	}

	@Override
	public State getState(long messageId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static class MessageQueueRetryWork extends WorkQueueAction {

		private AgentNotifier agentNotifier = AgentNotifier.getInstance();

		@Override
		public void execute(WorkQueueItem workItem) throws Exception {
			ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
			TaskLayer taskLayer = componentPersistence.getTaskLayer();
			MailBoxLayer mailBoxLayer = componentPersistence.getMailBoxLayer();
			MessageQueueLayer messageQueueLayer = componentPersistence.getMessageQueueLayer();
			AgentServiceClient agentServiceClient = AgentServiceClient.getInstance();
			List<MessageQueueItem> items = messageQueueLayer.loadNotSent();
			HashSet<String> failedOrderIds = new HashSet<String>();

			for (MessageQueueItem item : items) {

				if (failedOrderIds.contains(item.getOrderId()))
					continue;

				if (!execute(item))
					continue;

				boolean isSentSuccessfully = false;
				InputStream messageStream = null;
				try {
					if (item.getType().equals(Message.Type.REQUEST_SERVICE)) {
						messageStream = messageQueueLayer.getMessageStream(item);
						TaskOrder taskOrder = taskLayer.loadTaskOrder(item.getOrderId());
						ServiceResponse response = agentServiceClient.requestService(item, taskOrder, messageStream);

						if (response != null) {
							isSentSuccessfully = true;

							HashMap<String, Object> parameters = new HashMap<String, Object>();
							parameters.put(MonetEvent.PARAMETER_MAILBOX, response.mailBox);
							parameters.put(MonetEvent.PARAMETER_USER_ID, response.userId);
							agentNotifier.notify(new MonetEvent(MonetEvent.TASK_ORDER_REQUEST_SUCCESS, null, taskOrder.getTaskId(), parameters));
						}
					} else if (item.getType().equals(Message.Type.SIGNALING)) {
						if (agentServiceClient.sendSignaling(item)) {
							mailBoxLayer.deleteWithTaskId(taskLayer.loadTaskOrder(item.getOrderId()).getTaskId());
							isSentSuccessfully = true;
						}
					} else {
						messageStream = messageQueueLayer.getMessageStream(item);
						isSentSuccessfully = agentServiceClient.sendMessage(item, messageStream);

                        String errorMessage = item.getErrorMessage();
                        if (!isSentSuccessfully && errorMessage != null && errorMessage.equals("CLOSED")) {
                            TaskOrder taskOrder = taskLayer.loadTaskOrder(item.getOrderId());
                            taskOrder.setClosed(true);
                            taskLayer.saveTaskOrder(taskOrder);
                            isSentSuccessfully = true;
                        }
                        else if (isSentSuccessfully && item.getType().equals(Message.Type.CHAT)) {
							HashMap<String, Object> parameters = new HashMap<String, Object>();
							parameters.put(MonetEvent.PARAMETER_ORDER_ID, item.getOrderId());
							parameters.put(MonetEvent.PARAMETER_CHAT_ITEM_ID, item.getCode());
							agentNotifier.notify(new MonetEvent(MonetEvent.TASK_ORDER_CHAT_MESSAGE_SENT, null, item.getOrderId(), parameters));
						}
					}
				} catch (Throwable ex) {
					isSentSuccessfully = false;
					item.setErrorMessage(ex.getMessage());
					AgentLogger.getInstance().error(ex);
				} finally {
					StreamHelper.close(messageStream);
				}
				if (isSentSuccessfully) {
					messageQueueLayer.delete(item);
				} else {
					item.setRetries(item.getRetries() + 1);
					try {
						messageQueueLayer.save(item);
					} catch (Exception e) {
						AgentLogger.getInstance().error(e);
					}

					if (item.getType().equals(Message.Type.REQUEST_SERVICE)) {
						TaskOrder taskOrder = taskLayer.loadTaskOrder(item.getOrderId());
						agentNotifier.notify(new MonetEvent(MonetEvent.TASK_ORDER_REQUEST_FAILURE, null, taskOrder.getTaskId()));
					}

					failedOrderIds.add(item.getOrderId());
				}
			}
		}

		private boolean execute(MessageQueueItem item) {
			int retries = item.getRetries();
			Date lastUpdateTime = item.getLastUpdateTime();
			Date now = new Date();
			long difference = now.getTime() - lastUpdateTime.getTime();

			if (retries == 0)
				return true;

			if (retries < 10 && difference > MINUTE)
				return true;

			if (retries < 100 && difference > HOUR)
				return true;

			if (retries >= 1000 && difference > DAY)
				return true;

			return false;
		}

	}

}
