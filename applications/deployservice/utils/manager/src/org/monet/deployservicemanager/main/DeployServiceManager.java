package org.monet.deployservicemanager.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.monet.deployservicemanager.configuration.Configuration;
import org.monet.deployservicemanager.security.Security;
import org.monet.deployservicemanager.utils.Network;
import org.monet.deployservicemanager.utils.Template;
import org.monet.deployservicemanager.xml.Item;
import org.monet.deployservicemanager.xml.ResultXML;
import org.monet.deployservicemanager.xml.ServersXML;

/**
 * Servlet implementation class DeployServerManager
 */
public class DeployServiceManager extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String user;
	private String password;

	private static void initialize() {
		initializeConfiguration();
	}
	
	private static void initializeConfiguration() {
		Configuration.getInstance();
	}
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeployServiceManager() {
		super();
		initialize();		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Template template = new Template(response, this.getServletContext().getRealPath(""));

			String action = StringEscapeUtils.escapeHtml(request.getParameter("action"));
			
			user = (String) request.getSession().getAttribute("user");
			password = (String) request.getSession().getAttribute("password");

			Security security = new Security();
			if (security.verifyUser(user, password)) {
				
	      if ((action != null) && (action.equals("logout"))) {
	      	request.getSession().invalidate();
	      	response.sendRedirect(request.getContextPath() + "/index");
	      } else	
				  showHome(template);
	      
			} else {
				showLogin(template, "");
			}
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.println("I cant not show main page.");
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Template template = new Template(response, this.getServletContext().getRealPath(""));

			String action = StringEscapeUtils.escapeHtml(request.getParameter("action"));
			user = (String) request.getSession().getAttribute("user");
			password = (String) request.getSession().getAttribute("password");
			String server = StringEscapeUtils.escapeHtml(request.getParameter("server"));
			String container = StringEscapeUtils.escapeHtml(request.getParameter("container"));
			String space = StringEscapeUtils.escapeHtml(request.getParameter("space"));
			String docserverServer = StringEscapeUtils.escapeHtml(request.getParameter("docserver-server"));
			String docserverContainer = StringEscapeUtils.escapeHtml(request.getParameter("docserver-container"));
			String restartDocServer = StringEscapeUtils.escapeHtml(request.getParameter("restart-docserver"));
			String updateUrl = StringEscapeUtils.escapeHtml(request.getParameter("update-url"));
			String installSaiku = StringEscapeUtils.escapeHtml(request.getParameter("install-saiku"));
//			String installPlanner = StringEscapeUtils.escapeHtml(request.getParameter("install-planner"));
			String baseURL = StringEscapeUtils.escapeHtml(request.getParameter("base-url"));			
			String replaceTheme = StringEscapeUtils.escapeHtml(request.getParameter("replace-theme"));
			String spaceLabelES = request.getParameter("space-label-es");
			String restartDebug = StringEscapeUtils.escapeHtml(request.getParameter("restart-container-debug"));
			String federation = StringEscapeUtils.escapeHtml(request.getParameter("federation"));
			
			Security security = new Security();
			if (action.equals("login")) {
				user = StringEscapeUtils.escapeHtml(request.getParameter("user"));
				password = StringEscapeUtils.escapeHtml(request.getParameter("password"));

				if (security.verifyUser(user, password)) {
					request.getSession().setAttribute("user", user);
					request.getSession().setAttribute("password", password);
					showHome(template);
				} else {
					showLogin(template, "<font color='red'>User or password is incorrect.</font><br><br>");
				}
			} else {
				if (security.verifyUser(user, password)) {
					
					String command = "<command id='" + action + "'>";
					command += "<parameter id='user'>" + user + "</parameter>";
					command += "<parameter id='password'>" + password + "</parameter>";

					if (server != null && !server.equals(""))
						command += "<parameter id='server'>" + server + "</parameter>";
					if (container != null && !container.equals(""))
						command += "<parameter id='container'>" + container + "</parameter>";

					if (federation != null && !federation.equals(""))
						command += "<parameter id='federation'>"+federation+"</parameter>";					
					if (space != null && !space.equals(""))
						command += "<parameter id='space'>" + space + "</parameter>";

					if (docserverServer != null && !docserverServer.equals(""))
						command += "<parameter id='docserver-server'>" + docserverServer + "</parameter>";
					if (docserverContainer != null && !docserverContainer.equals(""))
						command += "<parameter id='docserver-container'>" + docserverContainer + "</parameter>";
					if (restartDocServer != null && restartDocServer.equals("on")) command += "<parameter id='restart-docserver'>true</parameter>";
					if (updateUrl != null && !updateUrl.equals(""))
						command += "<parameter id='url'>"+updateUrl+"</parameter>";					
					if (installSaiku != null && installSaiku.equals("on"))
						command += "<parameter id='install-saiku'>true</parameter>";
					if (baseURL != null && !baseURL.equals(""))
						command += "<parameter id='base-url'>"+baseURL+"</parameter>";					
					if (replaceTheme != null && replaceTheme.equals("on"))
						command += "<parameter id='replace-theme'>true</parameter>";					
					if (spaceLabelES != null && !spaceLabelES.equals(""))
						command += "<parameter id='space-label-es'>"+spaceLabelES+"</parameter>";
					if (restartDebug != null && restartDebug.equals("on"))
						command += "<parameter id='restart-debug'>true</parameter>";					
					
					command += "</command>";

					PrintWriter out = response.getWriter();
					Network network = new Network(Configuration.getDomain(), Configuration.getPort());

					if (action.equals("update_application")) {
						network.SendTCP(command);
		      	request.getSession().invalidate();
						out.println("<html><body onLoad='redirect()'><h4>Please, wait...</h4><script language='JavaScript'> function redirect() {setTimeout(\"location.href=''\", 5000);}</script></body></html>");
					} else
						  out.println("<html><body>Received message:<br><code>" + network.SendTCP(command).replaceAll("&lt;br/&gt;", "<br>") + "</code><br><br><a href=''>&lt; Back</a></body></html>");
					
					out.close();
				} else
					showLogin(template, "<font color='red'>Your session is expired.</font><br><br>");
			}

		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.println("I cant not show main page. Is Deploy Service power on?");
			out.close();
		}
	}

	private void showHome(Template template) throws ResourceNotFoundException, ParseErrorException, Exception {

		String command = "<command id='get_servers'>";
		command += "<parameter id='user'>" + user + "</parameter>";
		command += "<parameter id='password'>" + password + "</parameter>";
		command += "</command>";

		Network network = new Network(Configuration.getDomain(), Configuration.getPort());
		String get_servers = network.SendTCP(command);

		ResultXML resultXML = new ResultXML();
		Item result = resultXML.unserializelite(get_servers);

		if (result.getItem("status").getContent().equals("ok")) {

			ServersXML serversXML = new ServersXML();
			Item servers = serversXML.unserializelite(result.getItem("content").getContent());

			template.SetVar("configuration_appcaption", Configuration.CONST_AppCaption);
			template.SetVar("servers", servers);
			template.SetVar("showterminal", Configuration.getValue(Configuration.VAR_ShowTerminal, "true"));
			template.Show("home.html");
		}

	}

	private void showLogin(Template template, String message) throws ResourceNotFoundException, ParseErrorException, Exception {
		template.SetVar("user_not_found", message);
		template.SetVar("configuration_appcaption", Configuration.CONST_AppCaption);
		template.SetVar("showterminal", Configuration.getValue(Configuration.VAR_ShowTerminal, "true"));
		template.Show("login.html");
	}
}
