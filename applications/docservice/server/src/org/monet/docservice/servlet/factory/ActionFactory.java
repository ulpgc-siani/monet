package org.monet.docservice.servlet.factory;

import org.monet.docservice.guice.factory.Factory;
import org.monet.docservice.servlet.factory.impl.Action;

public interface ActionFactory extends Factory<String, Action> {
    public static final String ACTION_UPLOAD_TEMPLATE = "uploadTemplate";
    public static final String ACTION_UPLOAD_TEMPLATE_WITH_SIGN = "uploadTemplateWithSigns";
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
    public static final String ACTION_GET_DOCUMENT_CONTENT_TYPE = "getDocumentContentType";
    public static final String ACTION_COPY_DOCUMENT = "copyDocument";
    public static final String ACTION_OVERWRITE_CONTENT = "overwriteDocument";
    public static final String ACTION_PREPARE_DOCUMENT_SIGNATURE = "prepareDocumentSignature";
    public static final String ACTION_STAMP_DOCUMENT_SIGNATURE = "stampDocumentSignature";
    public static final String ACTION_PING = "ping";
}
