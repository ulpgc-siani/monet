package org.monet.api.space.backservice.impl.library;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class LibraryRestfull {

	private static final int TIMEOUT = 60*60*24;

	public static class RequestParameter implements Entry<String, ContentBody> {

		private String key;
		private ContentBody value;

		public RequestParameter(String key, ContentBody value) {
			this.key = key;
			this.value = value;
		}

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

	private static String getReaderContent(Reader oReader) throws IOException {
		StringBuffer sbContent = new StringBuffer();
		BufferedReader oBufferedReader;
		String sLine;

		oBufferedReader = new BufferedReader(oReader);
		while ((sLine = oBufferedReader.readLine()) != null) {
			sbContent.append(sLine + "\n");
		}

		return sbContent.toString();
	}

    public static InputStream requestStream(String url, HashMap<String, ContentBody> parameters, String certificateFile, String certificatePassword) throws Exception {
        return requestStream(url, parameters, new FileInputStream(certificateFile), certificatePassword, TIMEOUT);
    }

	public static InputStream requestStream(String url, HashMap<String, ContentBody> parameters, String certificateFile, String certificatePassword, int timeoutInSeconds) throws Exception {
		return requestStream(url, parameters, new FileInputStream(certificateFile), certificatePassword, timeoutInSeconds);
	}

	public static InputStream requestStream(String url, HashMap<String, ContentBody> parameters, InputStream certificate, String certificatePassword) throws Exception {
		return requestStream(url, parameters, certificate, certificatePassword, TIMEOUT);
	}

	public static InputStream requestStream(String url, HashMap<String, ContentBody> parameters, InputStream certificate, String certificatePassword, int timeoutInSeconds) throws Exception {
		try {
			return requestStreamWithSortedParameters(url, parameters, certificate, certificatePassword, timeoutInSeconds);
		}
		catch (Throwable ex) {
			return requestStreamWithParameters(url, parameters, certificate, certificatePassword, timeoutInSeconds);
		}
	}

	private static InputStream requestStreamWithParameters(String url, HashMap<String, ContentBody> parameters, InputStream certificate, String certificatePassword, int timeoutInSeconds) throws Exception {
		RequestConfig configuration = RequestConfig.custom().setSocketTimeout(timeoutInSeconds * 1000).setStaleConnectionCheckEnabled(true).build();
		HttpClient client = HttpClients.custom().setDefaultRequestConfig(configuration).build();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		StringBuilder requestArgsBuilder = new StringBuilder();
		HttpResponse response;

		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (Entry<String, ContentBody> param : parameters.entrySet()) {
			entityBuilder.addPart(param.getKey(), param.getValue());

			if (param.getValue() instanceof StringBody) {
				if (param.getKey().equals("op")) continue;
				requestArgsBuilder.append(param.getKey());
				requestArgsBuilder.append("=");
				requestArgsBuilder.append(LibraryRestfull.getReaderContent(((StringBody) param.getValue()).getReader()).trim());
				requestArgsBuilder.append("&");
			}
		}

		String timestamp = String.valueOf((new Date()).getTime());
		entityBuilder.addPart("timestamp", new StringBody(timestamp, ContentType.TEXT_PLAIN));

		requestArgsBuilder.append("timestamp=");
		requestArgsBuilder.append(timestamp);

		String hash = requestArgsBuilder.toString();
		entityBuilder.addPart("hash", new StringBody(hash, ContentType.TEXT_PLAIN));

		String signature = LibrarySigner.signText(hash, certificate, certificatePassword);
		entityBuilder.addPart("signature", new StringBody(signature, ContentType.TEXT_PLAIN));

		post.setEntity(entityBuilder.build());

		response = client.execute(post);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300)
			throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

		return response.getEntity().getContent();
	}

	private static InputStream requestStreamWithSortedParameters(String url, HashMap<String, ContentBody> parameters, InputStream certificate, String certificatePassword, int timeoutInSeconds) throws Exception {
		RequestConfig configuration = RequestConfig.custom().setSocketTimeout(timeoutInSeconds * 1000).setStaleConnectionCheckEnabled(true).build();
		HttpClient client = HttpClients.custom().setDefaultRequestConfig(configuration).build();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		Map<String, String> parametersToSign = new HashMap<>();
		HttpResponse response;

		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (Entry<String, ContentBody> param : parameters.entrySet()) {
			entityBuilder.addPart(param.getKey(), param.getValue());

			if (param.getValue() instanceof StringBody) {
				if (param.getKey().equals("op")) continue;
				parametersToSign.put(param.getKey(), LibraryRestfull.getReaderContent(((StringBody) param.getValue()).getReader()).trim());
			}
		}

		String timestamp = String.valueOf((new Date()).getTime());
		entityBuilder.addPart("timestamp", new StringBody(timestamp, ContentType.TEXT_PLAIN));
		parametersToSign.put("timestamp", timestamp);

		String hash = toQueryString(parametersToSign);
		entityBuilder.addPart("hash", new StringBody(hash, ContentType.TEXT_PLAIN));

		String signature = LibrarySigner.signText(hash, certificate, certificatePassword);
		entityBuilder.addPart("signature", new StringBody(signature, ContentType.TEXT_PLAIN));

		post.setEntity(entityBuilder.build());

		response = client.execute(post);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300)
			throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

		return response.getEntity().getContent();
	}

	public static String request(String url, HashMap<String, ContentBody> parameters, String certificateFilename, String certificatePassword) throws Exception {
		InputStream stream = LibraryRestfull.requestStream(url, parameters, new FileInputStream(certificateFilename), certificatePassword);
		InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
		return LibraryRestfull.getReaderContent(reader).trim();
	}

    public static String request(String url, HashMap<String, ContentBody> parameters, InputStream certificate, String certificatePassword) throws Exception {
        InputStream stream = LibraryRestfull.requestStream(url, parameters, certificate, certificatePassword);
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        return LibraryRestfull.getReaderContent(reader).trim();
    }

	public static String request(String url, HashMap<String, ContentBody> parameters, InputStream certificate, String certificatePassword, int timeoutInSeconds) throws Exception {
		InputStream stream = LibraryRestfull.requestStream(url, parameters, certificate, certificatePassword, timeoutInSeconds);
		InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
		return LibraryRestfull.getReaderContent(reader).trim();
	}

	public static String toQueryString(Map<String, String> parameters) {
		SortedSet<String> keys = new TreeSet<>(parameters.keySet());
		StringBuilder result = new StringBuilder();
		for (String key : keys) {
			result.append(key);
			result.append("=");
			result.append(parameters.get(key));
			result.append("&");
		}
		return result.length() > 0 ? result.substring(0, result.length()-1) : "";
	}

}