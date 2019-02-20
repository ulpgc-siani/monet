package org.monet.deployservicemanager.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.deployservicemanager.utils.Files;

/**
 * Servlet implementation class Jnlp
 */
public class Jnlp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Jnlp() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String domain = request.getServerName();
//		String port = String.valueOf(request.getServerPort());
		
		Files files = new Files();
		String sshport = files.loadTextFile("/etc/monet/ssh_port").trim();
				
		response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; filename=\"term.jnlp\"");
		
	 		
		PrintWriter out = response.getWriter();
		out.println("<?xml version='1.0' encoding='utf-8'?>");
		out.println("<jnlp spec='1.0+' codebase='http://www.weavervsworld.com/ssh' href='mindterm.jnlp'>");
		out.println("  <information>");
		out.println("    <title>MindTerm 3.1.2 SSH</title>");
		out.println("    <vendor>Appgate</vendor>");
  	out.println("    <homepage href='http://www.appgate.com/mindterm'/>");
		out.println("    <description>MindTerm Java SSH Client</description>");
		out.println("    <offline-allowed/>");
		out.println("</information>");
		out.println("<security>");
		out.println("  <all-permissions/>");
		out.println("</security>");
		out.println("<resources>");
		out.println("  <j2se version='1.4+'/>");
		out.println("  <jar href='mindterm312.weavselfsign.jar'/>");
		out.println("</resources>");
		out.println("<application-desc main-class='com.mindbright.application.MindTerm'>");
		out.println("  <argument>-protocol</argument>");
		out.println("  <argument>ssh2</argument>");
		out.println("  <argument>-alive</argument>");
		out.println("  <argument>60</argument>");
		out.println("  <argument>-80x132-enable</argument>");
		out.println("  <argument>true</argument>");
		out.println("  <argument>-80x132-toggle</argument>");
		out.println("  <argument>true</argument>");
		out.println("  <argument>-bg-color</argument>");
		out.println("  <argument>black</argument>");
		out.println("  <argument>-fg-color</argument>");
		out.println("  <argument>white</argument>");
		out.println("  <argument>-cursor-color</argument>");
		out.println("  <argument>i_green</argument>");
		out.println("  <argument>-geometry</argument>");
		out.println("  <argument>132x35</argument>");
		out.println("  <argument>-encoding</argument>");
		out.println("  <argument>utf-8</argument>");

		out.println("  <argument>-server</argument>");
		out.println("  <argument>"+domain+"</argument>");
		out.println("  <argument>-port</argument>");
		out.println("  <argument>"+sshport+"</argument>");
		out.println("  <argument>-username</argument>");
		out.println("  <argument>root</argument>");
		out.println("</application-desc>");
		out.println("</jnlp>");
		out.close();		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
