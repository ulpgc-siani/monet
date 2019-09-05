package org.monet.editor.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.eclipse.core.resources.IFile;
import org.monet.editor.library.StreamHelper;
import org.monet.editor.net.AgentRestfullClient.RequestParameter;
import org.monet.editor.net.CountingMultipartEntity.ProgressListener;

public class SpaceSetupServiceClient {

  public static void publish(String apiUrl, String path, IFile modelFile, String keyStorePath, String password, OutputStream consoleStream, ProgressListener listener) throws Exception {
    
    String modelHash = null;
    InputStream modelFileInputStream = null;
    try {
      modelFileInputStream = modelFile.getContents();
      modelHash = StreamHelper.calculateHashToHexString(modelFileInputStream);
    } finally {
      StreamHelper.close(modelFileInputStream);
    }
    
    try {
	  boolean pathContainsOp = path != null && path.contains(":operation");

	  if(!apiUrl.endsWith("/")) apiUrl += "/";
	  if (path != null && path.startsWith("/")) path = path.substring(1);
	  if (pathContainsOp) path = path.replace(":operation", "updatemodel");

	  apiUrl += path != null && !path.isEmpty() ? path : "servlet/setupservice";

  	  if (!pathContainsOp) apiUrl += "?op=updatemodel";
      
      modelFileInputStream = modelFile.getContents();
      ArrayList<Entry<String, ContentBody>> parameters = new ArrayList<Entry<String, ContentBody>>();
      parameters.add(new RequestParameter("op", new StringBody("updatemodel")));
      parameters.add(new RequestParameter("model", new InputStreamBody(modelFileInputStream, "application/monet", "model.zip")));
      parameters.add(new RequestParameter("model-hash", new StringBody(modelHash)));
      
      AgentRestfullClient.getInstance().executeWithAuth(apiUrl, parameters, keyStorePath, password, consoleStream, listener);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      StreamHelper.close(modelFileInputStream);
    }
  }

}
