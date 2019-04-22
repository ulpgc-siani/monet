package org.monet.federation.accountservice.client;

import org.apache.commons.codec.binary.Base64;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.*;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel.SignCertificate;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel.SimpleElement;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel.UnitInfo;
import org.monet.federation.accountservice.client.utils.Utils;
import org.monet.http.HttpRequest;
import org.monet.http.Request;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class FederationService {

	private String host;
	private int port;
	private String labels;
	private String idUnity;
	private ConnectionClientHandler conn;
	private long msgId = 0;
	private UnitInfo presentationLabel;

	private final String REQUEST_INIT = "InitRequest";
	private final String REQUEST_PING = "PingRequest";
	private final String REQUEST_ISLOGGED = "IsLogged";
	private final String REQUEST_LOGOUT = "Logout";
	private final String EXISTS_ACCOUNT = "ExistsAccount";
	private final String REQUEST_ACCOUNT = "GetAccount";
	private final String REQUEST_GENERATE_ACCOUNT_ID = "GenerateAccountId";
	private final String REQUEST_GET_INFO = "GetInfo";
	private final String REQUEST_SEARCH_USERS = "SearchUsers";
	private final String REQUEST_LOAD_MEMBERS = "LoadMembers";
	private final String REQUEST_LOAD_PARTNERS = "LoadPartners";
	private final String REQUEST_EXISTS_PARTNER = "ExistsPartner";
	private final String REQUEST_LOAD_PARTNER = "LoadPartner";
	private final String REQUEST_LOCATE_PARTNER = "LocatePartner";
	private final String REQUEST_VALIDATE_REQUEST = "ValidateRequest";
	private final String REQUEST_SIGN_CERTIFICATE = "SignCertificate";
	private final String REQUEST_TERMINATE_CONNECTION = "Close";
	private final String REQUEST_LOAD_USER_MOBILE_DEVICES = "LoadUserMobileDevices";
	private final String REQUEST_LOAD_MOBILE_DEVICES = "LoadMobileDevices";
	private final String REQUEST_REGISTER_MOBILE_DEVICE = "RegisterMobileDevice";
	private final String REQUEST_UNREGISTER_MOBILE_DEVICE = "UnRegisterMobileDevice";

	private long nextId() {
		if (msgId == Long.MAX_VALUE)
			msgId = 0;
		return this.msgId++;
	}

	/**
	 * @param host
	 * @param port
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
	public FederationService(String host, int port, String idUnity, String labels) throws Exception {
		this.host = host;
		this.port = port;
		this.idUnity = idUnity;
		this.labels = labels.replace("info>", "PresentationLabel>");
		Serializer serializer = new Persister();
		presentationLabel = serializer.read(UnitInfo.class, this.labels, false);
	}

	private SocketMessageModel createHeaderMessage(String action) {
		String msgID = String.valueOf(nextId());
		SocketMessageModel socketMessage = new SocketMessageModel();
		socketMessage.setId(msgID);
		socketMessage.setAction(action);
		socketMessage.setIdUnit(this.idUnity);
		return socketMessage;
	}

	private boolean ping() {
		try {
			SocketMessageModel socketMessage = createHeaderMessage(REQUEST_PING);
			SocketResponseMessage response = this.conn.execute(socketMessage, true);

			return response != null && response.getResponse() != null && response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
		} catch (Exception e) {
			return false;
		}
	}

	public void init() throws Exception {
		if (this.conn != null && this.conn.isOpen() && ping())
			return;

		this.connect();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_INIT);
		socketMessage.setPresentionLabel(this.presentationLabel);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		if (response.getResponse() == null)
			throw new Exception("Error init socket communication");
	}

	public boolean isLogged(String accessToken, HttpServletRequest request) throws Exception {
		return isLogged(accessToken, new HttpRequest(request));
	}

	public boolean isLogged(String accessToken, Request request) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_ISLOGGED);
		socketMessage.setAccesToken(accessToken);
		socketMessage.setVerifier(calculateVerifier(request));

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public boolean logout(String accessToken, HttpServletRequest request) throws Exception {
		return logout(accessToken, new HttpRequest(request));
	}

	public boolean logout(String accessToken, Request request) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOGOUT);
		socketMessage.setAccesToken(accessToken);
		socketMessage.setVerifier(calculateVerifier(request));

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public String generateAccountId(String username, String fullname, String email) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_GENERATE_ACCOUNT_ID);
		socketMessage.setUsername(username);
		socketMessage.setFullname(fullname);
		socketMessage.setEmail(email);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		return response.getResponse();
	}

	public boolean existsAccount(String accessToken, HttpServletRequest request) throws Exception {
		return existsAccount(accessToken, new HttpRequest(request));
	}

	public boolean existsAccount(String accessToken, Request request) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(EXISTS_ACCOUNT);
		socketMessage.setAccesToken(accessToken);
		socketMessage.setVerifier(calculateVerifier(request));

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public FederationAccount getAccount(String accessToken, HttpServletRequest request) throws Exception {
		return getAccount(accessToken, new HttpRequest(request));
	}

	public FederationAccount getAccount(String accessToken, Request request) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_ACCOUNT);
		socketMessage.setAccesToken(accessToken);
		socketMessage.setVerifier(calculateVerifier(request));

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		Serializer serializer = new Persister();
		FederationAccount fsAccount = serializer.read(FederationAccount.class, response.getResponse(), false);
		return fsAccount;
	}

	public FederationInfo getInfo() throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_GET_INFO);
		SocketResponseMessage response = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		FederationInfo federationInfo = serializer.read(FederationInfo.class, response.getResponse(), false);

		return federationInfo;
	}

	public UserList searchUsers(String condition, int startPos, int limit) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_SEARCH_USERS);
		socketMessage.setCondition(condition);
		socketMessage.setStartPos(startPos);
		socketMessage.setLimit(limit);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		UserList userList = serializer.read(UserList.class, response.getResponse(), false);

		return userList;
	}

	public FederationUnitList loadMembers() throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOAD_MEMBERS);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		FederationUnitList federationUnitList = serializer.read(FederationUnitList.class, response.getResponse(), false);

		return federationUnitList;
	}

	public FederationUnitList loadPartners() throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOAD_PARTNERS);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		FederationUnitList federationUnitList = serializer.read(FederationUnitList.class, response.getResponse(), false);

		return federationUnitList;
	}

	public boolean existsPartner(String partnerId) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_EXISTS_PARTNER);
		socketMessage.setPartnerId(partnerId);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public FederationUnit loadPartner(String partnerId) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOAD_PARTNER);
		socketMessage.setPartnerId(partnerId);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		FederationUnit federationUnit = serializer.read(FederationUnit.class, response.getResponse(), false);

		return federationUnit;
	}

	public FederationUnit locatePartner(String partnerName) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOCATE_PARTNER);
		socketMessage.setPartnerName(partnerName);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		FederationUnit federationUnit = serializer.read(FederationUnit.class, response.getResponse(), false);

		return federationUnit;
	}

	public FederationAccountResponse validateRequest(String signature, String hash) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_VALIDATE_REQUEST);
		socketMessage.setVerifierSign(signature, hash);

		try {
			SocketResponseMessage responseMessage = this.conn.execute(socketMessage, true);
			Serializer serializer = new Persister();
			FederationAccount fsAccount = serializer.read(FederationAccount.class, responseMessage.getResponse(), false);
			FederationAccountResponse resp = new FederationAccountResponse(fsAccount);
			return resp;
		} catch (Exception e) {
			FederationAccountResponse resp = new FederationAccountResponse(null);
			resp.setError(e.getMessage());
			return resp;
		}

	}

	public Certificate certificateSigningRequest(PublicKey publickey, String name) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_SIGN_CERTIFICATE);
		String publicKey = new String(Base64.encodeBase64(publickey.getEncoded()), Charset.forName("UTF-8"));
		SignCertificate signCertificate = new SignCertificate();
		signCertificate.setEmail(new SimpleElement(name));
		signCertificate.setPublickey(new SimpleElement(publicKey));
		socketMessage.setSignCertificate(signCertificate);

		SocketResponseMessage responseMessage = this.conn.execute(socketMessage, true);
		String message = responseMessage.getResponse();

		ByteArrayInputStream certBytes = new ByteArrayInputStream(message.getBytes());
		return CertificateFactory.getInstance("X509").generateCertificate(certBytes);
	}

	public DeviceList getUserMobileDevices(String userId) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOAD_USER_MOBILE_DEVICES);
		socketMessage.setId(userId);

		SocketResponseMessage responseMessage = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		DeviceList deviceList = serializer.read(DeviceList.class, responseMessage.getResponse(), false);

		return deviceList;
	}

	public DeviceList getAllMobileDevices() throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_LOAD_MOBILE_DEVICES);
		SocketResponseMessage responseMessage = this.conn.execute(socketMessage, true);

		Serializer serializer = new Persister();
		DeviceList deviceList = serializer.read(DeviceList.class, responseMessage.getResponse(), false);

		return deviceList;
	}

	public boolean registerMobileDevice(String userId, String deviceId) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_REGISTER_MOBILE_DEVICE);
		socketMessage.setId(userId);
		socketMessage.setDeviceId(deviceId);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public boolean unRegisterMobileDevice(String userId, String deviceId) throws Exception {
		this.init();

		SocketMessageModel socketMessage = createHeaderMessage(REQUEST_UNREGISTER_MOBILE_DEVICE);
		socketMessage.setId(userId);
		socketMessage.setDeviceId(deviceId);

		SocketResponseMessage response = this.conn.execute(socketMessage, true);
		return response.getResponse().equals(SocketResponseMessage.RESPONSE_OK);
	}

	public void close() throws Exception {
		if (this.conn != null) {
			if (!this.conn.isOpen())
				return;
			SocketMessageModel socketMessage = createHeaderMessage(REQUEST_TERMINATE_CONNECTION);
			this.conn.execute(socketMessage, false);
			this.conn.close();
		}
	}

	private String calculateVerifier(HttpServletRequest request) {
		return calculateVerifier(new HttpRequest(request));
	}

	private String calculateVerifier(Request request) {
		String remoteAddr = Utils.getAddr(request);
		String remoteUA = request.getHeader("User-Agent").toLowerCase();
		String verifier = remoteAddr + remoteUA;

		MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(verifier.getBytes());
			byte[] hash = digest.digest();
			verifier = Base64.encodeBase64String(hash);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return verifier;
	}

	private void connect() throws Exception {
		try {
			Socket socket = new Socket(host, port);
			socket.setSoTimeout(30000);
			this.conn = new ConnectionClientHandler(socket);
		}
		catch (Exception exception) {
			System.out.print(String.format("ERROR: Could not connect to federation socket: %s %s", host, port));
			throw exception;
		}
	}

}
