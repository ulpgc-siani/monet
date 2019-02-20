package org.monet.grided.core.persistence.database.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.monet.grided.constants.ErrorCode;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.MetaModelVersion;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.persistence.database.constants.Queries;
import org.monet.grided.core.util.StatementUtil;
import org.monet.grided.exceptions.DataException;

import com.google.inject.Inject;

public class ModelsProducer {

    private QueriesStore queriesStore;
    private DataSource dataSource;

    @Inject
    public ModelsProducer(QueriesStore queriesStore, DataSource dataSource) {
        this.queriesStore = queriesStore;        
        this.dataSource = dataSource;        
    }
                    
    public Model loadModel(long id) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                        
        Model model = null;
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_MODEL));
            statement.setLong(Params.ID, id);
            
            rs = statement.executeQuery();
            model = populateModel(rs);                
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_MODEL, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return model;        
    }
        
    public List<Model> loadModels() {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs = null;
        List<Model> models = null;
        
        try {
            connection = this.dataSource.getConnection();
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_MODELS));          
            rs = statement.executeQuery();
            models = populateModels(rs);            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_MODELS, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return models;
    }
    
    public  void saveModel(Model model) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                
        try {
            connection = this.dataSource.getConnection();            
            if (model.getId() == -1) {
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.INSERT_MODEL), Statement.RETURN_GENERATED_KEYS);
                statement.setString(Params.NAME, model.getName());
                                                                
                rs = statement.executeUpdateAndGetGeneratedKeys();                       
                if(rs != null && rs.next()) {
                    long id = rs.getLong(1);
                    model.setId(id);
                }
            }
            else {
                statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.UPDATE_MODEL));
                statement.setLong(Params.ID, model.getId());
                statement.setString(Params.NAME, model.getName());                
                
                int rows = statement.executeUpdate();
                if (rows == 0) throw new DataException(ErrorCode.SAVE_MODEL, null);
            }                                                                                   
            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.SAVE_MODEL, ex, statement.getQuery());   
        } finally {            
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                        
    }
            
    public void deleteModel(long modelId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.DELETE_MODEL));
            statement.setLong(Params.ID, modelId);                       
            int rows = statement.executeUpdate();
            if (rows == 0) throw new DataException(ErrorCode.DELETE_MODEL, null);
        } catch (Exception ex) {
            throw new DataException(ErrorCode.DELETE_MODEL, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                
    }
    
    public ModelVersion loadModelVersion(long modelId, long id) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        ModelVersion modelVersion = null;
                        
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_MODEL_VERSION));
            statement.setLong(Params.ID, id);
            
            rs = statement.executeQuery();
            modelVersion = populateModelVersion(rs);                
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_MODEL_VERSION, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return modelVersion;                  
    }
    
    public List<ModelVersion> loadModelVersions(long modelId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        List<ModelVersion> modelVersions = new ArrayList<ModelVersion>();
                        
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_MODEL_VERSIONS));
            statement.setLong(Params.MODEL_ID, modelId);
            
            rs = statement.executeQuery();
            modelVersions = populateModelVersions(rs);                
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_MODEL_VERSIONS, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return modelVersions;                  
        
    }    
    
    public List<ModelVersion> loadModelVersions(long modelId, String metaModelVersion) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        List<ModelVersion> modelVersions = new ArrayList<ModelVersion>();
                        
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_MODEL_VERSIONS_BY_METAMODEL));
            statement.setLong(Params.MODEL_ID, modelId);
            statement.setString(Params.METAMODEL_VERSION, metaModelVersion);
            
            rs = statement.executeQuery();
            modelVersions = populateModelVersions(rs);                
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_MODEL_VERSIONS, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return modelVersions;                  
        
    }    
    
    public ModelVersion loadLatestModelVersion(long modelId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
        ModelVersion modelVersion = null;
                        
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.LOAD_LATEST_MODEL_VERSION));
            statement.setLong(Params.MODEL_ID, modelId);
            
            rs = statement.executeQuery();
            modelVersion = populateModelVersion(rs);                
        } catch (Exception ex) {
            throw new DataException(ErrorCode.LOAD_MODEL, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }        
        return modelVersion;                
    }
    
    public void saveModelVersion(long modelId, ModelVersion version) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;

        try {
            connection = this.dataSource.getConnection();                        
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.INSERT_MODEL_VERSION), Statement.RETURN_GENERATED_KEYS);
            
            statement.setLong(Params.MODEL_ID, modelId);
            statement.setString(Params.LABEL, version.getLabel());
            statement.setTimestamp(Params.DATE, new java.sql.Timestamp(version.getDate()));
            statement.setString(Params.METAMODEL_VERSION, version.getMetaModelVersion().getValue());
                                                            
            rs = statement.executeUpdateAndGetGeneratedKeys();                       
            if(rs != null && rs.next()) {
                long id = rs.getLong(1);
                version.setId(id);
            }
                        
        } catch (Exception ex) {
            throw new DataException(ErrorCode.SAVE_MODEL_VERSION, ex, statement.getQuery());   
        } finally {            
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                   
    }
    
    public  void deleteModelVersion(long modelId, long modelVersionId) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        ResultSet rs  = null;
                
        try {
            connection = this.dataSource.getConnection();            
            statement = new NamedParameterStatement(connection, this.queriesStore.get(Queries.DELETE_MODEL_VERSION));
            statement.setLong(Params.MODEL_ID, modelId);                                   
            statement.setLong(Params.ID, modelVersionId);                       
            
            int rows = statement.executeUpdate();
            if (rows == 0) throw new DataException(ErrorCode.DELETE_MODEL_VERSION, null);
            
        } catch (Exception ex) {
            throw new DataException(ErrorCode.DELETE_MODEL_VERSION, ex, statement.getQuery());   
        } finally {
            StatementUtil.close(rs);
            StatementUtil.close(statement);
            StatementUtil.close(connection);
        }                        
    }
    
    
    private Model populateModel(ResultSet rs) throws SQLException {
        Model model = null;
  
        while (rs.next()) {
            if (rs.isFirst()) {
                long id               = rs.getLong(Params.ID);
                String name           = rs.getString(Params.NAME);             
                model = new Model(id, name);            
            }
            
            long modelVersionId = rs.getLong(Params.MODEL_VERSION_ID);
            
            if (modelVersionId != 0) {
                String modelVersionLabel = rs.getString(Params.MODEL_VERSION_LABEL);        
                Timestamp modelVersionDate = rs.getTimestamp(Params.MODEL_VERSION_DATE);
                String sMetaModelVersion   = rs.getString(Params.METAMODEL_VERSION);
                MetaModelVersion metaModelVersion = new MetaModelVersion(sMetaModelVersion);

                ModelVersion version = new ModelVersion(modelVersionId, model, modelVersionLabel, modelVersionDate.getTime(), metaModelVersion);
                model.addVersion(version);
            }            
        }
        return model;
    }
    
    private List<Model> populateModels(ResultSet rs) throws SQLException {
        List<Model> models = new ArrayList<Model>();
        
        while (rs.next()) {                       
            long id     = rs.getLong(Params.ID);
            String name = rs.getString(Params.NAME);             
            
            Model model = new Model(id, name);            
            models.add(model);            
        }                
        return models;
    }    

    private ModelVersion populateModelVersion(ResultSet rs) throws SQLException {
        ModelVersion version = null;
        
        Model model = null;
        if (rs.first()) {            
            
            if (rs.isFirst()) {
              long modelId     = rs.getLong(Params.MODEL_ID);
              String modelName = rs.getString(Params.MODEL_NAME);
              model = new Model(modelId, modelName);
            }
            
            long id         = rs.getLong(Params.ID);
            String label    = rs.getString(Params.LABEL);
            Timestamp time  = rs.getTimestamp(Params.DATE);
            String sVersion = rs.getString(Params.METAMODEL_VERSION);
          
            version = new ModelVersion(id, model, label, time.getTime(), new MetaModelVersion(sVersion));
        }
        
        return version;
    }
    
    private List<ModelVersion> populateModelVersions(ResultSet rs) throws SQLException {
        List<ModelVersion> versions = new ArrayList<ModelVersion>();
        
        Model model = null;
        while (rs.next()) {
            
            if (rs.isFirst()) {
                long   modelId   = rs.getLong(Params.MODEL_ID);
                String modelName = rs.getString(Params.MODEL_NAME); 
                model = new Model(modelId, modelName);
            }
            
            long id         = rs.getLong(Params.ID);
            String label    = rs.getString(Params.LABEL);
            Timestamp time  = rs.getTimestamp(Params.DATE);
            String sVersion = rs.getString(Params.METAMODEL_VERSION);
          
            ModelVersion version = new ModelVersion(id, model, label, time.getTime(), new MetaModelVersion(sVersion));
            versions.add(version);
        }
        
        return versions;
    }    
}