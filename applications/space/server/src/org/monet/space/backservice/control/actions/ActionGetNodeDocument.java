package org.monet.space.backservice.control.actions;

import org.apache.commons.io.IOUtils;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.IOException;
import java.io.InputStream;

public class ActionGetNodeDocument extends Action {

  public ActionGetNodeDocument() {
  }

  @Override
  public String execute() {
    String id = (String) this.parameters.get(Parameter.ID);
    ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
    InputStream fileContent = null;

    if (id == null)
      return ErrorCode.WRONG_PARAMETERS;

    try {
      fileContent = componentDocuments.getDocumentContent(id);
      this.response.setContentType(componentDocuments.getDocumentContentType(id));
      this.response.setHeader("Content-Disposition", "attachment; filename=" + id);
      IOUtils.copy(fileContent, response.getOutputStream());
    } catch (IOException exception) {
      this.agentException.error(exception);
    } finally {
      StreamHelper.close(fileContent);
    }

    return null;
  }

}
