package org.monet.mocks.singlesignon.service.federation;

import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.net.Socket;

public class FederationConnector {

	private String host;
	private int port;
	private String labels;
	private String idUnity;
	private ConnectionClientHandler connection;
	private long msgId = 0;
	private SocketMessageModel.UnitInfo presentationLabel;

	private final String REQUEST_INIT = "InitRequest";
	private final String REQUEST_ISLOGGED = "IsLogged";
	private final String REQUEST_LOGOUT = "Logout";
	private final String REQUEST_TERMINATE_CONNECTION = "Close";

	private long nextId() {
		if (msgId == Long.MAX_VALUE)
			msgId = 0;
		return this.msgId++;
	}

	/**
	 * @param socketHost
	 * @param socketPort
	 * @param idUnity
	 * @param labels  To use this param use this XML format:
	 *                <p/>
	 *                <pre>
	 *                                              {@code<info>
	 *                                                 <label lang="ES">Text</label>
	 *                                                 <label lang="EN">Text</label>
	 *                                                 .
	 *                                                 .
	 *                                                 .
	 *                                                 <description lang="ES">Text</description>
	 *                                                 <description lang="EN">Text</description>
	 *                                                 .
	 *                                                 .
	 *                                                 .
	 *                                              </info>}
	 *                                              </pre>
	 * @throws Exception
	 */
	public FederationConnector(String socketHost, int socketPort, String idUnity, String labels) throws Exception {
		this.host = socketHost;
		this.port = socketPort;
		this.idUnity = idUnity;
		this.labels = labels.replace("info>", "PresentationLabel>");
		Serializer serializer = new Persister();
		presentationLabel = serializer.read(SocketMessageModel.UnitInfo.class, this.labels, false);
	}

	private SocketMessageModel createHeaderMessage(String action) {
		String msgID = String.valueOf(nextId());
		SocketMessageModel socketMessage = new SocketMessageModel();
		socketMessage.setId(msgID);
		socketMessage.setAction(action);
		socketMessage.setIdUnit(this.idUnity);
		return socketMessage;
	}

	public void init() throws Exception {
		if (this.connection != null && this.connection.isOpen())
			return;
		this.connect();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_INIT);
		socketMessage.setPresentionLabel(this.presentationLabel);

		SocketResponseMessage response = this.connection.execute(socketMessage, true);
		if (response.getResponse() == null)
			throw new Exception("Error init socket communication");
	}

	public boolean isLogged(String accessToken, String verifier) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_ISLOGGED);
		socketMessage.setAccesToken(accessToken);
		socketMessage.setVerifier(verifier);

		SocketResponseMessage response = this.connection.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public boolean logout(String accessToken, String verifier) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOGOUT);
		socketMessage.setAccesToken(accessToken);
		socketMessage.setVerifier(verifier);

		SocketResponseMessage response = this.connection.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public void close() throws Exception {
		if (this.connection != null) {
			if (!this.connection.isOpen())
				return;
			SocketMessageModel socketMessage = createHeaderMessage(REQUEST_TERMINATE_CONNECTION);
			this.connection.execute(socketMessage, false);
			this.connection.close();
		}
	}

	private void connect() throws Exception {
		Socket socket = new Socket(host, port);
		socket.setSoTimeout(30000);
		this.connection = new ConnectionClientHandler(socket);
	}

}
