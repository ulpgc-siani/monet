package org.monet.space.fms.control.actions;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.monet.space.fms.control.constants.Actions;
import org.monet.space.fms.control.constants.Parameter;
import org.monet.space.fms.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.ComponentSecurity;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.Node;

import java.net.URLEncoder;
import java.util.HashMap;

public class ActionDownloadDocument extends Action {

	private NodeLayer nodeLayer;
	private ComponentSecurity componentSecurity;
	private ComponentDocuments componentDocuments;

	public ActionDownloadDocument() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentSecurity = ComponentSecurity.getInstance();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	@Override
	public String execute() {
		String nodeId = this.request.getParameter(Parameter.ID_NODE);
		Node node;
		HttpClient httpClient = new HttpClient();
		GetMethod method;
		Integer status;
		Boolean download;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (nodeId == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.DOWNLOAD_DOCUMENT);
		}

		node = this.nodeLayer.loadNode(nodeId);

		if (!this.componentSecurity.canWrite(node, this.getAccount())) {
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, nodeId);
		}

		download = this.componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.DOWNLOAD);
		if ((download == null) || (!download)) {
			throw new SystemException(ErrorCode.DOWNLOAD_NOT_SUPPORTED, nodeId);
		}

		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			String fileId = this.request.getParameter(Parameter.FILENAME);
			parameters.put(Parameter.ID, URLEncoder.encode(fileId, "UTF-8"));
			method = new GetMethod(this.componentDocuments.getDownloadUrl(parameters));
			status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_NOT_FOUND)
				throw new SystemException(ErrorCode.DOWNLOAD_NODE_FILE, nodeId, null);

			this.response.setContentType(method.getResponseHeader("Content-Type").getValue());
			this.response.setHeader("Content-Disposition", method.getResponseHeader("Content-Disposition").getValue());
			IOUtils.copy(method.getResponseBodyAsStream(), response.getOutputStream());

		} catch (Exception oException) {
			throw new SystemException(ErrorCode.DOWNLOAD_NODE_FILE, nodeId, oException);
		}

		return null; // Avoid controller getting response writer
	}

}
