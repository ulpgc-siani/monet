<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test Console</title>
</head>
<body>

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>

<%!
 String convertStreamToString(InputStream is) throws IOException {
  if (is != null) {
      StringBuilder sb = new StringBuilder();
      String line;

      try {
          BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
          while ((line = reader.readLine()) != null) {
              sb.append(line).append("\n");
          }
      } finally {
          is.close();
      }
      return sb.toString();
  } else {        
      return "";
  }
 };
 
 String NL = System.getProperty("line.separator"); 
 
 String printToString(Throwable throwable) {
   StringBuilder sb = new StringBuilder();   
       
   for (Throwable e = throwable; e != null; e = e.getCause()) {
     sb.append(NL);
     sb.append(e == throwable ? "Exception:" : "Caused By:").append(NL);
     sb.append("----------").append(NL);

     sb.append(">>> Type: ").append(e.getClass().getName()).append(NL);
     sb.append(">>> Message: ").append(e.getMessage()).append(NL);     
     StackTraceElement[] elems = e.getStackTrace();
     if (elems != null && elems.length > 0) {
       sb.append(">>> Stack trace: ").append(NL);

       for (StackTraceElement elem : elems) {
         sb.append(">>>     at ");
         sb.append(elem.getClassName());
         sb.append('.');
         sb.append(elem.getMethodName());
         sb.append('(');
         sb.append(elem.getFileName());
         sb.append(':');
         sb.append(Math.max(1, elem.getLineNumber()));
         sb.append(')');
         sb.append(NL);
       }
     }
   }
   return sb.toString();
 };
 
 
%>

<%
  Runtime runtime = Runtime.getRuntime();
  String path = "";

  String errorFinal = "";
  String messageFinal = "";
%>
Java version: <%= System.getProperty("java.version") %> from <%= System.getProperty("java.vendor")%><br>
<br>
Max memory: <%= runtime.maxMemory() %><br>
Total memory: <%= runtime.totalMemory() %><br>
Free memory: <%= runtime.freeMemory() %><br>
<%
//String nm = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("java.awt.graphicsenv", null));
// <!--java.awt.graphicsenv: <%=nm % >

String headless = System.getProperty("java.awt.headless");
%>
<br>
java.awt.headless: <%=headless%>
<br>
Charset: <%=java.nio.charset.Charset.defaultCharset().name()%>
<br>
<br>
<%
  try 
  {
  	InputStream in;
    String error, message;
  	String command;
  	Process process;

    command = "whoami";
    %>Command: <%= command %><br><%
    process = Runtime.getRuntime().exec(command);

    if(process.waitFor() == 0){
      in = process.getInputStream();
      message = convertStreamToString(in);             
      %>Message:  <pre><%= message%></pre><br><%          
    } else {
      %>Error when executing jar. Error code: <%= process.exitValue()%><br><%
      
      in = process.getErrorStream();              
      error = convertStreamToString(in);             
      %>Error when executing jar. Message:  <pre><%= error%></pre><br><%      
    }
    process.destroy();
    in.close();   
    
    
    command = "ps -FC java";
    %>Command: <%= command %><br><%
    process = Runtime.getRuntime().exec(command);

    if(process.waitFor() == 0){
      in = process.getInputStream();
      message = convertStreamToString(in);             
      %>Message:  <pre><%= message%></pre><br><%          
    } else {
      %>Error when executing jar. Error code: <%= process.exitValue()%><br><%
      
      in = process.getErrorStream();              
      error = convertStreamToString(in);             
      %>Error when executing jar. Message:  <pre><%= error%></pre><br><%      
    }
    process.destroy();
    in.close();   

    command = "ps -FC jsvc";
    %>Command: <%= command %><br><%
    process = Runtime.getRuntime().exec(command);

    if(process.waitFor() == 0){
      in = process.getInputStream();
      message = convertStreamToString(in);             
      %>Message:  <pre><%= message%></pre><br><%          
    } else {
      %>Error when executing jar. Error code: <%= process.exitValue()%><br><%
      
      in = process.getErrorStream();              
      error = convertStreamToString(in);             
      %>Error when executing jar. Message:  <pre><%= error%></pre><br><%      
    }
    process.destroy();
    in.close();   
    
    command = "free -m";
    %>Command: <%= command %><br><%
    process = Runtime.getRuntime().exec(command);

    if(process.waitFor() == 0){
      in = process.getInputStream();
      message = convertStreamToString(in);             
      %>Message:  <pre><%= message%></pre><br><%          
    } else {
      %>Error when executing jar. Error code: <%= process.exitValue()%><br><%
      
      in = process.getErrorStream();              
      error = convertStreamToString(in);             
      %>Error when executing jar. Message:  <pre><%= error%></pre><br><%      
    }
    process.destroy();
    in.close();
  } catch (Exception e) {
  	errorFinal = e.getMessage();
  	messageFinal = printToString(e);
  }
%>

<%= errorFinal %>
<pre><%= messageFinal %></pre>



</body>
</html>