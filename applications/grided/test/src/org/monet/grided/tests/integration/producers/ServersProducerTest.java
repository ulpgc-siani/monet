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
import org.monet.grided.core.model.Server;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.persistence.database.impl.QueriesStoreImpl;
import org.monet.grided.core.persistence.database.impl.ServersProducer;
import org.monet.grided.core.util.Resources;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class ServersProducerTest extends DatabaseTestCase {

    private static final String DATASET_FILE = "/database/dataset/servers.xml";
    
    private MysqlDataSource dataSource;
    private IDataSet dataSet;
    private ServersProducer producer;

    public void setUp() throws Exception {
        dataSource = new MysqlDataSource();
        dataSource.setDatabaseName("grided-test");                        
        dataSource.setUser("root");
        dataSource.setPassword("1234");
        dataSource.setPortNumber(3306);     
        
        Logger logger = mock(Logger.class);
        
        QueriesStore queriesStore = new QueriesStoreImpl(logger, "/database/mysql.queries.sql");
        this.producer = new ServersProducer(queriesStore, dataSource);
        
        DatabaseOperation.CLEAN_INSERT.execute(this.getConnection(), this.getDataSet());
    }
    
    public void testLoadServers() throws DataSetException {
        ITable table = this.dataSet.getTable("components");
        List<Server> servers = this.producer.loadServers();
        
        assertEquals(table.getRowCount(), servers.size());
    }
    
    public void testLoadServer() throws DataSetException {
        ITable table = this.dataSet.getTable("components");
        List<Server> servers = this.producer.loadServers();
        Server server = this.producer.loadServer(servers.get(0).getId());
        
        assertEquals(table.getValue(0, "name"), server.getName());                       
    }
    
    public void testCreateServer() throws DataSetException {       
        Server server = new Server("aldebarán", "10.2.2.23");        
        this.producer.saveServer(server);       
        
        assertTrue(server.getId() != 10);
    }
    
    public void testUpdateServer() throws DataSetException {
        ITable table = this.dataSet.getTable("components");
        
        List<Server> servers = this.producer.loadServers();
        Server server = this.producer.loadServer(servers.get(0).getId());
        server.setName("servidor nuego");        
        this.producer.saveServer(server);
        
        Server newServer = this.producer.loadServer(servers.get(0).getId());
        
        assertFalse(newServer.getName().equals(table.getValue(0, "name")));
    }
    
    public void testDeleteServer() throws DataSetException {        
        List<Server> servers = this.producer.loadServers();                        
        this.producer.deleteServer(servers.get(0).getId());                
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

