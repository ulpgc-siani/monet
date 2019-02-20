package org.monet.space.mobile.helpers;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.monet.space.mobile.net.MonetSSLSocketFactory;

import android.content.Context;

public class HttpClientHelper {

  private static final String      USER_AGENT        = "Monet Mobile Client/1.0";

  public static DefaultHttpClient setupHttpClient(Context context) {
    
    HttpParams httpParameters = new BasicHttpParams();
    HttpConnectionParams.setStaleCheckingEnabled(httpParameters, false);
    HttpConnectionParams.setConnectionTimeout(httpParameters, 4000000);
    HttpConnectionParams.setSoTimeout(httpParameters, 10000000);
    HttpConnectionParams.setSocketBufferSize(httpParameters, 8192);

    HttpClientParams.setRedirecting(httpParameters, false);

    HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(httpParameters, "UTF-8");
    HttpProtocolParams.setUserAgent(httpParameters, USER_AGENT);

    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    schemeRegistry.register(new Scheme("https", MonetSSLSocketFactory.getSocketFactory(context), 443));

    ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParameters, schemeRegistry);

    return new DefaultHttpClient(manager, httpParameters);
  }

}
