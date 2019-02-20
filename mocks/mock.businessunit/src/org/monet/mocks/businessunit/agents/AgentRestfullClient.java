package org.monet.mocks.businessunit.agents;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.monet.mocks.businessunit.core.Configuration;
import org.monet.mocks.businessunit.utils.LibrarySigner;
import org.monet.mocks.businessunit.utils.StreamHelper;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class AgentRestfullClient {
	private Configuration configuration;
	private final AgentFilesystem agentFilesystem;

	public AgentRestfullClient(Configuration configuration, AgentFilesystem agentFilesystem) {
		this.configuration = configuration;
		this.agentFilesystem = agentFilesystem;
	}

	public static class Result {
		public InputStream content;
		public long size;
		public String type;

		public Result(InputStream content, long size, String type) {
			this.content = content;
			this.size = size;
			this.type = type;
		}
	}

	public static class RequestParameter implements Entry<String, ContentBody> {

		private String key;
		private ContentBody value;

		public RequestParameter(String key, ContentBody value) {
			this.key = key;
			this.value = value;
		}

		public final String SOURCE_NAME = "source-name";
		public final String ACTION = "action";
		public final String START_POS = "start-pos";
		public final String COUNT = "count";
		public final String MODE = "mode";
		public final String FLATTEN = "flatten";
		public final String DEPTH = "depth";
		public final String FROM = "from";
		public final String FILTERS = "filters";
		public final String SEARCH_TEXT = "search-text";

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public ContentBody getValue() {
			return value;
		}

		@Override
		public ContentBody setValue(ContentBody value) {
			this.value = value;
			return value;
		}

	}

	@SuppressWarnings("unchecked")
	public Result execute(HttpClient client, String url, boolean isPost, HashMap<String, ?> parameters) throws Exception {
        HttpRequestBase method = isPost ? new HttpPost(url) : new HttpGet(url);
		HttpResponse response;

		if (isPost) {
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (Entry<String, ?> param : parameters.entrySet()) {
				if (param.getValue() instanceof List<?>) {
					for (ContentBody contentBody : (List<ContentBody>) param.getValue())
						entityBuilder.addPart(param.getKey(), contentBody);
				} else {
                    entityBuilder.addPart(param.getKey(), (ContentBody) param.getValue());
				}
			}

			((HttpPost) method).setEntity(entityBuilder.build());
		}

		response = client.execute(method);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300)
			throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

		HttpEntity entity = response.getEntity();
		return new Result(entity.getContent(), entity.getContentLength(), entity.getContentType() != null ? entity.getContentType().getValue() : null);
	}

	public Result executePostMultiParams(String url, HashMap<String, List<ContentBody>> parameters) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		return this.execute(client, url, true, parameters);
	}

	public Result executePost(String url, HashMap<String, ContentBody> parameters) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		return this.execute(client, url, true, parameters);
	}

	public Result executeGet(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
		return this.execute(client, url, false, null);
	}

	public String executeWithAuth(String url, ArrayList<Entry<String, ContentBody>> parameters) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        StringBuilder requestArgsBuilder = new StringBuilder();
        HttpResponse response;

        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (Entry<String, ContentBody> param : parameters) {
			entityBuilder.addPart(param.getKey(), param.getValue());

			if (param.getValue() instanceof StringBody) {
				requestArgsBuilder.append(param.getKey());
				requestArgsBuilder.append("=");
				requestArgsBuilder.append(agentFilesystem.getReaderContent(((StringBody) param.getValue()).getReader()).trim());
				requestArgsBuilder.append("&");
			}
		}

		String timestamp = String.valueOf((new Date()).getTime());
		entityBuilder.addPart("timestamp", new StringBody(timestamp, ContentType.TEXT_PLAIN));

		requestArgsBuilder.append("timestamp=");
		requestArgsBuilder.append(timestamp);

		String certificateFilename = configuration.getCertificateFilename();

		try {
			String signature = LibrarySigner.signText(requestArgsBuilder.toString(), certificateFilename, configuration.getCertificatePassword());
			entityBuilder.addPart("signature", new StringBody(signature, ContentType.TEXT_PLAIN));
		} catch (Exception exception) {
			throw new RuntimeException(String.format("Could not sign message: %s with certificate: %s", requestArgsBuilder.toString(), certificateFilename));
		}

		post.setEntity(entityBuilder.build());

		response = client.execute(post);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300)
			throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

		return StreamHelper.toString(response.getEntity().getContent()).trim();
	}

	public HashMap<String, List<ContentBody>> convertParameterMap(HashMap<String, List<String>> parameters) throws UnsupportedEncodingException {
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

		return postParams;
	}

}
