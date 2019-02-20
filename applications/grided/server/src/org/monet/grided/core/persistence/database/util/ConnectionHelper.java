package org.monet.grided.core.persistence.database.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.monet.grided.exceptions.SystemException;

public class ConnectionHelper {

    public void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (connection != null) connection.close();
            if (statement  != null) statement.close();
            if (resultSet  != null) resultSet.close();
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }
}

