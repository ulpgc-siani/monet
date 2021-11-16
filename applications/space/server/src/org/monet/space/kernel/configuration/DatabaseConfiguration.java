package org.monet.space.kernel.configuration;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.exceptions.SystemException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseConfiguration {

    private static DatabaseConfiguration instance;
    private Map<String, String> map;

    private static final String JDBC_TYPE = "Jdbc.Type";
    private static final String JDBC_DATASOURCE = "Jdbc.DataSource";
    private static final String JDBC_MAX_ACTIVE_CONNECTIONS = "Jdbc.MaxActiveConnections";
    private static final String JDBC_REMOVE_ABANDONED_TIMEOUT = "Jdbc.RemoveAbandonedTimeout";
    private static final String DATABASE_URL = "Jdbc.Url";
    private static final String DATABASE_USER = "Jdbc.User";
    private static final String DATABASE_PASSWORD = "Jdbc.Password";

    public static DatabaseConfiguration getInstance() {
        return instance;
    }

    public DatabaseConfiguration(Map<String, String> map) {
        this.map = map;
        instance = this;
    }

    public DatabaseConfiguration(String[] args) {
        this(toMap(args));
    }

    public DatabaseConfiguration(Properties properties) {
        this(toMap(properties));
    }

    public String url() {
        return map.get(DATABASE_URL);
    }

    public String user() {
        return map.get(DATABASE_USER);
    }

    public String password() {
        return map.get(DATABASE_PASSWORD);
    }

    public int maxActiveConnections() {
        return map.containsKey(JDBC_MAX_ACTIVE_CONNECTIONS) ? Integer.parseInt(map.get(JDBC_MAX_ACTIVE_CONNECTIONS)) : 15;
    }

    public int removeAbandonedTimeout() {
        return map.containsKey(JDBC_REMOVE_ABANDONED_TIMEOUT) ? Integer.parseInt(map.get(JDBC_REMOVE_ABANDONED_TIMEOUT)) : 300;
    }

    public String datasource() {
        return map.get(JDBC_DATASOURCE);
    }

    public DatabaseType type() {
        String value = map.get(JDBC_TYPE);
        for (DatabaseType type : DatabaseType.values()) {
            if (value.toLowerCase().contains(type.name().toLowerCase())) return type;
        }
        return DatabaseType.MYSQL;
    }

    private static Map<String, String> toMap(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        for (String arg : args) {
            if (!arg.contains("=")) continue;
            String key = arg.substring(0, arg.indexOf("="));
            String value = arg.substring(arg.indexOf("=") + 1);
            map.put(key, value);
        }
        return map;
    }

    private static Map<String, String> toMap(Properties properties) {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
    }

    public enum DatabaseType {
        MYSQL, ORACLE
    }

    public static DatabaseConfiguration fromMap(Map<String, String> map) {
    	return new DatabaseConfiguration(map);
	}

    public static DatabaseConfiguration fromXml(String sFilename) {
        InputStream isConfigFileContent;

        if (!AgentFilesystem.existFile(sFilename)) {
            throw new SystemException("Database configuration file not found", sFilename);
        }

        isConfigFileContent = AgentFilesystem.getInputStream(sFilename);

        Properties properties = new Properties();
        try {
            properties.loadFromXML(isConfigFileContent);
            isConfigFileContent.close();
            return new DatabaseConfiguration(properties);

        } catch (IOException exception) {
            AgentLogger.getInstance().error(new Exception("Exception in 'DatabaseConfiguration' (" + sFilename + ") : " + exception.getMessage(), exception));
        }
        return null;
    }
}