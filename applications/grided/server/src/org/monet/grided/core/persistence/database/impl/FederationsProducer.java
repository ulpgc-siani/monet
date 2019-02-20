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
import org.monet.grided.core.model.ComponentTypes;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.model.State;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.persistence.database.constants.Queries;
import org.monet.grided.core.util.StatementUtil;
import org.monet.grided.exceptions.DataException;

import com.google.inject.Inject;

public class FederationsProducer {

    private QueriesStore queriesStore;
    private DataSource dataSource;
    
    @Inject
    public FederationsProducer(QueriesStore queriesStore, DataSource dataSource) {
        this.queriesStore = queriesStore;
        this.dataSource = dataSource;    
    }
    
    public Federation loadFederation(long id) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        Federation federation = null;
        
        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_FEDERATION));            
            statement.setLong(Params.ID, id);       
                        
            rs = statement.executeQuery();
            federation = populateFederation(rs); 
            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_FEDERATION, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return federation;
    }
    
    
    public List<Federation> loadFederations(long serverId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        List<Federation> federations = null;
        
        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SERVER_FEDERATIONS));
            statement.setLong(Params.SERVER_ID, serverId);
            rs = statement.executeQuery();
            federations = populateFederations(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_FEDERATIONS, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return federations;
    }    
    
    public List<Federation> loadAllFederations() {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        List<Federation> federations = null;
        
        try {            
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_FEDERATIONS));            
            rs = statement.executeQuery();
            federations = populateFederations(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_FEDERATIONS, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return federations;
    }    
    
        
    public void saveFederation(Federation federation) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                
        try {
            connection = this.dataSource.getConnection();            
            if (federation.getId() == -1) {
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.INSERT_FEDERATION), Statement.RETURN_GENERATED_KEYS);
                statement.setString(Params.NAME, federation.getName());                
                statement.setInt(Params.TYPE, ComponentTypes.FEDERATION.ordinal());
                statement.setLong(Params.SERVER_ID, federation.getServer().getId());
                statement.setInt(Params.STATE, (federation.getState().isRunning())? 1 : 0);
                statement.setLong(Params.RUNNING_TIME, federation.getState().getRunningFrom());
                                
                rs = statement.executeUpdateAndGetGeneratedKeys();                       
                if(rs != null && rs.next()) {
                    long id = rs.getLong(1);
                    federation.setId(id);
                }
            }
            else {
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.UPDATE_FEDERATION));
                statement.setString(Params.NAME, federation.getName());                
                statement.setLong(Params.ID, federation.getId());
                statement.setLong(Params.SERVER_ID, federation.getServer().getId());
                statement.setInt(Params.STATE, (federation.getState().isRunning())? 1 : 0);
                statement.setLong(Params.RUNNING_TIME, federation.getState().getRunningFrom());
                
                int rows = statement.executeUpdate();
                if (rows == 0) throw new DataException(ErrorCode.SAVE_FEDERATION, null);
            }                                                                                   
            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.SAVE_FEDERATION, ex, statement.getQuery());   
        } finally {
            
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                
    }
    
    public void deleteFederation(long id) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.DELETE_FEDERATION));            
            statement.setLong(Params.ID, id);
                        
            statement.executeUpdate();                                         
        } catch (Exception ex) {
            throw new DataException(ErrorCode.DELETE_FEDERATION, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                
    }
    
    public void deleteFederations(long[] ids) {
        for (int i=0; i < ids.length; i++) {
            this.deleteFederation(ids[i]);
        }        
    }
        
    public Federation loadFederationOfSpace(long spaceId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        Federation federation = null;

        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_FEDERATION_OF_SPACE));
            statement.setLong(Params.SPACE_ID, spaceId);
            rs = statement.executeQuery();
            federation = populateFederation(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SPACES, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return federation;    
    }  

    
    private Federation populateFederation(ResultSet rs) throws SQLException {        
        Federation federation = null; 
        while (rs.next()) {
           if (rs.isFirst()) {
               Server server = new Server(rs.getLong(Params.SERVER_ID), rs.getString(Params.SERVER_NAME), rs.getString(Params.SERVER_IP));
               federation = new Federation(server, rs.getLong(Params.ID)); 
               federation.setName(rs.getString(Params.NAME));      
               State state = (rs.getInt(Params.STATE) == 1)? State.running(rs.getLong(Params.RUNNING_TIME)) : State.stopped();
               federation.setState(state);               
           } 
           
           Long spaceId = rs.getLong(Params.SPACE_ID);
           String spaceLabel = rs.getString(Params.SPACE_NAME); 
           
           if (spaceId == 0)  continue;
           
           Space space = new Space(federation, spaceId);
           space.setName(spaceLabel);        
           federation.add(space);                                
        }        
        return federation;
    }
    
    private List<Federation> populateFederations(ResultSet rs) throws SQLException {        
        List<Federation> federations = new ArrayList<Federation>();
        Server server = null;
        while (rs.next()) {
           if (rs.isFirst()) {
               server = new Server(rs.getLong(Params.SERVER_ID), rs.getString(Params.SERVER_NAME), rs.getString(Params.SERVER_IP));                            
           } 
           
           Federation federation = new Federation(server, rs.getLong(Params.ID)); 
           federation.setName(rs.getString(Params.NAME));
           State state = (rs.getInt(Params.STATE) == 1)? State.running(rs.getLong(Params.RUNNING_TIME)) : State.stopped();
           federation.setState(state);               
           federations.add(federation);                                 
        }        
        return federations;
    }
}
