package org.monet.space.fms.control.actions;

import org.apache.commons.fileupload.FileItem;
import org.monet.space.fms.control.constants.Actions;
import org.monet.space.fms.control.constants.Parameter;
import org.monet.space.fms.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.ComponentSecurity;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Session;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionUploadDocuments extends Action {

	private static final String ID_DOCUMENT_TEMPLATE = "%s/file/%s";
	private static final String OUTPUT_FORMAT = "{status: %d,name: \"%s\", mime: \"%s\", size: %d, files:[%s]}";
	private static final String FILE_NAME_EMPTY_CHARACTERS_REPLACEMENT = "[^a-zA-Z\\._\\/0-9\\- \\(\\)]";
	private static final int STATUS_OK = 0;
	private static final int STATUS_CANT_UPLOAD = 1;

	private NodeLayer nodeLayer;
	private ComponentSecurity componentSecurity;
	private ComponentDocuments componentDocuments;

	public ActionUploadDocuments() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentSecurity = ComponentSecurity.getInstance();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	@Override
	public String execute() {
		String nodeId = this.request.getParameter(Parameter.ID_NODE);
		Node node;
		Boolean upload;
		int status = STATUS_CANT_UPLOAD;
		String filename = null, contentType = null;
		List<String> documentIds = new ArrayList<>();

		try {
			this.checkSession(idSession);

			if (!this.getFederationLayer().isLogged())
				return ErrorCode.USER_NOT_LOGGED;

			if (nodeId == null)
				throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.UPLOAD_DOCUMENTS);

			node = this.nodeLayer.loadNode(nodeId);

			if (!this.componentSecurity.canWrite(node, this.getAccount()))
				throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.WRITE_NODE_PERMISSIONS, nodeId);

			upload = this.componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.UPLOAD);
			if ((upload == null) || (!upload))
				throw new SystemException(ErrorCode.UPLOAD_NOT_SUPPORTED, nodeId);

			for (FileItem fileItem : getPostParameterMap(request).get(Parameter.NEW_FILE))
				documentIds.add(upload(fileItem));

			status = STATUS_OK;

		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
		}

		return String.format(OUTPUT_FORMAT, status, filename, contentType, 0, LibraryString.implodeAndWrap(documentIds.toArray(new String[documentIds.size()]), ",", "\""));
	}

	private String upload(FileItem fileItem) throws IOException {
		String filename = LibraryFile.getFilename(fileItem.getName()).replaceAll(FILE_NAME_EMPTY_CHARACTERS_REPLACEMENT, "");
		String documentId = String.format(ID_DOCUMENT_TEMPLATE, request.getParameter(Parameter.ID_NODE), filename);
		String contentType = fileItem.getContentType();

		if (contentType == null || contentType.equals("application/octet-stream"))
			contentType = MimeTypes.getInstance().getFromFilename(filename);

		this.componentDocuments.uploadDocument(documentId, fileItem.getInputStream(), contentType, false);

		return documentId;
	}

	private boolean checkSession(String idSession) {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(idSession);

		if (session == null) {
			agentSession.add(idSession);
			return false;
		}

		return true;
	}

}
