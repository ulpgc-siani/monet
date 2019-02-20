package org.monet.federation.accountoffice.core.database.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepositorySource;
import org.monet.federation.accountoffice.core.logger.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.Properties;

@Singleton
public class DataRepositorySourceImpl implements DataRepositorySource {

	private Logger logger;
	private DataSource dataSource;
	private Properties queries;

	@Inject
	public DataRepositorySourceImpl(Logger logger, Configuration configuration) {
		try {
			Context context = new InitialContext();
			Context envContext = (javax.naming.Context) context.lookup("java:comp/env");
			this.dataSource = (DataSource) envContext.lookup(configuration.getDatabaseSource());

			this.queries = configuration.getQueries();

		} catch (Exception e) {
			String message = String.format("Check your database source name is correct: %s" + configuration.getDatabaseSource());
	        System.out.println(message);
			this.logger.error(message, e);
		}
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public Properties getQueries() {
		return this.queries;
	}

}
