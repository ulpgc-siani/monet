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

package org.monet.space.kernel.components.monet;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.PositionEnumeration;
import org.monet.space.kernel.agents.AgentRestfullClient;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.monet.documents.PresignedDocument;
import org.monet.space.kernel.components.monet.documents.SignPositions;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;
import org.monet.space.kernel.model.wrappers.NodeDocumentWrapper;
import org.monet.space.kernel.utils.PersisterHelper;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentDocumentsMonet extends ComponentDocuments {
	private final Configuration configuration;
	private final String componentDocumentUrl;
	private final AgentRestfullClient restFullClient;

	public static final String NAME = "documentsmonet";

	protected ComponentDocumentsMonet() {
		super();
		this.configuration = Configuration.getInstance();
		this.componentDocumentUrl = this.configuration.getValue(Configuration.COMPONENT_DOCUMENTS_MONET_URL) + "/document/";
		this.restFullClient = AgentRestfullClient.getInstance();
	}

	public synchronized static ComponentDocuments getInstance() {
		if (instance == null)
			instance = new ComponentDocumentsMonet();
		return instance;
	}

	@Override
	public HashMap<Integer, Boolean> getSupportedFeatures() {
		HashMap<Integer, Boolean> hmResult = new HashMap<Integer, Boolean>();
		hmResult.put(Feature.PREVIEW, true);
		hmResult.put(Feature.DOWNLOAD, true);
		hmResult.put(Feature.UPLOAD, true);
		return hmResult;
	}

	@Override
	public void run() throws SystemException {
		if (this.isRunning)
			return;
		this.isRunning = true;
	}

	@Override
	public void stop() throws SystemException {
		if (!this.isRunning)
			return;
		instance = null;
		this.isRunning = false;
	}

	@Override
	public boolean isShared() {
		return configuration.isDocumentServiceShared();
	}

	@Override
	public boolean ping() {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_PING));
			String result = StreamHelper.toString(restFullClient.executePost(this.componentDocumentUrl, parameters).content);
			return result.indexOf("true") != -1;
		} catch (Exception oException) {
			return false;
		}
	}

	@Override
	public void uploadTemplate(String idTemplate, String type, InputStream data, PositionEnumeration position, List<String> signFields) throws SystemException {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			String action = null;

			if (signFields == null || signFields.size() == 0) {
				action = ACTION_UPLOAD_TEMPLATE;
			} else {
				action = ACTION_UPLOAD_TEMPLATE_WITH_SIGNS;
				StringBuilder builder = new StringBuilder();
				for (String sign : signFields) {
					builder.append(sign);
					builder.append(",");
				}
				parameters.put(REQUEST_PARAM_SIGNS_FIELDS, toStringBody(builder.toString()));
			}

			if (data != null)
				parameters.put(REQUEST_PARAM_TEMPLATE_DATA, new InputStreamBody(data, idTemplate));

			parameters.put(REQUEST_PARAM_ACTION, toStringBody(action));
			parameters.put(REQUEST_PARAM_TEMPLATE_CODE, toStringBody(idTemplate));
			parameters.put(REQUEST_PARAM_MIME_TYPE, toStringBody(type));
			parameters.put(REQUEST_PARAM_SIGNS_POSITION, toStringBody(positionForSignature(position).toString()));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.UPLOAD_DOCUMENT_TEMPLATE, idTemplate, oException);
		}
	}

	@Override
	public String getPreviewUrl(Map<String, String> hmParameters) {
		Configuration oConfiguration = Configuration.getInstance();
		String sPreviewUrl;

		sPreviewUrl = oConfiguration.getValue(Configuration.COMPONENT_DOCUMENTS_MONET_URL) + "/preview/?id=" + hmParameters.get("id") + "&space=" + this.getSpace();

		if (hmParameters.get("page") != null)
			sPreviewUrl += "&page=" + hmParameters.get("page");
		if (hmParameters.get("thumb") != null)
			sPreviewUrl += "&thumb=" + hmParameters.get("thumb");

		return sPreviewUrl;
	}

	@Override
	public String getDownloadUrl(Map<String, String> hmParameters) {
		Configuration oConfiguration = Configuration.getInstance();
		String url = oConfiguration.getValue(Configuration.COMPONENT_DOCUMENTS_MONET_URL) + "/download/?id=" + hmParameters.get("id") + "&space=" +this.getSpace();
		if (hmParameters.containsKey("thumb"))
			url += "&thumb=1";
		return url;
	}

	@Override
	public void consolidateDocument(Node node, Boolean async) throws SystemException {
		NodeDocumentWrapper wrapper = new NodeDocumentWrapper(node);
		String idDocument = node.getId();
		Map<String, Integer> signsCount = wrapper.getCountSignatures();

		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			String operation;

			if (signsCount.size() > 0) {
				operation = ACTION_CONSOLIDATE_DOCUMENT_WITH_SIGNS;
				parameters.put(REQUEST_PARAM_SIGNS_COUNT, toStringBody(serializeSignatures(signsCount)));
				parameters.put(REQUEST_PARAM_SIGNS_COUNT_PATTERN, toStringBody(NodeDocumentWrapper.SIGNATURE_ID_PATTERN));
			}
			else
				operation = ACTION_CONSOLIDATE_DOCUMENT;

			parameters.put(REQUEST_PARAM_ACTION, toStringBody(operation));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_ASYNCRONOUS, toStringBody(Boolean.toString(async)));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));

			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.CONSOLIDATE_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public boolean existsDocument(String idDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_EXISTS_DOCUMENT));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			String result = StreamHelper.toString(restFullClient.executePost(this.componentDocumentUrl, parameters).content);
			return result.indexOf("true") != -1;
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.EXISTS_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public void createDocument(String idTemplate, String idDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_CREATE_DOCUMENT));
			parameters.put(REQUEST_PARAM_TEMPLATE_CODE, toStringBody(idTemplate));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.CREATE_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public void createDocumentInteroperable(String idTemplate, String idDocument, String referencedDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_CREATE_DOCUMENT));
			parameters.put(REQUEST_PARAM_TEMPLATE_CODE, toStringBody(idTemplate));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_DOCUMENT_REFERENCED, toStringBody(referencedDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.CREATE_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public void updateDocument(String idDocument, String sContent, Boolean async) {
		try {
			byte[] aData = null;
			if (!sContent.isEmpty()) {
				aData = sContent.getBytes("UTF-8");
			}

			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_UPDATE_DOCUMENT));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			if (aData != null)
				parameters.put(REQUEST_PARAM_DOCUMENT_DATA, new InputStreamBody(new ByteArrayInputStream(aData), idDocument));
			parameters.put(REQUEST_PARAM_ASYNCRONOUS, toStringBody(Boolean.toString(async)));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.UPDATE_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public void copyDocument(String idDocument, String idCopiedDocument, boolean generatePreview) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_COPY_DOCUMENT));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_COPIED_DOCUMENT_CODE, toStringBody(idCopiedDocument));
			parameters.put(REQUEST_PARAM_GENERATE_PREVIEW, toStringBody(Boolean.toString(generatePreview)));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.UPDATE_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public void removeDocument(String idDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_REMOVE_DOCUMENT));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.REMOVE_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public void uploadDocument(String idDocument, InputStream oData, String sContentType, boolean generatePreview) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_UPLOAD_DOCUMENT));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_CONTENT_TYPE, toStringBody(sContentType));
			parameters.put(REQUEST_PARAM_DOCUMENT_DATA, new InputStreamBody(oData, idDocument));
			parameters.put(REQUEST_PARAM_GENERATE_PREVIEW, toStringBody(Boolean.toString(generatePreview)));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.UPLOAD_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public void overwriteDocument(String id, String sourceContentId) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_OVERWRITE_CONTENT));
			parameters.put(REQUEST_PARAM_DESTINATION_DOCUMENT_ID, toStringBody(id));
			parameters.put(REQUEST_PARAM_SOURCE_DOCUMENT_ID, toStringBody(sourceContentId));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.OVERWRITE_DOCUMENT, id, oException);
		}
	}

	@Override
	public void overwriteDocument(String id, String sourceContentId, String schema) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_OVERWRITE_CONTENT));
			parameters.put(REQUEST_PARAM_DESTINATION_DOCUMENT_ID, toStringBody(id));
			parameters.put(REQUEST_PARAM_SOURCE_DOCUMENT_ID, toStringBody(sourceContentId));

			if (schema != null)
				parameters.put(REQUEST_PARAM_DOCUMENT_XML_DATA, toStringBody(schema));

			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));

			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.OVERWRITE_DOCUMENT, id, oException);
		}
	}

	@Override
	public void uploadImage(String idDocument, InputStream oData, String sContentType, int width, int height) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_UPLOAD_IMAGE));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_CONTENT_TYPE, toStringBody(sContentType));
			parameters.put(REQUEST_PARAM_DOCUMENT_DATA, new InputStreamBody(oData, idDocument));
			parameters.put(REQUEST_PARAM_WIDTH, toStringBody(String.valueOf(width)));
			parameters.put(REQUEST_PARAM_HEIGHT, toStringBody(String.valueOf(height)));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.UPLOAD_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public InputStream getDocumentContent(String idDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_GET_DOCUMENT));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			return restFullClient.execute(this.componentDocumentUrl, true, parameters).content;
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.GET_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public String getDocumentSchema(String idDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody(ACTION_GET_DOCUMENT_XML_DATA));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			return StreamHelper.toString(restFullClient.executePost(this.componentDocumentUrl, parameters).content);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.GET_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public String getDocumentHash(String idDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody("getDocumentHash"));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			return StreamHelper.toString(restFullClient.executePost(this.componentDocumentUrl, parameters).content).trim();
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.GET_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public String getDocumentContentType(String idDocument) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody("getDocumentContentType"));
			parameters.put(REQUEST_PARAM_DOCUMENT_CODE, toStringBody(idDocument));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			return StreamHelper.toString(restFullClient.executePost(this.componentDocumentUrl, parameters).content).trim();
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.GET_DOCUMENT, idDocument, oException);
		}
	}

	@Override
	public PresignedDocument prepareDocumentSignature(String documentId, String certificate, String reason, String location, String contact, String field) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody("prepareDocumentSignature"));
			parameters.put(REQUEST_PARAM_DOCUMENT_ID, toStringBody(documentId));
			parameters.put(REQUEST_PARAM_CERTIFICATE, toStringBody(certificate));
			parameters.put(REQUEST_PARAM_SIGN_REASON, toStringBody(reason));
			parameters.put(REQUEST_PARAM_SIGN_LOCATION, toStringBody(location));
			parameters.put(REQUEST_PARAM_SIGN_CONTACT, toStringBody(contact));
			parameters.put(REQUEST_PARAM_SIGN_FIELD, toStringBody(field));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			return PersisterHelper.load(restFullClient.executePost(this.componentDocumentUrl, parameters).content, PresignedDocument.class);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.GET_DOCUMENT, documentId, oException);
		}
	}

	@Override
	public void stampDocumentSignature(String documentId, String signId, String signature, User user) {
		try {
			HashMap<String, ContentBody> parameters = new HashMap<>();
			parameters.put(REQUEST_PARAM_ACTION, toStringBody("stampDocumentSignature"));
			parameters.put(REQUEST_PARAM_DOCUMENT_ID, toStringBody(documentId));
			parameters.put(REQUEST_PARAM_SIGN_ID, toStringBody(signId));
			parameters.put(REQUEST_PARAM_SIGNATURE, toStringBody(signature));
			parameters.put(REQUEST_PARAM_SPACE, toStringBody(this.getSpace()));
			restFullClient.executePost(this.componentDocumentUrl, parameters);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.GET_DOCUMENT, documentId, oException);
		}
	}

	@Override
	public void reset() {
	}

	@Override
	public void create() {
	}

	@Override
	public void destroy() {
	}

	private SignPositions positionForSignature(PositionEnumeration position) {

		if (position == null)
			return SignPositions.NONE;

		if (position == PositionEnumeration.BEGINNING)
			return SignPositions.TOP;

		if (position == PositionEnumeration.NEW_PAGE_AT_BEGINNING)
			return SignPositions.NEW_PAGE_AT_TOP;

		if (position == PositionEnumeration.END)
			return SignPositions.BOTTOM;

		if (position == PositionEnumeration.NEW_PAGE_AT_END)
			return SignPositions.NEW_PAGE_AT_BOTTOM;

		return SignPositions.NONE;
	}

	private StringBody toStringBody(String content) {
		return toStringBody(content, ContentType.TEXT_PLAIN);
	}

	private StringBody toStringBody(String content, ContentType contentType) {
		return new StringBody(content, contentType);
	}

	private String serializeSignatures(Map<String, Integer> signatures) {
		StringBuffer result = new StringBuffer();

		for (String signature : signatures.keySet()) {
			if (result.length() > 0)
				result.append(",");
			result.append(signature);
			result.append("#");
			result.append(signatures.get(signature));
		}

		return result.toString();
	}

}