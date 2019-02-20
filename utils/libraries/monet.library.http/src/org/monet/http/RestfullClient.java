package org.monet.http;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class RestfullClient {

  public static Object execute(String url, HashMap<String, ContentBody> parameters) throws Exception {
    DefaultHttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(url);
    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    HttpResponse response;
    
    for(Entry<String, ContentBody> param : parameters.entrySet()) {
      entity.addPart(param.getKey(), param.getValue());
    }
    
    post.setEntity(entity);

    response = client.execute(post);
    
    int status = response.getStatusLine().getStatusCode();
    
    if(status < 200 || status >= 300)
      throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));
    
    return response.getEntity().getContent();
  }
}
