package org.monet.bpi;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.space.kernel.agents.AgentRestfullClient;
import org.monet.space.kernel.utils.StreamHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class HttpClient {

	public static String get(String url) throws Exception {
		return StreamHelper.toString(AgentRestfullClient.getInstance().executeGet(url).content);
	}

	public static String post(String url, HashMap<String, List<String>> parameters) throws Exception {
		HashMap<String, List<ContentBody>> postParams = new HashMap<String, List<ContentBody>>();
		for (Entry<String, List<String>> entry : parameters.entrySet()) {
			List<ContentBody> contentBodyValues = postParams.get(entry.getKey());
			if (contentBodyValues == null) {
				contentBodyValues = new ArrayList<ContentBody>();
				postParams.put(entry.getKey(), contentBodyValues);
			}

			for (String entryValue : entry.getValue()) {
				contentBodyValues.add(new StringBody(entryValue, ContentType.TEXT_PLAIN));
			}

		}
		return StreamHelper.toString(AgentRestfullClient.getInstance().executePostMultiParams(url, postParams).content);
	}

}
