package org.monet.mocks.businessunit;

import com.google.inject.Inject;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.mocks.businessunit.agents.AgentFilesystem;
import org.monet.mocks.businessunit.agents.AgentRestfullClient;
import org.monet.mocks.businessunit.agents.AgentRestfullClient.RequestParameter;
import org.monet.mocks.businessunit.core.Configuration;
import org.monet.mocks.businessunit.core.MonetUri;
import org.monet.mocks.businessunit.utils.LibraryFile;
import org.monet.mocks.businessunit.utils.LibraryZip;
import org.monet.mocks.businessunit.utils.StreamHelper;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BusinessUnitService implements Service {
	private AgentRestfullClient client;
	private Configuration configuration;

	private static final String SERVICE_URL = "%s/service/business/%s/";
	private static final String MAILBOX_URL = "%s/service/mailbox/%s/";

	@Inject
	public BusinessUnitService(Configuration configuration, AgentFilesystem agentFilesystem) {
		this.configuration = configuration;
		this.client = new AgentRestfullClient(configuration, agentFilesystem);
	}

	@Override
	public String requestService(String name, boolean urgent, Date startDate, Date endDate, String comments) {
		try {
			String url = String.format(SERVICE_URL, configuration.getClientUnitUrl(), name);
			String rawResult = client.executeWithAuth(url, createParameters(urgent, startDate, endDate, comments));
			HashMap<String, String> resultMap = toMap(rawResult);
			String monetUri = resultMap.get("mailbox");
			return toUrl(MonetUri.build(monetUri));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String toUrl(MonetUri uri) {
		return String.format(MAILBOX_URL, configuration.getClientUnitUrl(), uri.getId());
	}

	public boolean sendMessage(String mailboxUrl, String code, Message message) {
		Map<String, InputStream> streams = this.messageToStream(message);

		try {
			ArrayList<Map.Entry<String, ContentBody>> parameters = new ArrayList<Map.Entry<String, ContentBody>>();
			parameters.add(new RequestParameter("message", new InputStreamBody(streams.get("message"), ContentType.APPLICATION_OCTET_STREAM, "message")));
			parameters.add(new RequestParameter("message-code", toStringBody(code)));
			parameters.add(new RequestParameter("message-type", toStringBody("default")));
			parameters.add(new RequestParameter("message-hash", toStringBody(StreamHelper.calculateHashToHexString(streams.get("hash")))));

			String resultString = client.executeWithAuth(mailboxUrl, parameters);
			boolean result = resultString.equals("OK");

			if (!result)
				return false;

			return result;
		} catch (Exception e) {
			return false;
		} finally {
			for (InputStream stream : streams.values())
				StreamHelper.close(stream);
		}
	}

	public Message readMessage(InputStream stream) {
		Message message = null;

		try {
			message = streamToMessage(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return message;
	}

	private ArrayList<Map.Entry<String,ContentBody>> createParameters(boolean urgent, Date startDate, Date endDate, String comments) {
		ArrayList<Map.Entry<String, ContentBody>> parameters = new ArrayList<Map.Entry<String, ContentBody>>();
		parameters.add(new RequestParameter("source-unit", toStringBody(configuration.getUnitName())));
		parameters.add(new RequestParameter("reply-mailbox", toStringBody(configuration.getUnitCallbackUrl())));
		parameters.add(new RequestParameter("urgent", toStringBody(String.valueOf(urgent))));
		parameters.add(new RequestParameter("start-date", toStringBody(startDate!=null?String.valueOf(startDate.getTime()):"")));
		parameters.add(new RequestParameter("end-date", toStringBody(endDate!=null?String.valueOf(endDate.getTime()):"")));
		parameters.add(new RequestParameter("comments", toStringBody(comments)));
		return parameters;
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

	private Map<String, InputStream> messageToStream(Message message) {
		File messageFile = null;
		Map<String, InputStream> streams = new HashMap<String, InputStream>();

		try {

			boolean isZIP = message.getAttachments().size() > 0;
			if (isZIP) {
				messageFile = File.createTempFile("Message", "temp");
				ZipOutputStream outputStream = null;
				try {
					outputStream = new ZipOutputStream(new FileOutputStream(messageFile));

					if (message.getContent() != null)
						LibraryZip.addZipEntry(".content", null, new ByteArrayInputStream(message.getContent().getBytes("UTF-8")), outputStream);

					int i = 0;
					Map<String, File> attachments = message.getAttachments();
					for (Map.Entry<String, File> attachment : attachments.entrySet()) {
						String key = attachment.getKey();
						File content = attachment.getValue();
						LibraryZip.addZipEntry(String.format("attach_%d.%s", i, LibraryFile.getContentType(content)), key, new FileInputStream(content), outputStream);
						i++;
					}

				} finally {
					StreamHelper.close(outputStream);
				}

				streams.put("message", new FileInputStream(messageFile));
				streams.put("hash", new FileInputStream(messageFile));
			} else {
				byte[] content = message.getContent() != null ? message.getContent().getBytes("UTF-8") : new byte[0];
				streams.put("message", new ByteArrayInputStream(content));
				streams.put("hash", new ByteArrayInputStream(content));
			}

			return streams;
		} catch (Exception ex) {
			throw new RuntimeException("Can't queue message", ex);
		} finally {
			if (messageFile != null)
				messageFile.delete();
		}
	}

	private Message streamToMessage(final InputStream stream) throws IOException {
		final File result = streamToFile(stream);

		Message message = new Message() {
			private String content = null;
			private Map<String, File> attachments = new HashMap<String, File>();
			private boolean parsed = false;
			private File rawFile = result;

			@Override
			public Map<String, File> getAttachments() {
				parseStream();
				return attachments;
			}

			@Override
			public void addAttachment(String key, File file) {
			}

			@Override
			public String getContent() {
				parseStream();
				return content;
			}

			@Override
			public void setContent(String content) {
			}

			private void parseStream() {
				if (parsed)
					return;

				File messageDir = new File(rawFile.getAbsolutePath() + "_content/");
				messageDir.mkdirs();

				try {
					if (isZipFile(rawFile)) {
						ZipInputStream zipStream = new ZipInputStream(new FileInputStream(rawFile));
						messageDir.mkdirs();

						ZipEntry entry;
						while ((entry = zipStream.getNextEntry()) != null) {
							String name = entry.getName();
							if (name.equals(".content")) {
								content = StreamHelper.toString(zipStream);
							} else {
								if (entry.getExtra() == null)
									continue;
								String key = new String(entry.getExtra(), "UTF-8");
								File entryFile = new File(messageDir, name);
								FileOutputStream entryOutputStream = null;
								try {
									entryOutputStream = new FileOutputStream(entryFile);
									StreamHelper.copyData(zipStream, entryOutputStream);
								} finally {
									StreamHelper.close(entryOutputStream);
								}

								attachments.put(key, entryFile);
							}
						}
					} else {
						FileInputStream messageFileStream = null;
						try {
							messageFileStream = new FileInputStream(rawFile);
							content = StreamHelper.toString(messageFileStream);
						} finally {
							StreamHelper.close(messageFileStream);
						}
					}
				}
				catch (IOException exception) {
					exception.printStackTrace();
				}

				parsed = true;
			}

			private boolean isZipFile(File file) throws IOException {
				if (file.isDirectory())
					return false;
				if (!file.canRead())
					throw new IOException("Cannot read file " + file.getAbsolutePath());
				if (file.length() < 4)
					return false;
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
				int test = in.readInt();
				in.close();
				return test == 0x504b0304;
			}
		};

		return message;
	}

	private File streamToFile(InputStream message) throws IOException {
		FileOutputStream messageFileOutputStream = null;
		File result = File.createTempFile(UUID.randomUUID().toString(), "");

		try {
			messageFileOutputStream = new FileOutputStream(result);
			byte[] buffer = new byte[8192];
			int bytesRead = 0;
			while ((bytesRead = message.read(buffer)) > 0) {
				messageFileOutputStream.write(buffer, 0, bytesRead);
			}
		} finally {
			StreamHelper.close(messageFileOutputStream);
		}

		return result;
	}

}
