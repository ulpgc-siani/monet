package org.monet.space.mobile.control.actions;

import org.monet.mobile.service.Response;
import org.monet.mobile.service.Result;
import org.monet.http.HttpRequest;
import org.monet.http.HttpResponse;
import org.monet.space.frontservice.configuration.Configuration;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.monet.federation.FederationApi;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Federation;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.PersisterHelper;
import org.monet.space.kernel.utils.StreamHelper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class Action<U> {

	protected static final String ID = "id";
	protected static final String EXTRA = "extra";

	protected AgentLogger agentLogger;

	public Action() {
		this.agentLogger = AgentLogger.getInstance();
	}

	protected FederationApi getFederationApi() {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Federation federation = businessUnit.getFederation();

		FederationApi api = new FederationApi();
        api.setUrl(federation.getUri());

		return api;
	}

	public U execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception{
		return execute(new HttpRequest(httpRequest), new HttpResponse(httpResponse));
	}

	public abstract U execute(org.monet.http.Request httpRequest, org.monet.http.Response httpResponse) throws Exception;

	protected <T> T getRequest(HttpServletRequest httpRequest, Class<T> requestClass) throws Exception, IOException {
		return getRequest(new HttpRequest(httpRequest), requestClass);
	}

	protected <T> T getRequest(org.monet.http.Request httpRequest, Class<T> requestClass) throws Exception, IOException {
		T request = null;
		Serializer serializer = new Persister();
		String requestData = httpRequest.getParameter("request");
		if (requestData != null)
			request = serializer.read(requestClass, requestData);
		else {
			String contentEncoding = httpRequest.getHeader("Content-Encoding");
			InputStream inputStream = httpRequest.getInputStream();
			if (contentEncoding != null && contentEncoding.equals("gzip"))
				inputStream = new GZIPInputStream(inputStream);
			request = serializer.read(requestClass, inputStream);
		}
		return request;
	}

	protected File getRequestContentAsFile(HttpServletRequest httpRequest, boolean createIfNull) throws IOException {
		return getRequestContentAsFile(new org.monet.http.HttpRequest(httpRequest), createIfNull);
	}

	protected File getRequestContentAsFile(org.monet.http.Request httpRequest, boolean createIfNull) throws IOException {
		boolean existsFile = true;

		httpRequest.getInputStream().read(new byte[0]);

		if (httpRequest.getInputStream().available() == 0) {
			if (!createIfNull)
				throw new RuntimeException("No data to save");
			existsFile = false;
		}

		File tempDir = this.getTempDir();
		File messageFile = File.createTempFile("Message", "", tempDir);

		if (!existsFile)
			return messageFile;

		FileOutputStream tempStream = null;
		try {
			tempStream = new FileOutputStream(messageFile);
			StreamHelper.copyData(httpRequest.getInputStream(), tempStream);
			StreamHelper.close(tempStream);
		} finally {
			StreamHelper.close(tempStream);
		}

		return messageFile;
	}

	private File getTempDir() {
		File tempDir = new File(Configuration.getInstance().getTempDir());
		if (!tempDir.exists())
			tempDir.mkdirs();
		return tempDir;
	}

	protected void writeResponse(HttpServletResponse httpResponse, Result result) throws Exception {
		writeResponse(new org.monet.http.HttpResponse(httpResponse), result);
	}

	protected void writeResponse(org.monet.http.Response httpResponse, Result result) throws Exception {
		GZIPOutputStream outputStream = null;
		try {
			httpResponse.setHeader("Content-Encoding", "gzip");
			httpResponse.setContentType("text/xml");
			outputStream = new GZIPOutputStream(httpResponse.getOutputStream());
			StringWriter writer = new StringWriter();
			PersisterHelper.save(writer, new Response(result));
			outputStream.write(writer.toString().getBytes("UTF-8"));
		} finally {
			StreamHelper.close(outputStream);
			StreamHelper.close(httpResponse.getOutputStream());
		}
	}

	protected void writeResponse(HttpServletResponse httpResponse, File file) throws Exception {
		this.writeResponse(httpResponse, MimeTypes.getInstance().getFromFilename(file.getName()), new FileInputStream(file), file.getName());
	}

	protected void writeResponse(HttpServletResponse httpResponse, String contentType, InputStream inputStream, String filename) throws Exception {
		writeResponse(new org.monet.http.HttpResponse(httpResponse), contentType, inputStream, filename);
	}

	protected void writeResponse(org.monet.http.Response httpResponse, String contentType, InputStream inputStream, String filename) throws Exception {
		OutputStream outputStream = httpResponse.getOutputStream();

		httpResponse.setContentType(contentType != null ? contentType : "application/octet-stream");
		if (filename != null)
			httpResponse.setHeader("Content-Disposition", "attachment;filename=" + filename);

		try {
			if (inputStream != null)
				StreamHelper.copyData(inputStream, outputStream);
		} finally {
			StreamHelper.close(inputStream);
			StreamHelper.close(outputStream);
		}
	}

	protected FederationLayer.Configuration createConfiguration(final org.monet.http.Request httpRequest) {
		return new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return httpRequest.getSessionId();
			}

			@Override
			public String getCallbackUrl() {
				return null;
			}

			@Override
			public String getLogoUrl() {
				return null;
			}

			@Override
			public org.monet.http.Request getRequest() {
				return httpRequest;
			}
		};
	}
}
