package org.monet.utils.oclient.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
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

	private String url;
	private String dbname;
	private String host;
	private String port;
	private String dbType;
  private OracleDataSource ods;

	private static Map<String, Connection> pool = new HashMap<String, Connection>();

	public Db() throws SQLException, UnknownHostException {
		this.url = "";
		this.dbname = "";
		this.host = "";
		this.port = "";


	}

  public Db(String url) throws SQLException, UnknownHostException {
    this();
    this.url = url;
    this.dbname = getDbNameFromUrl(url);
    dbType = getDbTypeFromUrl(url);

    if (dbType.equals("oracle")) {
      ods = new OracleDataSource();
      ods.setURL(url);
    }
  }

	public Db(String url, String osuser, String localMachine, String program) throws SQLException, UnknownHostException {
		this(url);

    if ("".equals(osuser)) osuser = System.getProperty("user.name").toString();
    if ("".equals(program)) program = "JDBC Thin Client";
    if ("".equals(localMachine)) localMachine = InetAddress.getLocalHost().getCanonicalHostName();

    if (dbType.equals("oracle")) {
      java.util.Properties props = new java.util.Properties();
      props.put("v$session.osuser", osuser);
      props.put("v$session.machine", localMachine);
      props.put("v$session.program", program);
      ods.setConnectionProperties(props);

      System.out.println("osuser: " + osuser);
      System.out.println("machine: " + localMachine);
      System.out.println("program: " + program);
    }
  }

//	public Db(String url, String user, String password) {
//		this();
//		this.url = url;
//		this.dbname = getDbNameFromUrl(url);
//		this.user = user.replaceAll("#dbname#", this.dbname);
//		this.password = password.replaceAll("#dbname#", this.dbname);
//		dbType = getDbTypeFromUrl(url);
//		String pattern_string = "Not database compatible.";
//
//		if (dbType.equals("oracle")) {
//				pattern_string = ".*:.*:.*:.*/.*@(.*):(.*):.*";
//				Pattern pattern = Pattern.compile(pattern_string);
//				Matcher matcher = pattern.matcher(url);
//
//				if (matcher.find()) {
//					host = matcher.group(1);
//					port = matcher.group(2);
//				}
//			}
//	}

//	public Db(String url, String user, String password, String dbname) {
//		this(url, user, password);
//		this.dbname = dbname;
//
//		if (this.dbname.equals(""))
//			this.dbname = getDbNameFromUrl(url);
//
//		this.user = user.replaceAll("#dbname#", this.dbname);
//		this.password = password.replaceAll("#dbname#", this.dbname);
//	}

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

      try (Connection connection = ods.getConnection()) {
        ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);

        Reader reader = new FileReader(fileName);
        scriptRunner.runScript(reader);
      }
		} else {
			System.err.println("Database not support.");
		}
	}

	public String executeSentence(String sentence) throws SQLException, IOException {
		String result = "";
		if (dbType.equals("oracle")) {
//			OracleDataSource ods = new OracleDataSource();
//			ods.setURL(url);
      sentence = sentence.replaceAll("`", "").replaceAll("\"", "'");

			ResultSet rs = null;
      try (Connection connection = ods.getConnection()) {
        rs = connection.createStatement().executeQuery(sentence);

        String sentence_op = sentence.split(" ")[0];
        if ((!sentence_op.equals("INSERT")) && (!sentence_op.equals("DELETE"))) {
          try {
            rs.next();


            Object value = rs.getObject(1);
            result = value.toString();
          } catch (Exception e) {
            System.out.println("SQL: " + sentence + ".\nSQLException: " + e.getMessage());
          }
        }
      } finally {
        if (rs != null)
          rs.close();

      }
		} else {
			System.err.println("Database not support.");
		}
		return result.replaceAll("\n", "");
	}

	public String executeSentenceXML(String sentence) throws SQLException, IOException {
		String result = "";

		if (isServerUp(host, Integer.parseInt(port))) {
			if (dbType.equals("oracle")) {

//				OracleDataSource ods = new OracleDataSource();
//				ods.setURL(url);
        sentence = sentence.replaceAll("`", "").replaceAll("\"", "'");

				ResultSet rs = null;
        try (Connection connection = ods.getConnection()) {
          rs = connection.createStatement().executeQuery(sentence);

          result = getXML(rs);

        } finally {
          if (rs != null)
            rs.close();

        }

			} else {
				System.err.println("Database not support.");
			}
		}

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
			Element results = doc.createElement("results");
			doc.appendChild(results);

			while (rs.next()) {
				Element row = doc.createElement("row");
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
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.setOutputProperty("omit-xml-declaration","yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
			result = sw.toString();
		} catch (SQLException | ParserConfigurationException | TransformerException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			pool.remove(dbname);
		}

		return result;
	}

	private boolean isServerUp(String host, int port) {
		boolean isUp = false;
    if ("".equals(host) && "".equals(port))
      isUp = true;
    else {
      try {
        Socket socket = new Socket(host, port);
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
