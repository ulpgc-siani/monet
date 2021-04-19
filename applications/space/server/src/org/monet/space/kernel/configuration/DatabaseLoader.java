package org.monet.space.kernel.configuration;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.monet.space.kernel.agents.AgentLogger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.ConnectionPoolDataSource;
import java.sql.SQLException;

public class DatabaseLoader {

    public static void load(DatabaseConfiguration database) {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

            InitialContext initialContext = new InitialContext();
            initialContext.createSubcontext("java:");
            initialContext.createSubcontext("java:comp");
            initialContext.createSubcontext("java:comp/env");
            initialContext.createSubcontext("java:comp/env/jdbc");
            initialContext.bind("java:comp/env/" + database.datasource(), dataSource(database));
        } catch (NamingException | SQLException e) {
            AgentLogger.getInstance().error(e);
        }
    }

    private static ConnectionPoolDataSource dataSource(DatabaseConfiguration database) throws SQLException {
        switch (database.type()) {
            case MYSQL:
                return mysqlDataSource(database);
            case ORACLE:
                return oracleDataSource(database);
            default:
                return null;
        }
    }

    private static ConnectionPoolDataSource mysqlDataSource(DatabaseConfiguration database) {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        //dataSource.setRetainStatementAfterResultSetClose(true);
        dataSource.setURL(database.url());
        dataSource.setUser(database.user());
        dataSource.setPassword(database.password());
        return dataSource;
    }

    private static ConnectionPoolDataSource oracleDataSource(DatabaseConfiguration database) throws SQLException {
        OracleConnectionPoolDataSource dataSource = new OracleConnectionPoolDataSource();
        dataSource.setURL(database.url());
        dataSource.setUser(database.user());
        dataSource.setPassword(database.password());
        return dataSource;
    }
}
