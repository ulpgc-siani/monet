package org.monet.deployservicemanager.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class Template {

	private VelocityEngine ve;
	private VelocityContext context;
	private HttpServletResponse response;

	public Template(HttpServletResponse response, String path) throws Exception {
		this.response = response;
		
		java.util.Properties p = new java.util.Properties();
		p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");

		ve = new VelocityEngine();

		ve.addProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path + "/WEB-INF/templates");
		ve.addProperty(Velocity.INPUT_ENCODING, "UTF-8");
		ve.addProperty(Velocity.ENCODING_DEFAULT, "UTF-8");

		ve.init(p);
		context = new VelocityContext();
	}

	public void Show(String fileName) throws ResourceNotFoundException, ParseErrorException, Exception {
		org.apache.velocity.Template t = ve.getTemplate(fileName);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		PrintWriter out = response.getWriter();
		out.println(writer.toString());
		out.close();		
	}

	public void SetVar(String name, Object value) {
		context.put(name, value);		
	}
	
}
