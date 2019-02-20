package utils;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Network {

	private List<String> cookies;
	private String urlLast;
	private HttpURLConnection conn;
	private String USER_AGENT = "Mozilla/5.0 firefox";	
	
	public Network() {
	}
	
	public String sendPost(String url, String postParams) throws Exception {

		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();
		conn.setInstanceFollowRedirects(false);

		// Acts like a browser
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int status = conn.getResponseCode();
/*		
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + java.net.URLDecoder.decode(postParams, "UTF-8"));
		System.out.println("Response Code : " + status);
*/
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		setCookies(conn.getHeaderFields().get("Set-Cookie"));
		setUrlLast(conn.getURL().toString());
		
		
		boolean redirect = false;	 
		// normally, 3xx is redirect
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP
				|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
			redirect = true;
		}
		if (redirect) 
			return this.sendPost(conn.getHeaderField("Location"), postParams);
		else
  		return response.toString();
	}

	public String getPageContent(String url) throws Exception {

		URL obj = new URL(url);
    conn = (HttpURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
/*		
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
*/
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));
		setUrlLast(conn.getURL().toString());

		return response.toString();

	}

	public String getFormParams(String html, String query) throws UnsupportedEncodingException {

		Document doc = Jsoup.parse(html);

		Hashtable<String,String> query_values=new Hashtable<String,String>();
		String values[] = query.split("&");
		String query_value[];
		for (int x=0;x<values.length;x++) {
			query_value = values[x].split("=");
			query_values.put(query_value[0], query_value[1]);
		}
		
		String value="";
    String key ="";
		Element loginform = doc.getElementById("formLogin");
		Elements inputElements = loginform.getElementsByTag("input");
		List<String> paramList = new ArrayList<String>();
		for (Element inputElement : inputElements) {
			key = inputElement.attr("name");
			value = inputElement.attr("value");
			
			if (query_values.containsKey(key)) {
				value = query_values.get(key);
			}
			
			paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
		}

		// build parameters list
		StringBuilder result = new StringBuilder();
		for (String param : paramList) {
			if (result.length() == 0) {
				result.append(param);
			} else {
				result.append("&" + param);
			}
		}
		return result.toString();
	}

	public List<String> getCookies() {
		return cookies;
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

	public String getUrlLast() {
		return urlLast;
	}

	public void setUrlLast(String urlLast) {
		this.urlLast = urlLast;
	}

	public String getRemoteIP() {
		try {
	    return this.getPageContent("http://wtfismyip.com/text");
    } catch (Exception e) {
    	return "";
    }
	}

	public String getDomainIP(String domain) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(domain);
		return address.getHostAddress();
	}
	
	public void setAgent(String agent) {
		USER_AGENT = agent;	
	}

}
