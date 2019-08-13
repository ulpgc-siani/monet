/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.agents;

import com.vividsolutions.jts.geom.Geometry;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DatabaseException;
import org.monet.space.kernel.exceptions.FilesystemException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.DatabaseRepositoryQuery;
import org.monet.space.kernel.model.MonetResultSet;
import org.monet.space.kernel.sql.NamedParameterStatement;
import org.monet.space.kernel.sql.QueryBuilder;
import org.monet.space.kernel.utils.BufferedQuery;
import org.monet.space.kernel.utils.StreamHelper;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

public abstract class AgentDatabase {
	String error;
	Properties queryRepository;
	Context context;
	Configuration configuration;
	String connectionChain;
	DataSource dataSource;
	AgentLogger logger;

	protected static AgentDatabase instance;
	protected static String TYPE;

	protected static final String QUERY_ESCAPED_SEMICOLON = "::SEMICOLON::";

	private final Map<String, String> columnDefinitions = new HashMap<>();

	protected AgentDatabase() {
		this.error = Strings.EMPTY;
		this.queryRepository = new Properties();
		this.context = Context.getInstance();
		this.configuration = Configuration.getInstance();
		this.connectionChain = "";
		this.dataSource = null;
		this.logger = AgentLogger.getInstance();
		NamedParameterStatement.setDebugMode(this.configuration.isDebugMode());
		NamedParameterStatement.setDatabaseQueryExecutionTimeWarning(this.configuration.getDatabaseQueryExecutionTimeWarning());
	}

	protected void registerType(String type, String definition) {
		columnDefinitions.put(type, definition);
	}

	public abstract class ColumnTypes {

		public static final String BOOLEAN = "boolean";
		public static final String DATE = "date";
		public static final String TEXT = "text";
		public static final String INTEGER = "integer";
		public static final String FLOAT = "float";
	}
	public String getOrderMode(String mode) {
		if (mode.equals(Common.OrderMode.ASCENDANT)) return "asc";
		else if (mode.equals(Common.OrderMode.DESCENDANT)) return "desc";
		return "asc";
	}

	public String getColumnDefinition(String type) {
		if (columnDefinitions.containsKey(type))
			return columnDefinitions.get(type);
		return columnDefinitions.get("default");
	}

	public abstract String getEmptyString();

	public abstract String getDateAsText(Date date);

	public abstract Timestamp getDateAsTimestamp(Date date);

	public abstract Object getDateAsMilliseconds(Date date);

	public abstract Date parseDateInMilliseconds(ResultSet resultSet, String columnName) throws SQLException;

	public abstract int getQueryStartPos(int startPos);

	public abstract String getSchemaName();

	public synchronized static AgentDatabase getInstance() {

		if (TYPE == null) throw new DatabaseException(ErrorCode.DATABASE_CONNECTION, null);
		if (instance == null) {
			if (TYPE.equalsIgnoreCase(Database.Types.MYSQL)) instance = new AgentDatabaseMysql();
			else if (TYPE.equalsIgnoreCase(Database.Types.ORACLE)) instance = new AgentDatabaseOracle();
		}

		return instance;
	}

	public synchronized static boolean setType(String newType) {
		AgentDatabase.TYPE = newType;
		return true;
	}

	public boolean initialize(String dataSourceName) {
		InputStream repositoryQueriesInputStream = null;
		javax.naming.Context context;
		javax.naming.Context envContext;

		this.queryRepository = new Properties();
		try {
			context = new InitialContext();
			repositoryQueriesInputStream = this.configuration.getDatabaseQueries(TYPE);

			this.queryRepository.loadFromXML(repositoryQueriesInputStream);

			envContext = (javax.naming.Context) context.lookup("java:comp/env");
			this.dataSource = (DataSource) envContext.lookup(dataSourceName);
		} catch (NamingException e) {
			throw new FilesystemException(ErrorCode.FILESYSTEM_READ_FILE, configuration.getDatabaseQueriesFilename(TYPE), e);
		} catch (IOException e) {
			throw new FilesystemException(ErrorCode.FILESYSTEM_READ_FILE, configuration.getDatabaseQueriesFilename(TYPE), e);
		} finally {
			StreamHelper.close(repositoryQueriesInputStream);
		}

		return true;
	}

