package org.monet.federation.setupservice.control.actions;

import org.monet.federation.setupservice.control.constants.Parameter;
import org.monet.federation.setupservice.core.constants.MessageCode;
import org.monet.filesystem.StreamHelper;

import java.io.FileOutputStream;
import java.io.InputStream;


public class ActionPutLogo extends ActionSetupService {

  @Override
  public String execute() throws Exception {
    InputStream originalLogo = (InputStream)this.parameters.get(Parameter.LOGO);
    FileOutputStream outputLogo = null;
    try {
      outputLogo = new FileOutputStream(this.configuration.getLogoFilename());
      StreamHelper.copy(originalLogo, outputLogo);
    }finally{
      StreamHelper.close(outputLogo);
    }
 
    return MessageCode.FEDERATION_OPERATION_OK;
  }

}
