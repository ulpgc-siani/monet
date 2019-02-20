package org.monet.grided.core.persistence.database.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.monet.grided.constants.ErrorCode;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.persistence.database.Transactional;
import org.monet.grided.core.persistence.database.constants.Queries;
import org.monet.grided.core.util.StatementUtil;
import org.monet.grided.exceptions.DataException;
import org.monet.grided.exceptions.SystemException;

import com.google.inject.Inject;

public class ServersProducer implements Transactional {
    
    private QueriesStore queriesStore;
    private DataSource dataSource;    
    private Connection transactionalConnection;

    @Inject
    public ServersProducer(QueriesStore queriesStore, DataSource dataSource) {
        this.queriesStore = queriesStore;
        this.dataSource = dataSource;        
    }
    
    public Server loadServer(long id) {        
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        Server server = null;
                        
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SERVER));
            statement.setLong(Params.ID, id);
            
            rs = statement.executeQuery();
            server = populateServer(rs);                
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SERVER, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return server;
    }    
    
    public List<Server> loadServers() {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        List<Server> servers = null;
        
        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SERVERS));          
            rs = statement.executeQuery();
            servers = populateServers(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SERVERS, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return servers;
    }
    
    public void saveServer(Server server) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                
        try {
            connection = this.dataSource.getConnection();  
                        
            if (server.getId() == -1) {
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.INSERT_SERVER), Statement.RETURN_GENERATED_KEYS);
                statement.setString(Params.NAME, server.getName());
                statement.setString(Params.IP, server.getIp());     
                statement.setInt(Params.ENABLED, (server.isEnabled() == true)? 1 : 0);                     
                
                rs = statement.executeUpdateAndGetGeneratedKeys();            
                if(rs != null && rs.next()) {
                  long id = rs.getLong(1);
                  server.setId(id);
                }
            }
            else {
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.UPDATE_SERVER));
                statement.setLong(Params.ID, server.getId());
                statement.setString(Params.NAME, server.getName());                
                statement.setString(Params.IP, server.getIp());
                statement.setInt(Params.ENABLED, (server.isEnabled() == true)? 1 : 0);
                
                int rows = statement.executeUpdate();
                if (rows == 0) throw new DataException(ErrorCode.UPDATE_EXCEPTION, null);
            }    
                        
        } catch (Exception ex) {
            throw new DataException(ErrorCode.SAVE_SERVER, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                
    }
    
    public void deleteServer(long serverId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.DELETE_SERVER));
            statement.setLong(Params.ID, serverId);                       
            int rows = statement.executeUpdate();
            if (rows == 0) throw new DataException(ErrorCode.DELETE_SERVER, null);
        } catch (Exception ex) {
            throw new DataException(ErrorCode.DELETE_SERVER, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                
    }
    
    public void deleteServers(long[] serverIds) {
        for (int i=0; i < serverIds.length; i++) {
            this.deleteServer(serverIds[i]);
        }
    }
    
    public long loadServerOfFederation(long federationId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        long serverId = -1;
                
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SERVER_ID_OF_FEDERATION));            
            statement.setLong(Params.FEDERATION_ID, federationId);                                   
            rs = statement.executeQuery();
            
            if (rs.next()) {                
                serverId = rs.getLong(Params.ID);
            }            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.DELETE_FEDERATION, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }     
        
        return serverId;
    }

        
    private List<Server> populateServers(ResultSet rs) throws SQLException {
        List<Server> servers = new ArrayList<Server>();
        
        while (rs.next()) {
            Server server = new Server(rs.getLong(Params.ID), rs.getString(Params.NAME), rs.getString(Params.IP));
            boolean state = (rs.getInt(Params.ENABLED) == 1)? true : false;
            server.setEnabled(state);
            servers.add(server);            
        }
        return servers;
    }
    
    private Server populateServer(ResultSet rs) throws SQLException {
        Server server = null;
        while (rs.next()) {
          if (rs.isFirst()) {
              server = new Server(rs.getLong(Params.ID), rs.getString(Params.NAME), rs.getString(Params.IP));
              boolean state = (rs.getInt(Params.ENABLED) == 1)? true : false;
              server.setEnabled(state);
          }
          Long federationId = rs.getLong(Params.FEDERATION_ID);
          if (federationId == 0) continue;
          
          Federation federation = new Federation(server, rs.getLong(Params.FEDERATION_ID));          
          federation.setName(rs.getString(Params.FEDERATION_NAME));
          server.add(federation);
        }
        return server;
    }

    @Override
    public void beginTransaction() {
        Connection con = null;
        try {        
            con = this.dataSource.getConnection();
            con.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new SystemException(ErrorCode.BEGIN_DATABASE_TRANSACTION, ex);
        }
        this.transactionalConnection = con;        
    }

    @Override
    public void commit()  {        
        try {
            this.transactionalConnection.commit();
            this.transactionalConnection.setAutoCommit(true);
        } catch (SQLException ex) {
            throw new SystemException(ErrorCode.DATABASE_COMMIT, ex);           
        }
    }
      

    @Override
    public void rollback()  {
        try {
            this.transactionalConnection.rollback();
            this.transactionalConnection.setAutoCommit(true);            
        } catch (SQLException ex) {
            throw new SystemException(ErrorCode.DATABASE_ROLLBACK, ex);
        }
    }
}

