package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ActionGetNodeFile extends Action {

	public ActionGetNodeFile() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String name = (String) this.parameters.get(Parameter.NAME);
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		InputStream fileInputStream = null;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		try {
			fileInputStream = new ByteArrayInputStream(new byte[0]);
			String contentType = componentDocuments.getDocumentContentType(name);

			if (componentDocuments.existsDocument(name)) {
				fileInputStream = componentDocuments.getDocumentContent(name);
				contentType = "application/octet-stream";
			}

			this.response.setContentType(contentType);
			this.response.setHeader("Content-Disposition", "attachment; filename=" + name);
			StreamHelper.copyData(fileInputStream, response.getOutputStream());
			this.response.getOutputStream().flush();
		} catch (IOException exception) {
			this.agentException.error(exception);
		} finally {
			StreamHelper.close(fileInputStream);
		}

		return null;
	}

}
