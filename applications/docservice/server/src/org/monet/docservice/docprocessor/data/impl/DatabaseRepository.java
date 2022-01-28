package org.monet.docservice.docprocessor.data.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.exceptions.BaseException;
import org.monet.docservice.core.exceptions.DocServiceException;
import org.monet.docservice.core.log.EventLog;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.sql.NamedParameterStatement;
import org.monet.docservice.core.util.MimeTypes;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.DataSourceProvider;
import org.monet.docservice.docprocessor.data.QueryStore;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentMetadata;
import org.monet.docservice.docprocessor.model.PreviewType;
import org.monet.docservice.docprocessor.model.Template;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseRepository implements Repository {

	private QueryStore queryStore;
	private Logger logger;
	private DataSource dataSource;
	private Configuration configuration;
	private DiskManagerImpl diskManager;
	private MimeTypes mimeTypes;

	@Inject
	public DatabaseRepository(QueryStore queryStore, Logger logger, DataSourceProvider dataSourceProvider, Configuration configuration, DiskManagerImpl diskManager) throws NamingException {
		logger.debug("DatabaseRepository(%s, %s, %s)", queryStore, logger, dataSourceProvider);

		this.queryStore = queryStore;
		this.logger = logger;
		this.dataSource = dataSourceProvider.get();
		this.configuration = configuration;
		this.diskManager = diskManager;
	}

	@Inject
	public void injectMimeTypes(MimeTypes mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public String createTemplate(String code, int documentType) {
		logger.debug("createTemplate(%s ,%s)", code, String.valueOf(documentType));

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet keys = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_TEMPLATE), Statement.RETURN_GENERATED_KEYS);
			statement.setString(QueryStore.INSERT_TEMPLATE_PARAM_CODE, code);
			statement.setInt(QueryStore.INSERT_TEMPLATE_PARAM_ID_DOCUMENT_TYPE, documentType);
			statement.setTimestamp(QueryStore.INSERT_TEMPLATE_PARAM_CREATED_DATE, new Timestamp(Calendar.getInstance().getTime().getTime()));

			keys = statement.executeUpdateAndGetGeneratedKeys();
			if (keys != null && keys.next()) {
				String instanceId = keys.getString(1);
				return instanceId;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(keys);
			close(statement);
			close(connection);
		}
		return "-1";
	}

	public void saveTemplateData(String templateId, InputStream stream, String hash, String contentType, String signsPosition) {
		logger.debug("saveTemplateData(%s, %s, %s, %s, %s)", templateId, stream, hash, contentType, signsPosition);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_TEMPLATE_DATA));
			statement.setString(QueryStore.INSERT_TEMPLATE_DATA_PARAM_ID_TEMPLATE, templateId);
			statement.setBinaryStream(QueryStore.INSERT_TEMPLATE_DATA_PARAM_DATA, stream);
			statement.setString(QueryStore.INSERT_TEMPLATE_DATA_PARAM_HASH, hash);
			statement.setString(QueryStore.INSERT_TEMPLATE_DATA_PARAM_CONTENT_TYPE, contentType);
			statement.setString(QueryStore.INSERT_TEMPLATE_DATA_PARAM_SIGNS_POSITION, signsPosition);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public void addTemplatePart(String templateId, String partId, InputStream partData) {
		logger.debug("addTemplatePart(%s, %s, %s)", templateId, partId, partData);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_TEMPLATE_PART));
			statement.setString(QueryStore.INSERT_TEMPLATE_PART_PARAM_ID, partId);
			statement.setString(QueryStore.INSERT_TEMPLATE_PART_PARAM_TEMPLATE, templateId);
			statement.setBinaryStream(QueryStore.INSERT_TEMPLATE_PART_PARAM_DATA, partData);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public void createDocument(String documentId, String templateId, int state) {
		logger.debug("createDocument(%s, %s, %s)", documentId, templateId, String.valueOf(state));

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.EXISTS_TEMPLATE));
			statement.setString(QueryStore.EXISTS_TEMPLATE_PARAM_ID, templateId);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt(1) < 1)
					throw new DocServiceException(String.format("Template '%s' doesn't exists.", templateId));
			}
			resultSet.close();
			resultSet = null;
			statement.close();
			statement = null;

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_DOCUMENT));
			statement.setString(QueryStore.INSERT_DOCUMENT_PARAM_ID, documentId);
			statement.setString(QueryStore.INSERT_DOCUMENT_PARAM_ID_TEMPLATE, templateId);
			statement.setInt(QueryStore.INSERT_DOCUMENT_PARAM_STATE, state);
			statement.setTimestamp(QueryStore.INSERT_DOCUMENT_PARAM_CREATED_DATE, new Timestamp(Calendar.getInstance().getTime().getTime()));
			statement.executeUpdate();
			statement.close();
			statement = null;

			statement = new NamedParameterStatement(connection, queryStore.get(QueryStore.CREATE_DOCUMENT_DATA_FROM_TEMPLATE));
			statement.setString(QueryStore.CREATE_DOCUMENT_DATA_FROM_TEMPLATE_PARAM_ID_DOCUMENT, documentId);
			statement.executeUpdate();
			statement.close();
			statement = null;

			statement = new NamedParameterStatement(connection, queryStore.get(QueryStore.SELECT_TEMPLATE_DATA_FOR_DOCUMENT));
			statement.setString(QueryStore.SELECT_TEMPLATE_DATA_FOR_DOCUMENT_PARAM_ID_DOCUMENT, documentId);
			resultSet = statement.executeQuery();

			String location;
			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(1);
				location = diskManager.addDocument(documentId, blob.getBinaryStream());
			} else {
				throw new ApplicationException(String.format("Not found template for document %s", documentId));
			}
			statement.close();
			statement = null;

			this.saveDocumentDataLocation(connection, documentId, location);

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
				logger.error(e.getMessage(), e);
			}

			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
	}

	public void createEmptyDocument(String documentId, int state) {
		logger.debug("createEmptyDocument(%s, %s)", documentId, String.valueOf(state));

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_DOCUMENT));
			statement.setString(QueryStore.INSERT_DOCUMENT_PARAM_ID, documentId);
			statement.setString(QueryStore.INSERT_DOCUMENT_PARAM_ID_TEMPLATE, null);
			statement.setInt(QueryStore.INSERT_DOCUMENT_PARAM_STATE, state);
			statement.setTimestamp(QueryStore.INSERT_DOCUMENT_PARAM_CREATED_DATE, new Timestamp(Calendar.getInstance().getTime().getTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public Document getDocument(String documentId) {
		logger.debug("getDocument(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet documentRS = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT));
			statement.setString(QueryStore.SELECT_DOCUMENT_PARAM_ID_DOCUMENT, documentId);
			documentRS = statement.executeQuery();
			if (documentRS != null && documentRS.next()) {
				Document document = new Document();
				document.setId(documentRS.getString(QueryStore.SELECT_DOCUMENT_RESULTSET_ID));
				document.setState(documentRS.getInt(QueryStore.SELECT_DOCUMENT_RESULTSET_STATE));
				document.setDeprecated(documentRS.getBoolean(QueryStore.SELECT_DOCUMENT_RESULTSET_IS_DEPRECATED));

				Template template = new Template();
				template.setCode(documentRS.getString(QueryStore.SELECT_DOCUMENT_RESULTSET_TEMPLATE_CODE));
				template.setId(documentRS.getString(QueryStore.SELECT_DOCUMENT_RESULTSET_TEMPLATE_ID));
				document.setTemplate(template);
				return document;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BaseException(e.getMessage());
		} finally {
			close(documentRS);
			close(statement);
			close(connection);
		}
		throw new ApplicationException(String.format("Document '%s' not found", documentId));
	}

	public void removeDocument(String documentId) {
		logger.debug("removeDocument(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			String location = null;
			if (this.existsDocument(documentId))
				location = this.getDocumentDataLocation(documentId);

			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA));
			statement.setString(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT, documentId);
			statement.executeUpdate();
			statement.close();
			statement = null;

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT_DATA));
			statement.setString(QueryStore.DELETE_DOCUMENT_DATA_ID_DOCUMENT, documentId);
			statement.executeUpdate();
			statement.close();
			statement = null;

			diskManager.deleteDocument(documentId, location);

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT));
			statement.setString(QueryStore.DELETE_DOCUMENT_ID_DOCUMENT, documentId);
			statement.executeUpdate();
			statement.close();
			statement = null;

			connection.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				logger.error(e.getMessage(), e1);
			}
		} finally {
			close(statement);
			close(connection);
		}
	}

	public InputStream getDocumentData(String documentId) {
		logger.debug("getDocumentData(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_DATA_LOCATION));
			statement.setString(QueryStore.SELECT_DOCUMENT_DATA_LOCATION_PARAM_ID_DOCUMENT, documentId);
			resultSet = statement.executeQuery();

			if (resultSet == null || !resultSet.next())
				throw new ApplicationException(String.format("Document %s not found", documentId));

			String location = resultSet.getString(QueryStore.SELECT_DOCUMENT_DATA_LOCATION_RESULTSET_LOCATION);

			return diskManager.readDocument(documentId, location);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
	}

	public String getDocumentDataLocation(String documentId) {
		logger.debug("getDocumentDataLocation(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet rs = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_DATA_LOCATION));
			statement.setString(QueryStore.SELECT_DOCUMENT_DATA_LOCATION_PARAM_ID_DOCUMENT, documentId);
			rs = statement.executeQuery();

			if (rs == null || !rs.next())
				throw new ApplicationException(String.format("Document %s not found", documentId));

			return rs.getString(QueryStore.SELECT_DOCUMENT_DATA_LOCATION_RESULTSET_LOCATION);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(rs);
			close(statement);
			close(connection);
		}
	}

	public String getDocumentDataContentType(String documentId) {
		logger.debug("getDocumentDataContentType(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet rs = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_DATA_CONTENTTYPE));
			statement.setString(QueryStore.SELECT_DOCUMENT_DATA_CONTENTTYPE_PARAM_ID_DOCUMENT, documentId);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				String contentType = rs.getString(QueryStore.SELECT_DOCUMENT_DATA_CONTENTTYPE_RESULTSET_CONTENTTYPE);
				return contentType;
			} else {
				throw new ApplicationException(String.format("Document %s not found", documentId));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(rs);
			close(statement);
			close(connection);
		}
	}

	public String getDocumentHash(String documentId) {
		logger.debug("getDocumentHash(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet rs = null;

		try {
			connection = this.dataSource.getConnection();
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_HASH));
			statement.setString(QueryStore.SELECT_DOCUMENT_HASH_PARAM_ID_DOCUMENT, documentId);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				String hash = rs.getString(QueryStore.SELECT_DOCUMENT_HASH_RESULTSET_HASH);
				return hash;
			} else {
				throw new ApplicationException(String.format("Document %s not found", documentId));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(rs);
			close(statement);
			close(connection);
		}
	}

	public void saveDocumentData(String documentId, InputStream data, String xmlData, String contentType, String hash) {
		logger.debug("saveDocumentData(%s, %s, %s, %s)", documentId, data, xmlData, contentType);

		ByteArrayInputStream xmlDataStream = null;

		try {
			if (xmlData != null)
				xmlDataStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
			saveDocumentData(documentId, data, xmlDataStream, contentType, hash);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			StreamHelper.close(xmlDataStream);
		}
	}

	public void saveDocumentData(String documentId, InputStream data, InputStream xmlData, String contentType, String hash) {
		logger.debug("saveDocumentData(%s, %s, %s, %s, %s)", documentId, data, xmlData, contentType, hash);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet documentDataCount = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_NUMBER_OF_DOCUMENTS_DATA_WITH_ID));
			statement.setString(QueryStore.SELECT_NUMBER_OF_DOCUMENTS_DATA_WITH_ID_PARAM_ID_DOCUMENT, documentId);
			documentDataCount = statement.executeQuery();
			boolean documentExists = documentDataCount != null && documentDataCount.next() && documentDataCount.getInt(1) > 0;
			documentDataCount.close();
			documentDataCount = null;
			statement.close();
			statement = null;

			if (documentExists) {
				String location = this.getDocumentDataLocation(documentId);
				statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.UPDATE_DOCUMENT_DATA_WITH_XML_DATA));
				diskManager.saveDocument(documentId, location, data);
			}
			else {
				statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_DOCUMENT_DATA_WITH_XML_DATA));
				String location = diskManager.addDocument(documentId, data);
				statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_LOCATION, location);
			}

			statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_ID_DOCUMENT, documentId);
			statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_CONTENT_TYPE, contentType);
			statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_HASH, hash);
			statement.setBinaryStream(QueryStore.INSERT_DOCUMENT_DATA_PARAM_XML_DATA, xmlData);
			statement.executeUpdate();
			statement.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(documentDataCount);
			close(statement);
			close(connection);
		}
	}

	public void saveDocumentData(String documentId, InputStream data, String contentType, String hash) {
		logger.debug("saveDocumentData(%s, %s, %s)", documentId, data, contentType);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet documentDataCount = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_NUMBER_OF_DOCUMENTS_DATA_WITH_ID));
			statement.setString(QueryStore.SELECT_NUMBER_OF_DOCUMENTS_DATA_WITH_ID_PARAM_ID_DOCUMENT, documentId);
			documentDataCount = statement.executeQuery();
			boolean documentExists = documentDataCount != null && documentDataCount.next() && documentDataCount.getInt(1) > 0;
			documentDataCount.close();
			documentDataCount = null;
			statement.close();
			statement = null;

			if (documentExists) {
				String location = this.getDocumentDataLocation(documentId);
				statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.UPDATE_DOCUMENT_DATA));
				diskManager.saveDocument(documentId, location, data);
			}
			else {
				statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_DOCUMENT_DATA));
				String location = diskManager.addDocument(documentId, data);
				statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_LOCATION, location);
			}

			statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_ID_DOCUMENT, documentId);
			statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_CONTENT_TYPE, contentType);
			statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_HASH, hash);
			statement.executeUpdate();
			statement.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(documentDataCount);
			close(statement);
			close(connection);
		}
	}

	public void saveDocumentXmlData(String documentId, InputStream xmlData) {
		logger.debug("saveDocumentXmlData(%s, %s)", documentId, xmlData);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet documentDataCount = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.UPDATE_DOCUMENT_XML_DATA));
			statement.setString(QueryStore.INSERT_DOCUMENT_DATA_PARAM_ID_DOCUMENT, documentId);
			statement.setBinaryStream(QueryStore.INSERT_DOCUMENT_DATA_PARAM_XML_DATA, xmlData);
			statement.executeUpdate();
			statement.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(documentDataCount);
			close(statement);
			close(connection);
		}
	}

	public boolean existsDocumentPreview(String documentId) {
		logger.debug("existsDocumentPreview(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet rs = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.EXISTS_DOCUMENT_PREVIEW));
			statement.setString(QueryStore.EXISTS_DOCUMENT_PREVIEW_PARAM_ID_DOCUMENT, documentId);
			rs = statement.executeQuery();

			return (rs != null && rs.next());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(rs);
			close(statement);
			close(connection);
		}
	}

	public String getDocumentPreviewDataContentType(String documentId, int page, int type) {
		logger.debug("getDocumentPreviewDataContentType(%s, %s, %s)", documentId, String.valueOf(page), type);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet rs = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE));
			statement.setString(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_PARAM_ID_DOCUMENT, documentId);
			statement.setInt(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_PARAM_PAGE, page);
			statement.setInt(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_PARAM_TYPE, type);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				String contentType = rs.getString(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_RESULTSET_CONTENTTYPE);
				return contentType;
			} else {
				throw new ApplicationException(String.format("Document %s not found", documentId));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(rs);
			close(statement);
			close(connection);
		}
	}

	public void readDocumentPreviewData(String documentId, int page, OutputStream data, int type) {
		logger.debug("readDocumentPreviewData(%s, %s, %s, %s)", documentId, String.valueOf(page), data, type);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet rs = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA));
			statement.setString(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT, documentId);
			statement.setInt(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_PARAM_PAGE, page);
			statement.setInt(QueryStore.SELECT_DOCUMENT_PREVIEW_DATA_PARAM_TYPE, type);
			rs = statement.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob(1);
				copyData(blob.getBinaryStream(), data);
			} else {
				throw new ApplicationException(String.format("Document preview %s not found", documentId));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(rs);
			close(statement);
			close(connection);
		}
	}

	public void saveDocumentPreviewData(String documentId, int page, InputStream data, String contentType, int type, int width, int height, float aspectRatio) {
		logger.debug("saveDocumentPreviewData(%s, %s, %s, %s, %s, %s, %s, %s)", documentId, String.valueOf(page), data, contentType, type, width, height, aspectRatio);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA));
			statement.setString(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT, documentId);
			statement.setInt(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_PAGE, page);
			statement.setBinaryStream(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_DATA, data);
			statement.setString(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_CONTENTTYPE, contentType);
			statement.setInt(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_TYPE, type);
			statement.setInt(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_WIDTH, width);
			statement.setInt(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_HEIGHT, height);
			statement.setFloat(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_ASPECT_RATIO, aspectRatio);
			statement.setTimestamp(QueryStore.INSERT_DOCUMENT_PREVIEW_DATA_PARAM_CREATED_DATE, new Timestamp(Calendar.getInstance().getTime().getTime()));
			statement.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
			}
			close(statement);
			close(connection);
		}
	}

	public void clearDocumentPreviewData(String documentId) {
		logger.debug("clearDocumentPreviewData(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA));
			statement.setString(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT, documentId);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public DocumentMetadata getDocumentMetadata(Document document) {
		logger.debug("getDocumentMetadata(%s)", document);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = this.dataSource.getConnection();

			DocumentMetadata metadata = new DocumentMetadata();
			metadata.setDocumentId(document.getId());
			metadata.setDeprecated(document.isDeprecated());

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_ESTIMATED_TIME));
			statement.setString(QueryStore.SELECT_DOCUMENT_ESTIMATED_TIME_PARAM_ID_DOCUMENT, document.getId());
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				long time = resultSet.getLong(QueryStore.SELECT_DOCUMENT_ESTIMATED_TIME_RESULTSET_TIME);
				if (time > 0) {
					metadata.setHasPendingOperations(true);
					metadata.setEstimatedTimeToFinish(time);
				}
			}
			close(resultSet);
			close(statement);
			resultSet = null;
			statement = null;

			if (!metadata.getHasPendingOperations()) {
				statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_METADATA));
				statement.setString(QueryStore.SELECT_DOCUMENT_METADATA_PARAM_ID_DOCUMENT, document.getId());

				resultSet = statement.executeQuery();

				if (!resultSet.next()) {
					metadata.addPage(1, 714, 1010, (float) 714.0 / 1010);
				} else {
					do {
						metadata.addPage(resultSet.getInt(QueryStore.SELECT_DOCUMENT_METADATA_RESULTSET_PAGE), resultSet.getInt(QueryStore.SELECT_DOCUMENT_METADATA_RESULTSET_WIDTH), resultSet.getInt(QueryStore.SELECT_DOCUMENT_METADATA_RESULTSET_HEIGHT), resultSet.getFloat(QueryStore.SELECT_DOCUMENT_METADATA_RESULTSET_ASPECT_RATIO));
					} while (resultSet.next());
				}
			}

			return metadata;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
	}

	public void updateDocument(String documentId, int state) {
		logger.debug("updateDocument(%s, %s)", documentId, String.valueOf(state));

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.UPDATE_DOCUMENT));
			statement.setString(QueryStore.UPDATE_DOCUMENT_PARAM_ID_DOCUMENT, documentId);
			statement.setInt(QueryStore.UPDATE_DOCUMENT_PARAM_STATE, state);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public InputStream getTemplatePart(String documentId, String partId) {
		logger.debug("getTemplatePart(%s, %s)", documentId, partId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		ByteArrayOutputStream data = new ByteArrayOutputStream();

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_TEMPLATE_PART));
			statement.setString(QueryStore.SELECT_TEMPLATE_PART_PARAM_ID_DOCUMENT, documentId);
			statement.setString(QueryStore.SELECT_TEMPLATE_PART_PARAM_ID_PART, partId);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(1);
				copyData(blob.getBinaryStream(), data);
			} else {
				throw new ApplicationException(String.format("Template part %s not found", partId));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
		return new ByteArrayInputStream(data.toByteArray());
	}

	public boolean existsDocument(String documentId) {
		logger.debug("existsDocument(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_NUMBER_OF_DOCUMENTS_WITH_ID));
			statement.setString(QueryStore.SELECT_NUMBER_OF_DOCUMENTS_WITH_ID_PARAM_ID_DOCUMENT, documentId);
			resultSet = statement.executeQuery();
			return resultSet != null && resultSet.next() && resultSet.getInt(1) > 0;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
	}

	public int removeAllNodeFiles(int nodeId) {
		logger.debug("removeAllNodeFiles(%d)", nodeId);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();

			String id_document_ims = String.format("%d/ims/%%", nodeId);
			String id_document_dms = String.format("%d/dms/%%", nodeId);
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_NODE_DOCUMENTS));
			statement.setString(QueryStore.DELETE_NODE_DOCUMENTS_PARAM_ID_DOCUMENT_IMS, id_document_ims);
			statement.setString(QueryStore.DELETE_NODE_DOCUMENTS_PARAM_ID_DOCUMENT_DMS, id_document_dms);
			return statement.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public List<String> getTemplateSigns(String id) {
		logger.debug("getTemplateSigns(%s)", id);

		List<String> signsFields = new ArrayList<String>();

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_TEMPLATE_SIGNS));
			statement.setString(QueryStore.SELECT_TEMPLATE_SIGNS_PARAM_ID_TEMPLATE, id);

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				signsFields.add(resultSet.getString(QueryStore.SELECT_TEMPLATE_SIGNS_RESULTSET_CODE));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
		return signsFields;
	}

	public String getTemplateSignsPosition(String id) {
		logger.debug("getTemplateSignsPosition(%s)", id);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_TEMPLATE_SIGNS_POSITION));
			statement.setString(QueryStore.SELECT_TEMPLATE_SIGNS_POSITION_PARAM_ID_TEMPLATE, id);

			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(QueryStore.SELECT_TEMPLATE_SIGNS_POSITION_RESULTSET_POSITION);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
		return null;
	}

	public void addSignFields(String templateId, String[] signFields) {
		logger.debug("addSignFields(%s)", templateId);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			for (String signName : signFields) {
				if (signName == null || signName.isEmpty())
					continue;

				statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_SIGN_FIELDS));
				statement.setString(QueryStore.INSERT_SIGN_FIELDS_PARAM_ID_TEMPLATE, templateId);
				statement.setString(QueryStore.INSERT_SIGN_FIELDS_PARAM_SIGN_NAME, signName);
				statement.executeUpdate();
				statement.close();
				statement = null;
			}
			connection.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
				}
			}

			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public InputStream getDocumentXmlData(String documentId) {
		logger.debug("getDocumentXmlData(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		ByteArrayOutputStream data = new ByteArrayOutputStream();

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_DOCUMENT_XML_DATA));
			statement.setString(QueryStore.SELECT_DOCUMENT_XML_DATA_PARAM_DOCUMENT_ID, documentId);

			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(1);
				if (blob == null)
					return null;
				copyData(blob.getBinaryStream(), data);
			} else {
				throw new ApplicationException(String.format("Document xml data %s not found", documentId));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
		return new ByteArrayInputStream(data.toByteArray());
	}

	public void insertEventLogBlock(List<EventLog> eventLogs) {
		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			for (EventLog eventLog : eventLogs) {
				try {
					statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_EVENTLOG));
					statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_LOGGER, eventLog.getLogger());
					statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_MESSAGE, eventLog.getMessage());
					statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_STACKTRACE, eventLog.getStacktrace());
					statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_PRIORITY, eventLog.getPriority());
					statement.setTimestamp(QueryStore.INSERT_EVENTLOG_PARAM_CREATIONTIME, new Timestamp(eventLog.getCreationTime().getTime()));
					statement.executeUpdate();
				} catch (Exception e) { /*
	                             * Nothing to do, error will be logged in file
                                 * only
                                 */
				} finally {
					close(statement);
				}
			}

			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
			}
			logger.error("Error inserting event log in database: " + e.getMessage(), e);
		} finally {
			close(connection);
		}
	}

	@Override
	public int[] getImageDimension(String documentId) {
		logger.debug("getImageDimension(%s)", documentId);

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		int[] dimension = new int[2];

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.SELECT_IMAGE_DIMENSION));
			statement.setString(QueryStore.SELECT_IMAGE_DIMENSION_PARAM_ID, documentId);

			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				dimension[0] = resultSet.getInt(QueryStore.SELECT_IMAGE_DIMENSION_PARAM_WIDTH);
				dimension[1] = resultSet.getInt(QueryStore.SELECT_IMAGE_DIMENSION_PARAM_HEIGHT);
			} else {
				createImagePreview(documentId, -1, -1);
				return getImageDimension(documentId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
		return dimension;
	}

	private boolean hasAlpha(Image image, String contentType) {
		String extension = mimeTypes.getExtension(contentType);
		boolean alpha;

		if (extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpe"))
			return false;

		try {
			PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
			pg.grabPixels();
			alpha = pg.getColorModel().hasAlpha();
		}
		catch(InterruptedException e) {
			alpha = false;
		}

		return alpha;
	}

	@Override
	public void overwriteDocument(String destinationDocumentId, String sourceDocumentId) {
		logger.debug("overwriteDocument(%s, %s)", destinationDocumentId, sourceDocumentId);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			String sourceDocumentLocation = this.getDocumentDataLocation(sourceDocumentId);
			String destinationDocumentLocation = this.getDocumentDataLocation(destinationDocumentId);
			diskManager.overrideDocument(sourceDocumentId, sourceDocumentLocation, destinationDocumentId, destinationDocumentLocation);

			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.OVERWRITE_DOCUMENT_DATA));
			statement.setString(QueryStore.OVERWRITE_DOCUMENT_PARAM_SOURCE_ID, sourceDocumentId);
			statement.setString(QueryStore.OVERWRITE_DOCUMENT_PARAM_DESTINATION_ID, destinationDocumentId);
			statement.executeUpdate();
			statement.close();
			statement = null;

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.UPDATE_DOCUMENT));
			statement.setString(QueryStore.UPDATE_DOCUMENT_PARAM_ID_DOCUMENT, destinationDocumentId);
			statement.setInt(QueryStore.UPDATE_DOCUMENT_PARAM_STATE, Document.STATE_OVERWRITTEN);
			statement.executeUpdate();
			statement.close();
			statement = null;

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA));
			statement.setString(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT, destinationDocumentId);
			statement.executeUpdate();
			statement.close();
			statement = null;

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
				logger.error(e.getMessage(), e);
			}

			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	public String getVersion() {
		logger.debug("getVersion()");

		Connection connection = null;
		NamedParameterStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = this.dataSource.getConnection();

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INFO_LOAD_VERSION));
			resultSet = statement.executeQuery();

			if (!resultSet.next())
				return null;

			return resultSet.getString("value");

		} catch (Exception e) {
			return null;
		} finally {
			close(resultSet);
			close(statement);
			close(connection);
		}
	}

	public void updateVersion(String version) {
		logger.debug("updateVersion(%s)", version);

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INFO_UPDATE_VERSION));
			statement.setString(QueryStore.INFO_UPDATE_VERSION_PARAM_VALUE, version);
			statement.executeUpdate();
			statement.close();

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
				logger.error(e.getMessage(), e);
			}

			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}
	}

	@Override
	public void cleanDocumentPreviews() {
		logger.debug("cleanDocumentPreviews()");

		Connection connection = null;
		NamedParameterStatement statement = null;

		try {
			connection = this.dataSource.getConnection();
			connection.setAutoCommit(false);

			int days = this.configuration.getDocumentPreviewsCacheSize();
			if (days <= 0)
				return;

			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.CLEAN_DOCUMENT_PREVIEW_DATA));
			statement.setInt(QueryStore.CLEAN_DOCUMENT_PREVIEW_DATA_PARAM_DAYS, days);
			statement.executeUpdate();
			statement.close();

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
				logger.error(e.getMessage(), e);
			}

			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
			close(connection);
		}

	}

	private void saveDocumentDataLocation(Connection connection, String documentId, String location) {
		logger.debug("saveDocumentDataLocation(%s,%s)", documentId, location);

		NamedParameterStatement statement = null;

		try {
			statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.UPDATE_DOCUMENT_DATA_LOCATION));
			statement.setString(QueryStore.UPDATE_DOCUMENT_DATA_LOCATION_PARAM_ID_DOCUMENT, documentId);
			statement.setString(QueryStore.UPDATE_DOCUMENT_DATA_LOCATION_PARAM_LOCATION, location);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e.getMessage());
		} finally {
			close(statement);
		}
	}

	public void createImagePreview(String imageId, int width, int height) {
		ByteArrayInputStream finalImageStream = null;
		ByteArrayOutputStream imageTempOutput = null;
		InputStream imageStream = null;

		try {
			imageStream = getDocumentData(imageId);
			String contentType = mimeTypes.getFromStream(imageStream);
			StreamHelper.close(imageStream);

			imageStream = getDocumentData(imageId);

			BufferedImage image = ImageIO.read(imageStream);
			boolean alpha = this.hasAlpha(image, contentType);
			imageTempOutput = new ByteArrayOutputStream();

			if (width == -1)
				width = image.getWidth();

			if (height == -1)
				height = image.getHeight();

			int thumbWidth = width >= height ? 96 : (width*96)/height;
			int thumbHeight = height >= width ? 96 : (height*96)/width;

			BufferedImage bdest = new BufferedImage(thumbWidth, thumbHeight, alpha?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bdest.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance((double)thumbWidth/image.getWidth(),
				(double)thumbHeight/image.getHeight());
			g.drawRenderedImage(image,at);
			g.dispose();
			ImageIO.write(bdest, "png", imageTempOutput);

			finalImageStream = new ByteArrayInputStream(imageTempOutput.toByteArray());
			clearDocumentPreviewData(imageId);
			saveDocumentPreviewData(imageId, 1, finalImageStream, contentType, PreviewType.PAGE, thumbWidth, thumbHeight, width / (float) height);
		} catch (Exception e) {
			logger.warn(String.format("Could not create image preview for %s", imageId));
			saveDocumentPreviewData(imageId, 1, finalImageStream, "application/octet-stream", PreviewType.PAGE, 0, 0, width / (float) height);
		}
		finally {
			StreamHelper.close(imageStream);
			StreamHelper.close(imageTempOutput);
			StreamHelper.close(finalImageStream);
		}
	}

  /* Utility methods */

	private static final void copyData(InputStream input, OutputStream output) throws IOException {
		int len;
		byte[] buff = new byte[16384];
		while ((len = input.read(buff)) > 0)
			output.write(buff, 0, len);
	}

	private static final void close(Connection connection) {
		if (connection != null)
			try {
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
			}
	}

	private static final void close(NamedParameterStatement statement) {
		if (statement != null)
			try {
				statement.close();
			} catch (SQLException e) {
			}
	}

	private static final void close(ResultSet result) {
		if (result != null)
			try {
				result.close();
			} catch (SQLException e) {
			}
	}

}
