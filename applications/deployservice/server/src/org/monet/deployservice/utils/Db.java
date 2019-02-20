package org.monet.deployservice.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Db {

	private Logger logger;

	private String url;
	private String user;
	private String password;
	private String dbname;
	private String host;
	private String port;
	private String dbType;

//	private static Map<String, Connection> pool = new HashMap<String, Connection>();

	private String getPath() {
		String path = "";
		try {
			path = new java.io.File(".").getCanonicalPath();
		} catch (Exception exception) {
			logger.error("Unable to read current path.");
		}
		return path;
	}

	public Db() {
		logger = Logger.getLogger(this.getClass());

		this.url = "";
		this.user = "";
		this.password = "";
		this.dbname = "";
		this.host = "";
		this.port = "";
	}

	public Db(String url, String user, String password) {
		this();
		this.url = url;
		this.dbname = getDbNameFromUrl(url);
		this.user = user.replaceAll("#dbname#", this.dbname);
		this.password = password.replaceAll("#dbname#", this.dbname);
		dbType = getDbTypeFromUrl(url);
		String pattern_string = "Not database compatible.";

		if (dbType.equals("mysql")) {
      try {
        Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

			pattern_string = ".*:.*://(.*):(.*)/.*";
			Pattern pattern = Pattern.compile(pattern_string);
			Matcher matcher = pattern.matcher(url);

			if (matcher.find()) {
				host = matcher.group(1);
				if (host.equals("localhost"))
					host = "127.0.0.1";
				port = matcher.group(2);
			}

			if (host.equals("") || port.equals("")) {
				pattern_string = ".*:.*://(.*)/.*";
				pattern = Pattern.compile(pattern_string);
				matcher = pattern.matcher(url);

				port = "3306";
				if (matcher.find()) {
					host = matcher.group(1);
					if (host.equals("localhost"))
						host = "127.0.0.1";
				}
			}
		} else {
			if (dbType.equals("oracle")) {
				pattern_string = ".*:.*:.*:.*/.*@(.*):(.*):.*";
				Pattern pattern = Pattern.compile(pattern_string);
				Matcher matcher = pattern.matcher(url);

				if (matcher.find()) {
					host = matcher.group(1);
					port = matcher.group(2);
				}
			}
		}

/*		if (host.equals("") || port.equals("")) {
			String error = "Failed to load database host or port (Pattern: " + pattern_string + ", Db url: " + url + ").";
			logger.warn(error);
//			throw new RuntimeException(error);
		}*/
	}

	public Db(String url, String user, String password, String dbname) {
		this(url, user, password);
		this.dbname = dbname;

		if (this.dbname.equals(""))
			this.dbname = getDbNameFromUrl(url);

		user = user.replaceAll("#dbname#", this.dbname);
		password = password.replaceAll("#dbname#", this.dbname);
	}

	public String getDbNameFromUrl(String url) {
		Pattern pattern = Pattern.compile(".*:.*://.*/(.*)\\?.*");
		if (getDbTypeFromUrl(url).equals("oracle"))
			pattern = Pattern.compile(".*:.*:.*:(.*)/.*");

		Matcher matcher = pattern.matcher(url);

		String dbName = "";
		if (matcher.find()) {
			dbName = matcher.group(1);
		}

		return dbName;
	}

	public String getDbTypeFromUrl(String url) {
		return url.toLowerCase().split(":")[1];
	}

	public void executeScript(String fileName) throws SQLException, IOException {
		if (dbType.equals("oracle")) {
			OracleDataSource ods = new OracleDataSource();
			ods.setUser(user);
			ods.setPassword(password);
			ods.setURL(url);
			Connection connection = ods.getConnection();

			try {
				ScriptRunner scriptRunner = new ScriptRunner(connection, true, false);

				Reader reader = new FileReader(fileName);
				scriptRunner.runScript(reader);
			} finally {
				connection.close();
			}
		} else {
			if (dbType.equals("mysql")) {
				String command = "";
				String passwordText = "";
				if (!password.equals(""))
					passwordText = " --password=\"" + password + "\" ";

				logger.info("Deploy mysql file: " + fileName);

				command = "mysql --default-character-set=utf8 --host=" + host + " --port=" + port + " --database=" + dbname + " --user=" + user + passwordText + " < " + fileName + " 2>&1";
				logger.info(command);

				Shell shell = new Shell();
				if (shell.executeCommand(command, new File(new File(fileName).getParent())) > 0) {
					throw new RuntimeException("Error exec database script. Info: " + shell.lastInfo());
				}
			}
		}
	}

	public String executeSentence(String sentence) throws SQLException, IOException {
		String result = "";
		if (dbType.equals("oracle")) {
			OracleDataSource ods = new OracleDataSource();
			ods.setUser(user);
			ods.setPassword(password);
			ods.setURL(url);
			Connection connection = ods.getConnection();
			sentence = sentence.replaceAll("`", "").replaceAll("\"", "'");

			ResultSet rs = null;
			try {
				rs = connection.createStatement().executeQuery(sentence);

				String sentence_op = sentence.split(" ")[0];
				if ((!sentence_op.equals("INSERT")) && (!sentence_op.equals("DELETE"))) {
					try {
						rs.next();
						Object value = rs.getObject(1);
						result = value.toString();
					} catch (Exception e) {
						logger.warn("SQL: " + sentence + ". SQLException: " + e.getMessage());
					}
				}
			} finally {
				if (rs != null)
					rs.close();
				connection.close();
			}
		} else {
			if (dbType.equals("mysql")) {
				String command = "";
				String passwordText = "";
				if (!password.equals(""))
					passwordText = " --password=\"" + password + "\" ";

				sentence = "'" + sentence + "'";
				if (SystemOS.isWindows()) {
					sentence = sentence.replace("\"", "&quot;");
					sentence = sentence.replace("'", "\"");
					sentence = sentence.replace("&quot;", "'");
				}

				command = "mysql --default-character-set=utf8 --silent --host=" + host + " --port=" + port + " --database=" + dbname + " --user=" + user + passwordText + " --execute=" + sentence;

				Shell shell = new Shell();
				result = shell.executeCommandWithResponse(command, new File(getPath()));

			}
		}
		return result.replaceAll("\n", "");
	}

	public String executeSentenceXML(String sentence) throws SQLException, IOException {
		String result = "";

		if (isServerUp(host, port)) {
			logger.debug("Start execute sentence in '" + dbname + "': " + sentence);
			if (dbType.equals("oracle")) {

				OracleDataSource ods = new OracleDataSource();
				ods.setUser(user);
				ods.setPassword(password);
				ods.setURL(url);
				Connection connection = ods.getConnection();
				sentence = sentence.replaceAll("`", "").replaceAll("\"", "'");

				ResultSet rs = null;
				try {
					rs = connection.createStatement().executeQuery(sentence);

					result = getXML(rs);

				} finally {
					if (rs != null)
						rs.close();
					connection.close();
				}

			} else {
				if (dbType.equals("mysql")) {
          Connection connection = null;
					ResultSet rs = null;
					try {
//						conn = pool.get(dbname);
//						if (conn == null) {
							connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbname + "?user=" + user + "&password=" + password);
//							logger.info("Added new connection to pool: " + dbname);
//							pool.put(dbname, conn);
//						}

						rs = connection.createStatement().executeQuery(sentence);

						result = getXML(rs);
					} catch (Exception e) {
            logger.error("I can't connect to mysql database: " + e.getMessage());
          }	finally {
						if (rs != null)
							rs.close();
						connection.close();
					}
				}
			}
			logger.debug("Finish execute in '" + dbname + "': " + sentence);
		} else
			logger.debug("Server not connected.");

		return result;
	}

	private String getXML(ResultSet rs) {
		String result = "";
		try {

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element results = doc.createElement("Results");
			doc.appendChild(results);

			while (rs.next()) {
				Element row = doc.createElement("Row");
				results.appendChild(row);
				for (int i = 1; i <= colCount; i++) {
					String columnName = rsmd.getColumnName(i).toLowerCase();
					Object value = rs.getObject(i);
					if (value == null)
						value = "";
					if (value instanceof oracle.sql.TIMESTAMP)
						value = ((oracle.sql.TIMESTAMP) value).timestampValue();

					Element node = doc.createElement(columnName);
					if (value instanceof Timestamp) {
						Timestamp date_mysql = (Timestamp) value;

						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						String date_string = dateFormat.format(date_mysql);

						node.appendChild(doc.createTextNode(date_string));

					} else
						node.appendChild(doc.createTextNode(value.toString()));

					row.appendChild(node);
				}
			}
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
			result = sw.toString();
		} catch (SQLException | ParserConfigurationException | TransformerException ex) {
			logger.error("SQLException: " + ex.getMessage());
//			pool.remove(dbname);
		}

		return result;
	}

	private boolean isServerUp(String host, String port) {
		boolean isUp = false;
    if ("".equals(host) && "".equals(port))
      isUp = true;
    else {
      try {
        Socket socket = new Socket(host, Integer.parseInt(port));
        // Server is up
        isUp = true;
        socket.close();
      } catch (IOException e) {
        // Server is down
      }
    }
		return isUp;
	}
}
