package org.monet.space.kernel.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseConfiguration {

    private static DatabaseConfiguration instance;
    private Map<String, String> map;

    public static final String JDBC_TYPE = "Jdbc.Type";
    public static final String JDBC_DATASOURCE = "Jdbc.DataSource";
    public static final String DATABASE_URL = "DATABASE_URL";
    public static final String DATABASE_USER = "DATABSE_USER";
    public static final String DATABASE_PASSWORD = "DATABSE_PASSWORD";

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
}