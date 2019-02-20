package tests;

import utils.Strings;
import app.Main;
import app.Monet;

public class Micv3DevTest extends Thread {

	private String host = "micv3.dev.gisc.siani.es";
	private String space = "micv";
	private String view = "mcfQD9g";
	
	public Micv3DevTest(String str) {
		super(str);
	}
	
	public void run() {
		try {
	    testUser(getName(), "1234");
    } catch (Exception e) {
	    e.printStackTrace();
    }
	}
	
	public void testUser(String username, String password) throws Exception {
		Strings strings = new Strings();
		String page = "";
		String expression = "";
		String cvNode = "";

		Monet monet = new Monet(host, space);
		monet.setAgent(Main.agent);
		
		try {
			monet.login(username, password);
		} catch (Exception e) {
			System.out.println("Error: El usuario '"+username+"' no existe o no tiene acceso.");
			monet.logout();
			return;
		}
		
	  try {
			page  = monet.getMainNode(view);		
		  expression = "<\\s*div class=\\\\\"idnode\\\\\"[^>]*>(.*?)<\\\\\\s*\\/\\s*div>";

		  cvNode = strings.getRegularExpression(expression, page, 1)[0];
		} catch (Exception e) {
			System.out.println("Error: No he podido obtener el nodo del curriculum para el usuario '"+username+"'.");
			monet.logout();
			return;
		}
		monet.getNode(cvNode);
		String nombre = monet.getAttribute("Nombre");
		String apellidos = monet.getAttribute("Apellidos");	
		String paginaWebOld = monet.getAttribute("PaginaWeb");	
		if (paginaWebOld.equals("") || (! strings.isNumeric(paginaWebOld))) 
			paginaWebOld = "0";

		String paginaWeb = Integer.toString(Integer.parseInt(paginaWebOld) + 1);
		page = monet.saveNode(cvNode, "<attribute code=\"PaginaWeb\" order=\"-1\"><indicatorlist><indicator code=\"value\" order=\"1\">"+paginaWeb+"</indicator></indicatorlist></attribute>");
	
		monet.logout();

		String paginaWebNew = "";		
		
	  try {
  		monet.login(username, password);
		} catch (Exception e) {
			System.out.println("Error: Problema, el segundo login no funcionó. Mensaje: "+e.getMessage()+", Contenido: " + monet.getLastPage() + ", URL: " + monet.getLastUrl());
			return;			
		}

	  try {
	  	page  = monet.getMainNode("mcfQD9g");		
	    expression = "<\\s*div class=\\\\\"idnode\\\\\"[^>]*>(.*?)<\\\\\\s*\\/\\s*div>";
		  cvNode = strings.getRegularExpression(expression, page, 1)[0];
		} catch (Exception e) {
			System.out.println("Error: Problema, obteniendo los datos del nodo. Mensaje: "+e.getMessage()+", Contenido: " + monet.getLastPage() + ", URL: " + monet.getLastUrl());
			monet.logout();
			return;			
		}

		monet.getNode(cvNode);
		paginaWebNew = monet.getAttribute("PaginaWeb");	
		monet.logout();		
		
		System.out.println("Info: Usuario: "+username+", Nombre y apellidos: " + nombre +" "+ apellidos + ", Valor Página Web antes: " + paginaWebOld + ", Valor página web después: " + paginaWebNew + ", CV node: " + cvNode);						
	}
}
