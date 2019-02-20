

import java.io.InputStream;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import util.LibrarySigner;
import util.StreamHelper;

public class AgentServiceClient {
  private static AgentServiceClient oInstance;
  
  protected AgentServiceClient() {
  }

  public synchronized static AgentServiceClient getInstance() {
    if (oInstance == null) oInstance = new AgentServiceClient();
    return oInstance;
  }

  public boolean callService(String serviceUrl, InputStream documentStream, String requestDocumentHash,
                             String certificateFile, String certificatePassword, String callbackUrl) {
  	try {
	    String requestId;
	    String requestArgs;
	    DefaultHttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(serviceUrl);
	    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	    HttpResponse response;
	    
	    String codeService = serviceUrl.substring(serviceUrl.substring(0, serviceUrl.length()-1).lastIndexOf("/")).replaceAll("/", "");
	    
	    entity.addPart("requestDocument", new InputStreamBody(documentStream, "application/pdf", "request.pdf"));
	    entity.addPart("requestDocumentHash", new StringBody(requestDocumentHash));
	    entity.addPart("serviceResponseCallback", new StringBody(callbackUrl));
	    entity.addPart("timestamp", new StringBody(String.valueOf((new Date()).getTime())));
	    
	    requestArgs = String.format("codeService=%s&requestDocumentHash=%s&serviceResponseCallback=%s&timestamp=%d", 
	                  codeService, requestDocumentHash, callbackUrl, (new Date()).getTime());

	    String signature = LibrarySigner.signText(requestArgs, certificateFile, certificatePassword);
	    entity.addPart("signature", new StringBody(signature));
	    
	    post.setEntity(entity);

	    response = client.execute(post);
	    
	    requestId = StreamHelper.toString(response.getEntity().getContent());
	    
	    System.out.println("Servicio creado con id='" + requestId + "'");
	    
	    return true;
  	} catch (Exception e) {
  	  e.printStackTrace();
  	}
  	return false;
  }
  
}
