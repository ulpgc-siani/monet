package org.monet.space.kernel.agents;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.exceptions.CantSignException;
import org.monet.space.kernel.library.LibrarySigner;
import org.monet.space.kernel.utils.MessageHelper;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

public class AgentRestfullClient {

	private static final int Timeout = 1000 * 240;
	private static AgentRestfullClient instance;

	public synchronized static AgentRestfullClient getInstance() {
		if (instance == null) {
			instance = new AgentRestfullClient();
		}
		return instance;
	}

	private AgentRestfullClient() {
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

		public static final String SOURCE_NAME = "source-name";
		public static final String ACTION = "action";
		public static final String START_POS = "start-pos";
		public static final String COUNT = "count";
		public static final String MODE = "mode";
		public static final String FLATTEN = "flatten";
		public static final String DEPTH = "depth";
		public static final String FROM = "from";
		public static final String FILTERS = "filters";
		public static final String SEARCH_TEXT = "search-text";

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
	public Result execute(String url, boolean isPost, HashMap<String, ?> parameters) throws Exception {
		HttpClient client = buildClient();
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

		method.setConfig(requestConfig());
		response = client.execute(method);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300)
			throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

		HttpEntity entity = response.getEntity();
		return new Result(entity.getContent(), entity.getContentLength(), entity.getContentType() != null ? entity.getContentType().getValue() : null);
	}

	private HttpClient buildClient() {
		return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig()).build();
	}

	private RequestConfig requestConfig() {
		return RequestConfig.custom().setSocketTimeout(Timeout).setConnectTimeout(Timeout).setConnectionRequestTimeout(Timeout).build();
	}

	public Result executePostMultiParams(String url, HashMap<String, List<ContentBody>> parameters) throws Exception {
		return this.execute(url, true, parameters);
	}

	public Result executePost(String url, HashMap<String, ContentBody> parameters) throws Exception {
		return this.execute(url, true, parameters);
	}

	public Result executeGet(String url) throws Exception {
		return this.execute(url, false, null);
	}

	public String executeWithAuth(String url, ArrayList<Entry<String, ContentBody>> parameters) throws Exception {
		Configuration configuration = Configuration.getInstance();

		HttpClient client = buildClient();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		Map<String, String> parametersToSign = new HashMap<>();
		HttpResponse response;

		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (Entry<String, ContentBody> param : parameters) {
			entityBuilder.addPart(param.getKey(), param.getValue());

			if (param.getValue() instanceof StringBody) {
				parametersToSign.put(param.getKey(), AgentFilesystem.getReaderContent(((StringBody) param.getValue()).getReader()).trim());
			}
		}

		String timestamp = String.valueOf((new Date()).getTime());
		entityBuilder.addPart("timestamp", new StringBody(timestamp, ContentType.TEXT_PLAIN));
		parametersToSign.put("timestamp", timestamp);

		String requestArgsBuilder = MessageHelper.toQueryString(parametersToSign);
		try {
			String signature = LibrarySigner.signText(requestArgsBuilder, configuration.getCertificateFilename(), configuration.getCertificatePassword());
			entityBuilder.addPart("signature", new StringBody(signature, ContentType.TEXT_PLAIN));

			AgentLogger.getInstance().debug("AgentRestfullClient:executeWithAuth. Signature: %s. RequestArgs: %s.", signature, requestArgsBuilder);
		} catch (Exception exception) {
			throw new CantSignException(String.format("Could not sign message: %s with certificate: %s", requestArgsBuilder, configuration.getCertificateFilename()));
		}

		post.setEntity(entityBuilder.build());
		post.setConfig(requestConfig());
		response = client.execute(post);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300)
			throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

		return StreamHelper.toString(response.getEntity().getContent()).trim();
	}

	public static HashMap<String, List<ContentBody>> convertParameterMap(HashMap<String, List<String>> parameters) throws UnsupportedEncodingException {
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