	protected static final void close(Connection connection) {
		if (connection != null)
			try {
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
			}
	}

	protected static final void close(ResultSet result) {
		if (result != null)
			try {
				result.close();
			} catch (Throwable e) {
			}
	}

	protected static final void close(NamedParameterStatement statement) {
		if (statement != null)
			try {
				statement.close();
			} catch (SQLException e) {
			}
	}

	protected static final void close(Statement statement) {
		if (statement != null)
			try {
				statement.close();
			} catch (SQLException e) {
			}
	}

	protected int doUpdateQuery(Connection connection, NamedParameterStatement statement) {
		int result;

		try {
			result = statement.executeUpdate();
		} catch (Exception exception) {
			throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, statement.getQuery(), exception);
		} finally {
			close(statement);
		}

		return result;
	}

	protected String doUpdateQueryAndGetGeneratedKey(Connection connection, NamedParameterStatement statement) {
		ResultSet result = null;

		try {
			result = statement.executeUpdateAndGetGeneratedKeys();
			result.next();
			return result.getString(1);
		} catch (Exception exception) {
			throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, statement.getQuery(), exception);
		} finally {
			this.closeQuery(result);
			close(statement);
		}
	}

	protected NamedParameterStatement getPreparedStatement(Connection connection, String query) {
		NamedParameterStatement result = null;

		try {
			result = new NamedParameterStatement(connection, query);
		} catch (SQLException oException) {
			throw new DatabaseException(ErrorCode.QUERY_FAILED, query, oException);
		}

		return result;
	}

	public NamedParameterStatement getRepositoryPreparedStatement(Connection connection, String name, Map<String, Object> parameters, Map<String, String> subQueries) throws SQLException {
		NamedParameterStatement preparedStatement = null;
		String query;
		QueryBuilder queryBuilder;

		if (!this.queryRepository.containsKey(name)) {
			throw new DatabaseException(ErrorCode.UNKOWN_DATABASE_QUERY, name);
		}

		query = (String) this.queryRepository.get(name);
		queryBuilder = new QueryBuilder(query);

		if (subQueries != null)
			for (Entry<String, String> subQuery : subQueries.entrySet())
				queryBuilder.insertSubQuery(subQuery.getKey(), subQuery.getValue());

		preparedStatement = new NamedParameterStatement(connection, queryBuilder.build());

		if (parameters != null)
			for (Entry<String, Object> parameter : parameters.entrySet()) {
				try {
					if (parameter.getValue() instanceof Geometry)
						preparedStatement.setObject(parameter.getKey(), this.fromGeometry(connection, (Geometry) parameter.getValue()));
					else if (parameter.getValue() instanceof InputStream)
						preparedStatement.setBinaryStream(parameter.getKey(), (InputStream) parameter.getValue());
					else
						preparedStatement.setObject(parameter.getKey(), parameter.getValue());
				} catch (Exception ex) {
					throw new SQLException(ex.getMessage() + " on column: " + parameter.getKey(), ex);
				}
			}

		return preparedStatement;
	}

	public NamedParameterStatement getRepositoryPreparedStatement(Connection connection, String name, Map<String, Object> parameters, Map<String, String> subQueries, int returnGeneratedKeys) {
		NamedParameterStatement preparedStatement = null;
		String query;
		QueryBuilder queryBuilder;

		if (!this.queryRepository.containsKey(name)) {
			throw new DatabaseException(ErrorCode.UNKOWN_DATABASE_QUERY, name);
		}

		query = (String) this.queryRepository.get(name);

		try {
			queryBuilder = new QueryBuilder(query);

			if (subQueries != null)
				for (Entry<String, String> subQuery : subQueries.entrySet())
					queryBuilder.insertSubQuery(subQuery.getKey(), subQuery.getValue());

			preparedStatement = new NamedParameterStatement(connection, queryBuilder.build(), returnGeneratedKeys);

			if (parameters != null) {
				for (Entry<String, Object> parameter : parameters.entrySet()) {
					if (parameter.getValue() instanceof Geometry)
						preparedStatement.setObject(parameter.getKey(), this.fromGeometry(connection, (Geometry) parameter.getValue()));
					else if (parameter.getValue() instanceof InputStream)
						preparedStatement.setBinaryStream(parameter.getKey(), (InputStream) parameter.getValue());
					else
						preparedStatement.setObject(parameter.getKey(), parameter.getValue());
				}
			}
		} catch (Exception oException) {
			throw new DatabaseException(ErrorCode.QUERY_FAILED, query, oException);
		}

		return preparedStatement;
	}

	public List<NamedParameterStatement> getRepositoryPreparedStatements(Connection connection, DatabaseRepositoryQuery[] queries) throws SQLException {
		ArrayList<NamedParameterStatement> result = new ArrayList<NamedParameterStatement>();

		String query;

		for (DatabaseRepositoryQuery dbQuery : queries) {
			query = this.getRepositoryQuery(dbQuery.getName());
			QueryBuilder queryBuilder = new QueryBuilder(query);
			for (Entry<String, String> subQuery : dbQuery.getSubQueries().entrySet())
				queryBuilder.insertSubQuery(subQuery.getKey(), subQuery.getValue());
			String[] singleQueries = queryBuilder.build().split("::SEMICOLON::");
			for (String sinlgeQuery : singleQueries) {
				NamedParameterStatement statement = new NamedParameterStatement(connection, sinlgeQuery);
				for (Entry<String, Object> param : dbQuery.getParameters().entrySet())
					statement.setObject(param.getKey(), param.getValue());
				result.add(statement);
			}
		}

		return result;
	}

	public String getRepositoryProperty(String name) {

		if (!this.queryRepository.containsKey(name)) {
			throw new DatabaseException(ErrorCode.UNKOWN_PROPERTY, name);
		}

		String property = (String) this.queryRepository.get(name);
		return property != null ? property.trim() : null;
	}

	public String getRepositoryQuery(String name) {
		String query;
		if (!this.queryRepository.containsKey(name)) {
			throw new DatabaseException(ErrorCode.UNKOWN_DATABASE_QUERY, name);
		}

		query = (String) this.queryRepository.get(name);

		return query;
	}

	public boolean executeRepositoryUpdateTransaction(DatabaseRepositoryQuery[] queries) throws SQLException {
		Connection connection = null;
		List<NamedParameterStatement> queriesArray;
		String currentQuery = null;
		boolean autoCommit = true;

		if (queries.length == 0) {
			return true;
		}

		try {
			connection = this.dataSource.getConnection();
			queriesArray = this.getRepositoryPreparedStatements(connection, queries);
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);

			for (NamedParameterStatement statement : queriesArray) {
				try {
					currentQuery = statement.getQuery();
					statement.executeUpdate();
				} finally {
					statement.close();
				}
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (SQLException oException) {
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException oRollbackException) {
				throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, Strings.ROLLBACK, oRollbackException);
			}
			throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, currentQuery, oException);
		} finally {
			close(connection);
		}

		return true;
	}

	public boolean executeRepositoryQueries(DatabaseRepositoryQuery[] queries) {
		Connection connection = null;
		String query = null;
		Statement auxStatement = null;
		boolean autoCommit = true;

		try {
			connection = this.dataSource.getConnection();
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);

			for (DatabaseRepositoryQuery dbQuery : queries) {
				query = this.getRepositoryQuery(dbQuery.getName());
				QueryBuilder queryBuilder = new QueryBuilder(query);
				for (Entry<String, String> subQuery : dbQuery.getSubQueries().entrySet())
					queryBuilder.insertSubQuery(subQuery.getKey(), subQuery.getValue());
				String[] singleQueries = queryBuilder.build().split("::SEMICOLON::");
				for (String singleQuery : singleQueries) {
					try {
						singleQuery = LibraryString.cleanSpecialChars(singleQuery);
						int countParameters = dbQuery.getParameters().size();
						if (countParameters > 0) {
							NamedParameterStatement statement = null;
							try {
								statement = new NamedParameterStatement(connection, singleQuery);
								for (Entry<String, Object> param : dbQuery.getParameters().entrySet())
									statement.setObject(param.getKey(), param.getValue());
								statement.execute();
							}
							finally {
								statement.close();
							}
						} else {
							Statement statement = null;
							try {
								statement = connection.createStatement();
								statement.executeUpdate(singleQuery);
							}
							finally {
								statement.close();
							}
						}
					} catch (SQLException ex) {
						this.logger.error(ex);
						try {
							auxStatement = connection.createStatement();
							auxStatement.executeUpdate(singleQuery);
						} finally {
							auxStatement.close();
						}
					}
				}
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (SQLException exception) {
			try {
				if (connection != null) connection.rollback();
			} catch (SQLException oRollbackException) {
				throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, Strings.ROLLBACK, oRollbackException);
			}
			throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, query, exception);
		} finally {
			close(connection);
		}

		return true;
	}

	public MonetResultSet executeRepositorySelectQuery(String name) {
		return this.executeRepositorySelectQuery(name, null, null);
	}

	public MonetResultSet executeRepositorySelectQuery(String name, Map<String, Object> parameters) {
		return this.executeRepositorySelectQuery(name, parameters, null);
	}

	public MonetResultSet executeRepositorySelectQuery(String name, Map<String, Object> parameters, Map<String, String> subQueries) {
		Connection connection = null;
		NamedParameterStatement statement = null;
		MonetResultSet result = null;

		try {
			connection = this.dataSource.getConnection();

			if (connection == null)
				throw new DatabaseException(ErrorCode.DATABASE_CONNECTION, name);

			statement = this.getRepositoryPreparedStatement(connection, name, parameters, subQueries);
			result = new MonetResultSet(connection, statement.executeQuery());
		} catch (SQLException ex) {
			closeQuery(result);
			close(statement);
			close(connection);
			this.logger.error(ex);
			throw new DatabaseException(ErrorCode.DATABASE_CONNECTION, name + "=> " + statement != null ? statement.getQuery() : "", ex);
		}

		return result;
	}

	public int executeRepositoryUpdateQuery(String name) {
		return this.executeRepositoryUpdateQuery(name, null, null);
	}

    public int executeRepositoryUpdateQuery(Connection connection, String name) {
        return this.executeRepositoryUpdateQuery(connection, name, null, null);
    }

	public int executeRepositoryUpdateQuery(String name, Map<String, Object> parameters) {
		return this.executeRepositoryUpdateQuery(name, parameters, null);
	}

    public int executeRepositoryUpdateQuery(Connection connection, String name, Map<String, Object> parameters) {
        return this.executeRepositoryUpdateQuery(connection, name, parameters, null);
    }

	public int executeRepositoryUpdateQuery(String name, Map<String, Object> parameters, Map<String, String> subQueries) {
		Connection connection = null;

		try {
			connection = this.dataSource.getConnection();
            return this.executeRepositoryUpdateQuery(connection, name, parameters, subQueries);
		} catch (SQLException ex) {
			throw new DatabaseException(ErrorCode.DATABASE_CONNECTION, null, ex);
		} finally {
			close(connection);
		}
	}

    public int executeRepositoryUpdateQuery(Connection connection, String name, Map<String, Object> parameters, Map<String, String> subQueries) {
        NamedParameterStatement statement = null;
        int result = -1;

        try {
            statement = this.getRepositoryPreparedStatement(connection, name, parameters, subQueries);
            result = this.doUpdateQuery(connection, statement);
        } catch (SQLException ex) {
            throw new DatabaseException(ErrorCode.QUERY_FAILED, statement != null ? statement.getQuery() : null, ex);
        } finally {
            close(statement);
        }

        return result;
    }

	public String executeRepositoryUpdateQueryAndGetGeneratedKey(String name, Map<String, Object> parameters) {
		return executeRepositoryUpdateQueryAndGetGeneratedKey(name, parameters, null);
	}

	public String executeRepositoryUpdateQueryAndGetGeneratedKey(String name, Map<String, Object> parameters, Map<String, String> subQueries) {
		Connection connection = null;
		NamedParameterStatement statement = null;
		String result = "";

		try {
			connection = this.dataSource.getConnection();
			statement = this.getRepositoryPreparedStatement(connection, name, parameters, subQueries, Statement.RETURN_GENERATED_KEYS);
			result = this.doUpdateQueryAndGetGeneratedKey(connection, statement);
		} catch (SQLException ex) {
			throw new DatabaseException(ErrorCode.QUERY_FAILED, statement != null ? statement.getQuery() : null, ex);
		} finally {
			close(statement);
			close(connection);
		}

		return result;
	}

	public boolean executeUpdateTransaction(BufferedQuery bufferedQuery) {
		Connection connection = null;

		try {
			connection = this.dataSource.getConnection();
            return this.executeUpdateTransaction(connection, bufferedQuery);
		} catch (SQLException exception) {
            this.logger.error(exception);
            return false;
		} finally {
			close(connection);
		}
	}

    public boolean executeUpdateTransaction(Connection connection, BufferedQuery bufferedQuery) {
        boolean autoCommit = true;
        String query;
        Statement statement = null;

        try {
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            while ((query = bufferedQuery.readQuery()) != null) {
                query = query.replace(AgentDatabase.QUERY_ESCAPED_SEMICOLON, Strings.SEMICOLON);
                statement.execute(query);
            }

            if (autoCommit) {
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            this.logger.error(exception);
            try {
                connection.rollback();
            } catch (SQLException oRollbackException) {
                throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, Strings.ROLLBACK, oRollbackException);
            }
            return false;
        } finally {
            close(statement);
        }

        return true;
    }

	public String getConnector() {
		if (AgentDatabase.TYPE == null) return Strings.EMPTY;
		if (AgentDatabase.TYPE.equalsIgnoreCase(Database.Types.MYSQL)) return "com.mysql.jdbc.Driver";
		else if (AgentDatabase.TYPE.equalsIgnoreCase(Database.Types.ORACLE)) return "oracle.jdbc.driver.OracleDriver";
		return Strings.EMPTY;
	}

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException exception) {
            throw new DatabaseException(ErrorCode.DATABASE_CONNECTION, Strings.ROLLBACK, exception);
        }
    }

	public MonetResultSet executeSelectQuery(String query) {
		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = this.getPreparedStatement(connection, query);
			return new MonetResultSet(connection, statement.executeQuery());
		} catch (SQLException ex) {
			close(statement);
			close(connection);
			throw new DatabaseException(ErrorCode.DATABASE_CONNECTION, query + "=> " + statement != null ? statement.getQuery() : "", ex);
		}
	}

	public boolean executeBatchQueries(String batchQueries) {
		Connection connection = null;
		Statement statement = null;
		boolean autoCommit = true;
		String[] batchQueriesArray = batchQueries.split(Strings.SEMICOLON);
		int pos;

		if (batchQueriesArray.length == 0) {
			return true;
		}

		try {
			connection = this.dataSource.getConnection();
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			for (pos = 0; pos < batchQueriesArray.length; pos++) {
				batchQueriesArray[pos] = batchQueriesArray[pos].trim();
				if (batchQueriesArray[pos].equals(Strings.EMPTY)) continue;
				batchQueriesArray[pos] = batchQueriesArray[pos].replace(AgentDatabase.QUERY_ESCAPED_SEMICOLON, Strings.SEMICOLON);
				statement.addBatch(batchQueriesArray[pos].trim());
			}

			statement.executeBatch();

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (SQLException exception) {
			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, Strings.ROLLBACK, rollbackException);
			}
		} finally {
			close(statement);
			close(connection);
		}

		return true;
	}

	public boolean closeQuery(ResultSet result) {

		if (result == null) return false;

		try {
			if (result.getStatement() != null) result.getStatement().close();
			result.close();
		} catch (Exception exception) {
			throw new SystemException(ErrorCode.CLOSE_QUERY, null, exception);
		}

		return true;
	}

	public abstract String prepareAsFullTextCondition(String condition);

	public abstract Object fromGeometry(Connection connection, Geometry geometry) throws Exception;

	public abstract Geometry getGeometryColumn(ResultSet resultSet, String column) throws Exception;

}
