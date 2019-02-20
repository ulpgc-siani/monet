package org.monet.deployservice.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.X509Certificate;

public class Network {

	private Logger logger;
	
	public Network() {
		logger = Logger.getLogger(this.getClass());
	}
	
	public String getCurrentEnvironmentNetworkIp(String InterfaceName) {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();

			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();

				while (address.hasMoreElements()) {
					InetAddress addr = address.nextElement();

					if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && !(addr.getHostAddress().indexOf(":") > -1)
					    && (ni.getDisplayName().equals(InterfaceName))) {
						return addr.getHostAddress();
					}
				}
			}

			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		} catch (SocketException e) {
			System.err.println("Somehow we have a socket error...");
			return "";
		}
	}
	
  public byte[] download(String url) throws IOException {
	  logger.info("Download start: " + url);
  	  	  
		Pattern pattern = Pattern.compile("(.*)://(.*):(.*)@");
		Matcher matcher = pattern.matcher(url);
    String protocol = "";
  	String username = "";
    String password = "";
		if (matcher.find()) {
			protocol = matcher.group(1);
			username = matcher.group(2);
			password = matcher.group(3);
		} else {
			pattern = Pattern.compile("(.*)://.*");
			matcher = pattern.matcher(url);
			if (matcher.find()) {
				protocol = matcher.group(1);
			}			
		}

	  logger.info("Download username: " + username);
		
    Authenticator.setDefault(new MyAuthenticator(username, password));
  	
		URL u = new URL(url);
    URLConnection uc = u.openConnection();
    
    if (protocol.equals("https")) { 
  	  try {	  
  	  	// Create a trust manager that does not validate certificate chains
  	      final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
  	          @Override
  	          public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
  	          }
  	          @Override
  	          public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
  	          }
  	          @Override
  	          public X509Certificate[] getAcceptedIssuers() {
  	              return null;
  	          }
  	      } };
  	      
  	      // Install the all-trusting trust manager
  	      final SSLContext sslContext = SSLContext.getInstance( "SSL" );
  	      sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
  	      // Create an ssl socket factory with our all-trusting manager
  	      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

  	      ( (HttpsURLConnection) uc ).setSSLSocketFactory( sslSocketFactory );
  	  
  	  } catch ( final Exception e ) {
  	    e.printStackTrace();
      }	  
    }       
    
    int contentLength = uc.getContentLength();
    
    java.util.List<String> values = uc.getHeaderFields().get("Content-Length");
    if (values != null && !values.isEmpty()) {
      String sLength = (String) values.get(0);

    	if (sLength != null) {
    		contentLength = Integer.parseInt(sLength); 
    	}
    }    

//    InputStream raw = uc.getInputStream();
//    InputStream in = new BufferedInputStream(raw);
    InputStream in = new BufferedInputStream(uc.getInputStream());   
    byte[] data = new byte[contentLength];
    int bytesRead = 0;
    int offset = 0;
    int retry = 10;

    while (offset < contentLength) {
//      bytesRead = in.read(data, offset, data.length - offset);

      while (retry > 0) {
        bytesRead = in.read(data, offset, data.length - offset);
        if (bytesRead == -1) {
          if (offset == contentLength) {
            break;
          } else {
            offset = 0;
            uc = u.openConnection();
            in = new BufferedInputStream(uc.getInputStream());
            retry--;
            logger.warn("Fail download '"+url+"'. Retry: " + retry);
          }
        } else break;
      }
     
      
      if (bytesRead == -1) break;
      offset += bytesRead;
    }
    in.close();

    if (offset != contentLength) {
    	String message = "Only read " + offset + " bytes; Expected " + contentLength + " bytes";
    	logger.error(message);
      throw new IOException(message);
    }

		logger.info("Download finish: " + url);
    return data;
	}
	
	
  public void downloadFile(String url, String fileName) throws IOException { 	
    FileOutputStream out = new FileOutputStream(fileName);
    out.write(download(url));
    out.flush();
    out.close();  	
	}
  
  public String downloadString(String url) {
  	String result="";
    try {
	    result = new String(download(url), "UTF-8");
    } catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
    } catch (IOException e) {
	    e.printStackTrace();
    }
    return result;
  }
  
  static class MyAuthenticator extends Authenticator {
    private String username, password;

    public MyAuthenticator(String user, String pass) {
      username = user;
      password = pass;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
      System.out.println("Requesting Host  : " + getRequestingHost());
      System.out.println("Requesting Port  : " + getRequestingPort());
      System.out.println("Requesting Prompt : " + getRequestingPrompt());
      System.out.println("Requesting Protocol: " + getRequestingProtocol());
      System.out.println("Requesting Scheme : " + getRequestingScheme());
      System.out.println("Requesting Site  : " + getRequestingSite());
      return new PasswordAuthentication(username, password.toCharArray());
    }
  }
  
}
