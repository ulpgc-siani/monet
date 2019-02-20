<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>

<%
  File file = new File("/etc/monet/deployservice_port");
  BufferedReader reader = null;
  String text = "";
  
  try {
    reader = new BufferedReader(new FileReader(file));
    text = reader.readLine();

  } catch (FileNotFoundException e) {
  } catch (IOException e) {
  } finally {
    try {
      if (reader != null) {
        reader.close();
      }
    } catch (IOException e) {
    }
  } 
%>

<%=text%>
