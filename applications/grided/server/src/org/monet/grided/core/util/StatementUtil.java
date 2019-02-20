package org.monet.grided.core.util;

import java.sql.Connection;
import java.sql.ResultSet;

import org.monet.grided.core.persistence.database.impl.NamedParameterStatement;
import org.monet.grided.exceptions.SystemException;

public class StatementUtil {

    public static void close(Connection connection) {
        try{
            if (connection != null) connection.close();          
        }
        catch(Exception ex) {
            throw new SystemException(ex);
        }
    }        

    public static void close(NamedParameterStatement statement) {
        try{
            if (statement != null) statement.close();          
        }
        catch(Exception ex) {
            throw new SystemException(ex);
        }
    }   
    
    public static void close(ResultSet rs) {
        try{
            if (rs != null) rs.close();          
        }
        catch(Exception ex) {
            throw new SystemException(ex);
        }        
    }
}

