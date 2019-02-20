package org.monet.editor.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.monet.editor.library.LibraryFilesystem;
import org.monet.editor.library.LibrarySigner;
import org.monet.editor.library.StreamHelper;
import org.monet.editor.net.CountingMultipartEntity.ProgressListener;

public class AgentRestfullClient {

  private static AgentRestfullClient instance;

  public synchronized static AgentRestfullClient getInstance() {
    if (instance == null) {
      instance = new AgentRestfullClient();
    }
    return instance;
  }

  private AgentRestfullClient() {
  }

  public static class RequestParameter implements Entry<String, ContentBody> {

    private String      key;
    private ContentBody value;

    public RequestParameter(String key, ContentBody value) {
      this.key = key;
      this.value = value;
    }

    public static final String NAME_THESAURUS = "name-thesaurus";
    public static final String ACTION         = "action";
    public static final String START_POS      = "start-pos";
    public static final String COUNT          = "count";
    public static final String MODE           = "mode";
    public static final String FLATTEN        = "flatten";
    public static final String DEPTH          = "depth";
    public static final String FROM           = "from";
    public static final String SEARCH_TEXT    = "search-text";

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

  public InputStream execute(HttpClient client, String url, HashMap<String, ContentBody> parameters) throws Exception {
    HttpPost post = new HttpPost(url);
    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    HttpResponse response;

    StringBuilder requestArgsBuilder = new StringBuilder();

    if(parameters != null) {
      for (Entry<String, ContentBody> param : parameters.entrySet()) {
        entity.addPart(param.getKey(), param.getValue());
  
        if (param.getValue() instanceof StringBody) {
          requestArgsBuilder.append("&");
          requestArgsBuilder.append(param.getKey());
          requestArgsBuilder.append("=");
          requestArgsBuilder.append(LibraryFilesystem.getReaderContent(((StringBody) param.getValue()).getReader()));
        }
      }
    }

    post.setEntity(entity);

    response = client.execute(post);

    int status = response.getStatusLine().getStatusCode();

    if (status < 200 || status >= 300)
      throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

    return response.getEntity().getContent();
  }

  public String execute(String url, HashMap<String, ContentBody> parameters) throws Exception {
    HttpClient client = this.getHttpClient(url);
    return StreamHelper.toString(this.execute(client, url, parameters));
  }

  private X509TrustManager trustManager = new X509TrustManager() {

    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  };
  
  private HttpClient getHttpClient(String url) throws NoSuchAlgorithmException, KeyManagementException {
    DefaultHttpClient client = new DefaultHttpClient();
    
    if(url.startsWith("https")) {
      SSLContext ctx = SSLContext.getInstance("TLS");
      ctx.init(null, new TrustManager[]{trustManager}, null);
      SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      ClientConnectionManager ccm = client.getConnectionManager();
      SchemeRegistry sr = ccm.getSchemeRegistry();
      sr.register(new Scheme("https", 443, ssf));
      client = new DefaultHttpClient(ccm, client.getParams());
    }
    
    return client;
  }

  public void executeWithAuth(String url, ArrayList<Entry<String, ContentBody>> parameters, String keyStorePath, String password, OutputStream resultStream, ProgressListener listener) throws Exception {
    HttpClient client = this.getHttpClient(url);
    
    HttpPost post = new HttpPost(url);
    CountingMultipartEntity entity = new CountingMultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, listener);
    HttpResponse response;

    StringBuilder requestArgsBuilder = new StringBuilder();

    for (Entry<String, ContentBody> param : parameters) {
      entity.addPart(param.getKey(), param.getValue());

      if (param.getValue() instanceof StringBody && !param.getKey().equals("op")) {
        requestArgsBuilder.append(param.getKey());
        requestArgsBuilder.append("=");
        requestArgsBuilder.append(LibraryFilesystem.getReaderContent(((StringBody) param.getValue()).getReader()).trim());
        requestArgsBuilder.append("&");
      }
    }

    String timestamp = String.valueOf((new Date()).getTime());
    entity.addPart("timestamp", new StringBody(timestamp));

    requestArgsBuilder.append("timestamp=");
    requestArgsBuilder.append(timestamp);

    String signature = LibrarySigner.signText(requestArgsBuilder.toString(), keyStorePath, password);
    entity.addPart("signature", new StringBody(signature));

    post.setEntity(entity);

    response = client.execute(post);

    int status = response.getStatusLine().getStatusCode();

    if (status < 200 || status >= 300)
      throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

    response.getEntity().writeTo(resultStream);
  }

}
