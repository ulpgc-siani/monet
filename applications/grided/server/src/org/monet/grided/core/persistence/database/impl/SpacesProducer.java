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
import org.monet.grided.core.model.MetaModelVersion;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.model.State;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.persistence.database.constants.Queries;
import org.monet.grided.core.util.StatementUtil;
import org.monet.grided.exceptions.DataException;

import com.google.inject.Inject;

public class SpacesProducer {
    
    private QueriesStore queriesStore;
    private DataSource dataSource;

    @Inject
    public SpacesProducer(QueriesStore queriesStore, DataSource dataSource) {
        this.queriesStore = queriesStore;
        this.dataSource = dataSource;
    }    
    
    public Space loadSpace(long id) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        Space space = null;
        
        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SPACE));
            statement.setLong(Params.ID, id);
            rs = statement.executeQuery();
            space = populateSpace(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SPACE, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return space;
    }    
    
    
    public List<Space> loadServerSpaces(long serverId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        List<Space> spaces = null;

        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SERVER_SPACES));
            statement.setLong(Params.SERVER_ID, serverId) ;         
            rs = statement.executeQuery();
            spaces = populateSpaces(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SPACES, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return spaces;            
    }
    
    
    public List<Space> loadSpacesWithModel(long modelId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        List<Space> spaces = null;

        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SPACES_WITH_MODEL));
            statement.setLong(Params.MODEL_ID, modelId) ;         
            rs = statement.executeQuery();
            spaces = populateSpacesWithParents(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SPACES, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return spaces;            
    }
    
    public List<Space> loadSpacesByModelVersion(long modelVersionId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        List<Space> spaces = null;

        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_SPACES_BY_MODELVERSION));
            statement.setLong(Params.MODEL_VERSION_ID, modelVersionId) ;         
            rs = statement.executeQuery();
            spaces = populateSpacesWithParents(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SPACES, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return spaces;                    
    }
    
    public List<Space> loadFederationSpaces(long federationId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        List<Space> spaces = null;

        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_FEDERATION_SPACES));
            statement.setLong(Params.FEDERATION_ID, federationId);
            rs = statement.executeQuery();
            spaces = populateSpaces(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_SPACES, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return spaces;    
    }
            
    public void saveSpace(Space space) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;

        try {
            connection = this.dataSource.getConnection();            
            if (space.getId() == -1) {
                
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.INSERT_SPACE), Statement.RETURN_GENERATED_KEYS);
                statement.setString(Params.NAME, space.getName());
                statement.setInt(Params.TYPE, ComponentTypes.SPACE.ordinal());
                statement.setLong(Params.FEDERATION_ID, space.getFederation().getId());
                statement.setInt(Params.STATE, (space.getState().isRunning())? 1 : 0);
                statement.setLong(Params.RUNNING_TIME, space.getState().getRunningFrom());
                
                rs = statement.executeUpdateAndGetGeneratedKeys();                       
                if(rs != null && rs.next()) {
                    long id = rs.getLong(1);
                    space.setId(id);
                }                                
            }
            else {
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.UPDATE_SPACE));
                statement.setLong(Params.ID, space.getId());
                statement.setString(Params.NAME, space.getName());   
                statement.setInt(Params.STATE, (space.getState().isRunning())? 1 : 0);
                statement.setLong(Params.RUNNING_TIME, space.getState().getRunningFrom());
                                
                int rows = statement.executeUpdate();
                if (rows == 0) throw new DataException(ErrorCode.SAVE_SPACE, null);                
            }                       
        } catch (Exception ex) {
            throw new DataException(ErrorCode.SAVE_SPACE, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                        
    }
                    
    public void deleteSpace(long id) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;

        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.DELETE_SPACE));
            statement.setLong(Params.ID, id);                         
            statement.executeUpdate();            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.DELETE_SPACE, ex);   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                
    }
    

    private Space populateSpace(ResultSet rs) throws SQLException {
        Space space = null;
        
        if (rs.next()) {
          Server server = new Server(rs.getLong(Params.SERVER_ID), rs.getString(Params.SERVER_NAME), rs.getString(Params.SERVER_IP));
          Federation federation = new Federation(server, rs.getLong(Params.FEDERATION_ID));
          federation.setName(rs.getString(Params.FEDERATION_NAME));
          
                    
          space = new Space(federation, rs.getLong(Params.ID));
                    
          long modelVersionId = rs.getLong(Params.MODEL_VERSION_ID);          
          if (modelVersionId != 0) {
            MetaModelVersion metaModelVersion = new MetaModelVersion(rs.getString(Params.METAMODEL_VERSION));
            ModelVersion modelVersion = new ModelVersion(rs.getLong(Params.MODEL_VERSION_ID), rs.getString(Params.MODEL_VERSION_LABEL), metaModelVersion);
            space.setModelVersion(modelVersion);
          }
          
          space.setName(rs.getString(Params.NAME));             
          State state = (rs.getInt(Params.STATE) == 1)? State.running(rs.getLong(Params.RUNNING_TIME)) : State.stopped();
          space.setState(state);               
        }
        
        return space;
    }
    
    private List<Space> populateSpaces(ResultSet rs) throws SQLException {
        List<Space> spaces = new ArrayList<Space>();
        while (rs.next()) {
            Space space = new Space(rs.getLong(Params.ID));
            space.setName(rs.getString(Params.NAME));
            State state = (rs.getInt(Params.STATE) == 1)? State.running(rs.getLong(Params.RUNNING_TIME)) : State.stopped();
            space.setState(state);               
            spaces.add(space);
        }
        return spaces;        
    }
    
    private List<Space> populateSpacesWithParents(ResultSet rs) throws SQLException {
        List<Space> spaces = new ArrayList<Space>();
        
        while (rs.next()) {
            Server server = new Server(rs.getLong(Params.SERVER_ID), rs.getString(Params.SERVER_NAME), rs.getString(Params.SERVER_IP));
            Federation federation = new Federation(server, rs.getLong(Params.FEDERATION_ID));
            federation.setName(rs.getString(Params.FEDERATION_NAME));
                        
            Space space = new Space(federation, rs.getLong(Params.ID));

            long modelVersionId = rs.getLong(Params.MODEL_VERSION_ID);          
            if (modelVersionId != 0) {
                MetaModelVersion metaModelVersion = new MetaModelVersion(rs.getString(Params.METAMODEL_VERSION));
                ModelVersion modelVersion = new ModelVersion(rs.getLong(Params.MODEL_VERSION_ID), rs.getString(Params.MODEL_VERSION_LABEL), metaModelVersion);
                space.setModelVersion(modelVersion);
              }
            
            space.setName(rs.getString(Params.NAME));
            State state = (rs.getInt(Params.STATE) == 1)? State.running(rs.getLong(Params.RUNNING_TIME)) : State.stopped();
            space.setState(state);            
            spaces.add(space);
        }
        return spaces;        
    }
}