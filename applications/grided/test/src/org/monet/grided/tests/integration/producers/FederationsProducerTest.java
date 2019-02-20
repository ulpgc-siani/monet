package org.monet.grided.tests.integration.producers;

import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.util.List;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.monet.grided.control.log.Logger;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.persistence.database.impl.FederationsProducer;
import org.monet.grided.core.persistence.database.impl.QueriesStoreImpl;
import org.monet.grided.core.persistence.database.impl.ServersProducer;
import org.monet.grided.core.util.Resources;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class FederationsProducerTest extends DatabaseTestCase {

    private static final String DATASET_FILE = "/database/dataset/federations.xml";
    
    private MysqlDataSource dataSource;
    private IDataSet dataSet;
    private FederationsProducer producer;

    private ServersProducer serversProducer;

    public void setUp() throws Exception {
        dataSource = new MysqlDataSource();
        dataSource.setDatabaseName("grided-test");                        
        dataSource.setUser("root");
        dataSource.setPassword("1234");
        dataSource.setPortNumber(3306);     
        
        Logger logger = mock(Logger.class);
        
        QueriesStore queriesStore = new QueriesStoreImpl(logger, "/database/mysql.queries.sql");
        this.producer = new FederationsProducer(queriesStore, dataSource);
        this.serversProducer = new ServersProducer(queriesStore, dataSource);
                
        DatabaseOperation.CLEAN_INSERT.execute(this.getConnection(), this.getDataSet());
    }
    
    public void testLoadFederations() throws DataSetException {           
        List<Server> servers = this.serversProducer.loadServers();
        long serverId = servers.get(0).getId();
        
        List<Federation> federations = this.producer.loadFederations(serverId);
        
        assertEquals(1, federations.size());
    }
    
    public void testLoadFederation() throws DataSetException {
        ITable table = this.dataSet.getTable("components");
        long serverId = Long.valueOf((String)table.getValue(1, "parent_id"));
        
        List<Federation> federations = this.producer.loadFederations(serverId);
        Federation federation = this.producer.loadFederation(federations.get(0).getId());
        
        assertEquals(table.getValue(0, "name"), federation.getName());                        
    }
    
    public void testCreateFederation() throws DataSetException {
        Server server = new Server(0, "aldebarán", "127.0.0.1");
        Federation federation = new Federation(server);        
        this.producer.saveFederation(federation);       
        
        assertTrue(federation.getId() != 10);
    }
    
    public void testUpdateFederation() throws DataSetException {
        ITable table = this.dataSet.getTable("components");
        long serverId = Long.valueOf((String)table.getValue(1, "parent_id"));
        
        List<Federation> Federations = this.producer.loadFederations(serverId);
        Federation Federation = this.producer.loadFederation(Federations.get(0).getId());
        Federation.setName("servidor nuego");        
        this.producer.saveFederation(Federation);
        
        Federation newFederation = this.producer.loadFederation(Federations.get(0).getId());
        
        assertFalse(newFederation.getName().equals(table.getValue(0, "name")));
    }
    
    public void testDeleteFederation() throws DataSetException {     
        ITable table = this.dataSet.getTable("components");
        long serverId = Long.valueOf((String)table.getValue(1, "parent_id"));
        
        List<Federation> Federations = this.producer.loadFederations(serverId);                        
        this.producer.deleteFederation(Federations.get(0).getId());                
   }   
    

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        DatabaseConnection con = new DatabaseConnection(this.dataSource.getConnection());
        return con;        
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        InputStream is = Resources.getAsStream(DATASET_FILE);
        dataSet = new XmlDataSet(is);
        return dataSet;
    }    
}

