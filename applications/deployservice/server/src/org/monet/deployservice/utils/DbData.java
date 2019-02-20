package org.monet.deployservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbData {
	public String url;
	public String urlAdmin;
	public String user;
	public String password;
	public String name;
	public String type;
	public boolean create;

	public DbData() {
		url = "";
		urlAdmin = "";
		user = "";
		password = "";
		name = "";
		type = "";
		create = true;
	}
	
	public String getHost() {
		Pattern pattern = Pattern.compile(".*:.*://(.*):(.*)/.*");
		Matcher matcher = pattern.matcher(url);
		String host = "";
		if (matcher.find()) {
			host = matcher.group(1);
		}
		if (host.equals("") || host.equals("localhost")) host = "127.0.0.1";
		
		return host;
	}

}
