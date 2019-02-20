package app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import utils.Network;
import utils.Strings;

public class Monet {
	private Network http = new Network();
	private Strings strings = new Strings();
	private String mainNode = "";
	private String url_space = "";
	private String lastpage = "";
	
	public Monet(String domain, String space) {
		this.url_space = "http://" + domain + "/" + space + "/";
	}
	
	public void login(String username, String password) throws Exception {
		lastpage = http.getPageContent(url_space);
		lastpage = http.getPageContent(url_space);
		String postParams = http.getFormParams(lastpage, "username="+username+"&password="+password);
		lastpage = http.sendPost(http.getUrlLast() + "&action=login", postParams);
		setMainNodeId();
	}
	
	public void logout() throws UnsupportedEncodingException, Exception {
		send("op=logout&sender=ajax&timezone=0");
	}
		
	public String getMainNode(String view) throws Exception {
		return send("op=loadnode&sender=ajax&timezone=0&id="+mainNode+"&mode=preview.html%3Fview%3D"+view+"&_=");
	}
	
	public String getNode(String node) throws Exception {
		return send("op=loadmainnode&sender=ajax&timezone=0&id="+node+"&_=");		
	}

	public String saveNode(String node, String data) throws UnsupportedEncodingException, Exception {
		lastpage = send("op=savenodeattribute&sender=ajax&timezone=0&idnode="+node+"&_=&data=" + URLEncoder.encode(data, "UTF-8")); 
		return lastpage;
	}
	
	public String getAttribute(String attribute) throws Exception {
		Strings strings = new Strings();
		String expression = "<attribute code='"+attribute+"' order='-1'><attributelist \\\\/><indicatorlist><indicator code='value' order='[^']*'[^>]*>(.*?)<\\\\/indicator>";
		String result = "";
		try {
			result = strings.getRegularExpression(expression, lastpage, 0)[0]; 
		} catch (Exception e) {}
		return result;
		
	}

	public String getLastPage() {
		return lastpage;
	}
	
	public String getLastUrl() {
		return http.getUrlLast();
	}
	
	public String getCookies() {
		if (http.getCookies() == null) return ""; 
		return http.getCookies().toString();
	}

	public void setAgent(String agent) {
		http.setAgent(agent);
	}
	
	private String send(String parameters) throws UnsupportedEncodingException, Exception {
		lastpage = http.sendPost(url_space + "servlet/office.api", parameters/*URLEncoder.encode(parameters, "UTF-8")*/); 
		return lastpage;
	}
	
	private void setMainNodeId() throws Exception {
	  send("op=loadaccount&sender=ajax&timezone=0&_=");
		mainNode = strings.getRegularExpression("\\[\\{\"id\":\"(.*)\",\"active\":true", lastpage, 0)[0];
	}

}
