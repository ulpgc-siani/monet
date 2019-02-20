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

package org.monet.space.kernel.components;

import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.PositionEnumeration;
import org.monet.space.kernel.components.monet.documents.PresignedDocument;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public abstract class ComponentDocuments extends Component {
    public static final String ACTION_PING = "ping";
    public static final String ACTION_GET_VERSION = "getVersion";
	public static final String ACTION_UPLOAD_TEMPLATE = "uploadTemplate";
	public static final String ACTION_UPLOAD_TEMPLATE_WITH_SIGNS = "uploadTemplateWithSigns";
	public static final String ACTION_UPLOAD_DOCUMENT = "uploadDocument";
	public static final String ACTION_UPLOAD_IMAGE = "uploadImage";
	public static final String ACTION_CREATE_DOCUMENT = "createDocument";
	public static final String ACTION_UPDATE_DOCUMENT = "updateDocument";
	public static final String ACTION_REMOVE_DOCUMENT = "removeDocument";
	public static final String ACTION_EXISTS_DOCUMENT = "existsDocument";
	public static final String ACTION_CONSOLIDATE_DOCUMENT = "consolidateDocument";
	public static final String ACTION_CONSOLIDATE_DOCUMENT_WITH_SIGNS = "consolidateDocumentWithSigns";
	public static final String ACTION_REMOVE_ALL_NODE_FILES = "removeAllNodeFiles";
	public static final String ACTION_GET_DOCUMENT = "getDocument";
	public static final String ACTION_GET_DOCUMENT_XML_DATA = "getDocumentXmlData";
	public static final String ACTION_GET_DOCUMENT_HASH = "getDocumentHash";
	public static final String ACTION_COPY_DOCUMENT = "copyDocument";
	public static final String ACTION_OVERWRITE_CONTENT = "overwriteDocument";

	public static final String REQUEST_PARAM_ACTION = "action";
	public static final String REQUEST_PARAM_TEMPLATE_CODE = "templateCode";
	public static final String REQUEST_PARAM_TEMPLATE_DATA = "templateData";
	public static final String REQUEST_PARAM_MIME_TYPE = "mimeType";
	public static final String REQUEST_PARAM_DOCUMENT_CODE = "documentCode";
	public static final String REQUEST_PARAM_DOCUMENT_DATA = "documentData";
	public static final String REQUEST_PARAM_CONTENT_TYPE = "contentType";
	public static final String REQUEST_PARAM_GENERATE_PREVIEW = "generatePreview";
	public static final String REQUEST_PARAM_WIDTH = "width";
	public static final String REQUEST_PARAM_HEIGHT = "height";
	public static final String REQUEST_PARAM_ASYNCRONOUS = "asynchronous";
	public static final String REQUEST_PARAM_NODE_CODE = "nodeCode";
	public static final String REQUEST_PARAM_COPIED_DOCUMENT_CODE = "copiedDocumentCode";
	public static final String REQUEST_PARAM_SOURCE_DOCUMENT_ID = "sourceDocumentId";
	public static final String REQUEST_PARAM_DESTINATION_DOCUMENT_ID = "destinationDocumentId";
	public static final String REQUEST_PARAM_DOCUMENT_ID = "documentId";
	public static final String REQUEST_PARAM_DOCUMENT_XML_DATA = "documentXmlData";
	public static final String REQUEST_PARAM_CERTIFICATE = "certificate";
	public static final String REQUEST_PARAM_SIGN_REASON = "signReason";
	public static final String REQUEST_PARAM_SIGN_LOCATION = "signLocation";
	public static final String REQUEST_PARAM_SIGN_CONTACT = "signContact";
	public static final String REQUEST_PARAM_SIGN_FIELD = "signField";
	public static final String REQUEST_PARAM_SIGNS_POSITION = "signsPosition";
	public static final String REQUEST_PARAM_SIGNS_FIELDS = "signFields";
	public static final String REQUEST_PARAM_SIGNS_COUNT = "signsCount";
	public static final String REQUEST_PARAM_SIGNS_COUNT_PATTERN = "signsCountPattern";
	public static final String REQUEST_PARAM_SIGN_ID = "signId";
	public static final String REQUEST_PARAM_SIGNATURE = "signature";

	protected static ComponentDocuments instance;

	public static class Feature {
		public static final Integer PREVIEW = 0;
		public static final Integer DOWNLOAD = 1;
		public static final Integer UPLOAD = 2;
	}

	protected ComponentDocuments() {
		super();
	}

	public synchronized static ComponentDocuments getInstance() {
		return instance;
	}

	public static Boolean started() {
		if (instance == null)
			return false;
		return instance.isRunning;
	}

    public abstract boolean ping();

	public abstract void uploadDocument(String idDocument, InputStream oData, String sContentType, boolean generatePreview);

	public abstract void uploadImage(String idDocument, InputStream oData, String sContentType, int width, int height);

	public abstract void uploadTemplate(String idTemplate, String type, InputStream data, PositionEnumeration position, List<String> signFields);

	public abstract String getPreviewUrl(Map<String, String> hmParameters);

	public abstract String getDownloadUrl(Map<String, String> hmParameters);

	public abstract void consolidateDocument(Node node, Boolean async);

	public abstract boolean existsDocument(String idDocument);

	public abstract void createDocument(String idTemplate, String idDocument);

	public abstract void updateDocument(String idDocument, String sContent, Boolean async);

	public abstract void copyDocument(String idDocument, String idCopiedDocument, boolean generatePreview);

	public abstract void removeDocument(String idDocument);

	public abstract InputStream getDocumentContent(String requestDocumentId);

	public abstract String getDocumentSchema(String idDocument);

	public abstract String getDocumentHash(String idDocument);

	public abstract String getDocumentContentType(String documentId);

	public abstract void overwriteDocument(String id, String sourceContentId);

	public abstract void overwriteDocument(String id, String sourceContentId, String schema);

	public abstract PresignedDocument prepareDocumentSignature(String documentId, String certificate, String reason, String location, String contact, String field);

	public abstract void stampDocumentSignature(String documentId, String signId, String signature, User user);

}