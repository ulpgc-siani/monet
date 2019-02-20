package org.monet.deployservice.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Md5;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.OperationIDs;
import org.monet.deployservice_engine.control.commands.ResultIDs;
import org.monet.deployservice_engine.control.engine.Engine;
import org.monet.deployservice_engine.xml.CommandXML;
import org.monet.deployservice_engine.xml.ResultXML;
import org.monet.deployservice_engine.xml.ServersXML;

public class ServiceHTTPS {
	private Logger logger;
	private Engine engine;
	private SSLServerSocket clientSocket = null;
	private SSLSocket currentSocket = null;

	public ServiceHTTPS(Engine engine) {
		logger = Logger.getLogger(this.getClass());
		this.engine = engine;
	}

	public void start() {
		openSocket();
		showServerInfo();

		// Listening to the port
		try {
			while (true) {			
				currentSocket = (SSLSocket) clientSocket.accept();

				try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(currentSocket.getOutputStream())); BufferedReader r = new BufferedReader(new InputStreamReader(currentSocket.getInputStream()))) {
					String m = r.readLine();
					if (m != null) {
						Pattern pattern = Pattern.compile("GET /(.*) HTTP/1\\..");
						Matcher matcher = pattern.matcher(m);
						String receiveText = "";
						if (matcher.find()) {
							receiveText = matcher.group(1);
						}
						String result = execute(URLDecoder.decode(receiveText, "UTF-8"));
						Integer result_length = result.length();

						w.write("HTTP/1.0 200 OK");
						w.newLine();
						w.write("Content-Type: text/plain");
						w.newLine();
						w.write("Content-Length: " + result_length);
						w.newLine();
						w.newLine();

						w.write(result);
						w.flush();
					}
				} catch (Exception e) {
					String error = "The request failed. Message: " + e.getMessage();
					logger.debug(error);
				} finally {
					currentSocket.close();
				}
				openSocket();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void showServerInfo() {
		ServersXML serversXML = new ServersXML();
		serversXML.unserialize(Configuration.getServersConfig());

		if (! "".equals(Configuration.getValue(Configuration.VAR_StartCommandToBegin))) {
			try {
				CommandXML commandXMLStart = new CommandXML();
				Item command = commandXMLStart.unserialize(Configuration.getValue(Configuration.VAR_StartCommandToBegin));
				engine.getItem(command);
			} catch (Exception e) {
				logger.error("Could not start begin command. Details: " + e.getMessage(), e);
			}
		}
	}

	public String execute(String receiveText) throws IOException {
		String address = currentSocket.getRemoteSocketAddress().toString();
		address = address.replace("/", "").split(":")[0];

		if (!receiveText.contains("<command id=\"get_servers\">")) {
			logger.info("["+address+"] Receive command: " + receiveText);
		} else {
			logger.debug("["+address+"] Receive command: " + receiveText);
		}

		Item result = new Item();
		result.setProperty("id", ResultIDs.InvalidCommand);

		ResultXML resultXML = new ResultXML();
		CommandXML commandXML = new CommandXML();
		Item command = commandXML.unserialize(receiveText);

		Boolean execute = false;
		if (! "".equals(Configuration.getValue(Configuration.VAR_User))) {
			String remotePassword;
			try {
				Md5 md5 = Md5.getInstance();
				remotePassword = md5.hashData(command.getItem("password").getContent().getBytes(StandardCharsets.UTF_8));
			} catch (Exception e) {
				remotePassword = "";
			}

			if ((command.getItem("user") == null) || (command.getItem("password") == null) || (!command.getItem("user").getContent().equals(Configuration.getValue(Configuration.VAR_User))) || (!remotePassword.equals(Configuration.getValue(Configuration.VAR_Password)))) {
				result.setProperty("id", ResultIDs.InvalidUserOrPassword);
			} else
				execute = true;

		} else
			execute = true;

		if (execute) {
			clientSocket.close();
			clientSocket = null;
			
			if (!command.getProperty("id").equals(OperationIDs.GetServers)) {
				logger.info("Execute command " + command.getProperty("id"));
			} else {
				logger.debug("Execute command " + command.getProperty("id"));
			}

			try {
				result = engine.getItem(command);

				if (!command.getProperty("id").equals(OperationIDs.GetServers)) {
					logger.info("Finnish command " + receiveText);
				} else {
					logger.debug("Finnish command " + receiveText);
				}
			} catch (Exception e) {
				String error = "Error execute command.";
				ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
				logger.error(error + " Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e), e);
				result = null;
			}
		}
		return resultXML.serialize(result);
	}

	private void openSocket() {
		String ksName = Configuration.getValue(Configuration.VAR_HTTPS_KeyStoreFile);
		char ksPass[] = Configuration.getValue(Configuration.VAR_HTTPS_KeyStorePassword).toCharArray();
		char ctPass[] = Configuration.getValue(Configuration.VAR_HTTPS_KeyStorePassword).toCharArray();
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

			try {
				logger.debug("Loading keystore: " + ksName);
				ks.load(new FileInputStream(ksName), ksPass);
				kmf.init(ks, ctPass);
			} catch (IOException e) {
				String error = "Could not load keystore: " + ksName + ", Message: " + e.getMessage();
				logger.error(error, e);
				System.err.println(error);
				System.exit(1);
			}

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			SSLServerSocketFactory ssf = sc.getServerSocketFactory();

			Integer retry = 0;
			if (clientSocket != null) {
				clientSocket.close();
				clientSocket = null;
			}
			while ((clientSocket == null) && (retry < 3)) {
				try {
					logger.debug("Loading port: " + Configuration.getValue(Configuration.VAR_Port));
					clientSocket = (SSLServerSocket) ssf.createServerSocket(Integer.parseInt(Configuration.getValue(Configuration.VAR_Port)));
				} catch (IOException e) {
					String error = "Could not listen on specified port. Retry " + retry + ". Message: " + e.getMessage();
					logger.error(error);
					System.err.println(error);
				}
				retry++;
				if ((clientSocket == null) && (retry < 3))
					Thread.sleep(1000);
			}
			if (clientSocket == null)
				System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
