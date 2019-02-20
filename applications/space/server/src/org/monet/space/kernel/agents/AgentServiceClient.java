package org.monet.space.kernel.agents;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.api.federation.setupservice.impl.library.LibraryString;
import org.monet.space.kernel.agents.AgentRestfullClient.RequestParameter;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class AgentServiceClient {

	private static AgentServiceClient oInstance;
	private AgentRestfullClient restfullClient;

	protected AgentServiceClient() {
		this.restfullClient = AgentRestfullClient.getInstance();
	}

	public static class ServiceResponse {
		public MailBoxUri mailBox;
		public String userId;
	}

	public synchronized static AgentServiceClient getInstance() {
		if (oInstance == null)
			oInstance = new AgentServiceClient();
		return oInstance;
	}

	public ServiceResponse requestService(MessageQueueItem item, TaskOrder taskOrder, InputStream messageStream) {
		try {
			String uri = item.getUri();
            MailBoxUri replyMailBox = MailBoxUri.build(LibraryString.cleanSpecialChars(StreamHelper.toString(messageStream)));
            FederationLayer federationLayer = ComponentFederation.getInstance().getDefaultLayer();

            if (Uri.isMonetUri(uri)) {
                Uri internalUri = (Uri) Uri.build(uri);
                String partnerName = internalUri.getPartner();
                if (federationLayer.locatePartner(internalUri.getPartner()) == null) {
                    AgentLogger.getInstance().error(String.format("Could not request service to partner: %s. It doesn't exists in federation!", partnerName), null);
                    return null;
                }
            }

			ArrayList<Entry<String, ContentBody>> parameters = new ArrayList<>();
			parameters.add(new RequestParameter("source-unit", toStringBody(String.valueOf(BusinessUnit.getInstance().getName()))));
			parameters.add(new RequestParameter("reply-mailbox", toStringBody(replyMailBox.toString())));
			parameters.add(new RequestParameter("urgent", toStringBody(String.valueOf(taskOrder.isUrgent()))));

			Date startDate = taskOrder.getInternalSuggestedStartDate();
			parameters.add(new RequestParameter("start-date", toStringBody(String.valueOf(startDate != null ? startDate.getTime() : ""))));
			Date endDate = taskOrder.getInternalSuggestedEndDate();
			parameters.add(new RequestParameter("end-date", toStringBody(String.valueOf(endDate != null ? endDate.getTime() : ""))));
			parameters.add(new RequestParameter("comments", toStringBody(taskOrder.getComments() != null ? LibraryEncoding.encode(taskOrder.getComments()) : "")));

			String rawResult = this.restfullClient.executeWithAuth(AgentFederationUnit.getUrl(uri), parameters);
			HashMap<String, String> resultMap = toMap(rawResult);
			String mailBoxUri = resultMap.get("mailbox");
			String signature = resultMap.get("signature");

			ServiceResponse result = new ServiceResponse();
			result.mailBox = MailBoxUri.build(mailBoxUri);

			String userId = federationLayer.validateRequest(signature, mailBoxUri);
			result.userId = userId;

			return result;
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			return null;
		}
	}

	public boolean sendMessage(MessageQueueItem item, InputStream messageStream) {
		try {
			ArrayList<Entry<String, ContentBody>> parameters = new ArrayList<Entry<String, ContentBody>>();
			parameters.add(new RequestParameter("message", new InputStreamBody(messageStream, ContentType.APPLICATION_OCTET_STREAM, "message")));
			parameters.add(new RequestParameter("message-code", toStringBody(item.getCode())));
			parameters.add(new RequestParameter("message-type", toStringBody(item.getType())));
			parameters.add(new RequestParameter("message-hash", toStringBody(item.getHash())));

            String mailboxUrl = AgentFederationUnit.getUrl(item.getUri());
			String resultString = this.restfullClient.executeWithAuth(mailboxUrl, parameters);
			boolean result = resultString.equals("OK");

			if (!result)
				item.setErrorMessage(resultString);

			return result;
		} catch (Exception e) {
			item.setErrorMessage(e.getClass().getName() + ": " + e.getMessage());
			return false;
		} finally {
			StreamHelper.close(messageStream);
		}
	}

	public boolean sendSignaling(MessageQueueItem item) {
		try {
			ArrayList<Entry<String, ContentBody>> parameters = new ArrayList<Entry<String, ContentBody>>();
			parameters.add(new RequestParameter("signaling", toStringBody(item.getCode())));

            String mailboxUrl = AgentFederationUnit.getUrl(item.getUri());
			String resultString = this.restfullClient.executeWithAuth(mailboxUrl, parameters);
			boolean result = resultString.equals("OK");

			if (!result)
				item.setErrorMessage(resultString);

			return result;
		} catch (Exception e) {
			item.setErrorMessage(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	private final HashMap<String, String> toMap(String parameters) {
		HashMap<String, String> values = new HashMap<String, String>();
		for (String keyValue : parameters.split("&")) {
			String[] keyValuePair = keyValue.trim().split("=");
			if (keyValuePair.length == 2) {
				values.put(keyValuePair[0], keyValuePair[1]);
			}
		}
		return values;
	}

    private StringBody toStringBody(String content) {
        return toStringBody(content, ContentType.TEXT_PLAIN);
    }

    private StringBody toStringBody(String content, ContentType contentType) {
        return new StringBody(content, contentType);
    }
    
}
