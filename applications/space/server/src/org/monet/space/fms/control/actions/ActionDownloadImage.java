package org.monet.space.fms.control.actions;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.monet.space.fms.configuration.Configuration;
import org.monet.space.fms.control.constants.Actions;
import org.monet.space.fms.control.constants.Parameter;
import org.monet.space.fms.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.ComponentSecurity;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;

public class ActionDownloadImage extends Action {

	private NodeLayer nodeLayer;
	private ComponentSecurity componentSecurity;
	private ComponentDocuments componentDocuments;

	public ActionDownloadImage() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentSecurity = ComponentSecurity.getInstance();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	@Override
	public String execute() {
		String idNode = this.request.getParameter(Parameter.ID_NODE);
		Node oNode;
		HttpClient oHttpClient = new HttpClient();
		GetMethod method;
		Integer iStatus;
		Boolean bDownload;
		InputStream output;

		if (idNode == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.DOWNLOAD_IMAGE);
		}

		oNode = this.nodeLayer.loadNode(idNode);

		if (!this.componentSecurity.canRead(oNode, this.getAccount())) {
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idNode);
		}

		bDownload = this.componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.DOWNLOAD);
		if ((bDownload == null) || (!bDownload)) {
			throw new SystemException(ErrorCode.DOWNLOAD_NOT_SUPPORTED, idNode);
		}

		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			String fileId = this.request.getParameter(Parameter.FILENAME);
			String thumb = this.request.getParameter(Parameter.THUMB);
			boolean isPreview = thumb != null && thumb.equals("1");
			String contentType;

			parameters.put(Parameter.ID, URLEncoder.encode(fileId, "UTF-8"));
			if (isPreview)
				parameters.put(Parameter.THUMB, "1");
			method = new GetMethod(this.componentDocuments.getDownloadUrl(parameters));
			iStatus = oHttpClient.executeMethod(method);

			if (iStatus == HttpStatus.SC_NOT_FOUND || method.getResponseHeader("Content-Type") == null) {
				output = AgentFilesystem.getInputStream(Configuration.getInstance().getNoPictureImageFile());
				contentType = MimeTypes.getInstance().get("jpg");
			} else {
				output = method.getResponseBodyAsStream();
				contentType = method.getResponseHeader("Content-Type").getValue();
			}

			this.response.setContentType(contentType);
			this.response.setHeader("Content-Disposition", "attachment; filename=" + LibraryFile.getFilename(fileId));
			IOUtils.copy(output, response.getOutputStream());

		} catch (Exception oException) {
			throw new SystemException(ErrorCode.DOWNLOAD_NODE_FILE, idNode, oException);
		}

		return null; // Avoid controller getting response writer
	}

}
