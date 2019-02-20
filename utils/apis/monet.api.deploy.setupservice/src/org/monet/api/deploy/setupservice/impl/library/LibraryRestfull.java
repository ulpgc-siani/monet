package org.monet.api.deploy.setupservice.impl.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class LibraryRestfull {

  public static class RequestParameter implements Entry<String, ContentBody> {
    
    private String key;
    private ContentBody value;
    
    public RequestParameter(String key, ContentBody value) {
      this.key = key;
      this.value = value;
    }
    
    @Override
    public String getKey() {
      return key;
    }
    
    @Override
    public ContentBody getValue() {
      return value;
    }
    
    @Override
    public ContentBody setValue(ContentBody value) {
      this.value = value;
      return value;
    }
    
  }
  
  private static String getReaderContent(Reader oReader) throws IOException {
    StringBuffer sbContent = new StringBuffer();
    BufferedReader oBufferedReader;
    String sLine;

    oBufferedReader = new BufferedReader(oReader);
    while ((sLine = oBufferedReader.readLine()) != null) {
      sbContent.append(sLine + "\n");
    }

    return sbContent.toString();
  }

  public static String execute(String url, HashMap<String, ContentBody> parameters, String certificateFilename, String certificatePassword) throws Exception {
    DefaultHttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(url);
    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    HttpResponse response;
    
    StringBuilder requestArgsBuilder = new StringBuilder();
    
    for(Entry<String, ContentBody> param : parameters.entrySet()) {
      entity.addPart(param.getKey(), param.getValue());
      
      if(param.getValue() instanceof StringBody) {
        if (param.getKey().equals("op")) continue;
        requestArgsBuilder.append(param.getKey());
        requestArgsBuilder.append("=");
        requestArgsBuilder.append(LibraryRestfull.getReaderContent(((StringBody)param.getValue()).getReader()).trim());
        requestArgsBuilder.append("&");
      }
    }
    
    String timestamp = String.valueOf((new Date()).getTime());
    entity.addPart("timestamp", new StringBody(timestamp));
    
    requestArgsBuilder.append("timestamp=");
    requestArgsBuilder.append(timestamp);

    String signature = LibrarySigner.signText(requestArgsBuilder.toString(), certificateFilename, certificatePassword);
    entity.addPart("signature", new StringBody(signature));
    
    post.setEntity(entity);

    response = client.execute(post);
    
    int status = response.getStatusLine().getStatusCode();
    
    if(status < 200 || status >= 300)
      throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));
    
    InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
    
    return LibraryRestfull.getReaderContent(reader).trim();
  }

}
