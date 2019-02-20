<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Info</title>
</head>
<body>
<%@ page import="java.io.*" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.lang.management.*" %>
<%@ page import="java.util.Iterator" %>

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

  boolean isWindows()  {
     return System.getProperty("os.name").startsWith("Windows");
  };
   
%>

<%
  String currentDir = new File(this.getServletConfig().getServletContext().getRealPath("/")).getCanonicalPath();
  String contextName = this.getServletConfig().getServletContext().getServletContextName();
  String homeDir = System.getProperty("user.home");

  File f = new File(currentDir + "/WEB-INF/version.txt");
  if (f.exists()) {
    out.print("Version file: <br>");
    InputStream in = new FileInputStream(f);
    InputStreamReader is = new InputStreamReader(in);
    BufferedReader br = new BufferedReader(is);
    String read = br.readLine();


%>
<xmp><%
  while (read != null) {
    out.println(read);
    read = br.readLine();
  }

  br.close();
  is.close();
  in.close();
%></xmp>
<%
  } else {
    out.print("Version file: Error, file not exists.<br>");
  }


  f = new File(homeDir + "/." + contextName + "/businessmodel/MANIFEST");
  if (f.exists()) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    out.println("Model upload date: " + sdf.format(f.lastModified()) + "<br>");

    out.print("Model file: <br>");
    InputStream in = new FileInputStream(f);
    InputStreamReader is = new InputStreamReader(in);
    BufferedReader br = new BufferedReader(is);
    String read = br.readLine();


%>
<xmp><%
  while (read != null) {
    out.println(read);
    read = br.readLine();
  }

  br.close();
  is.close();
  in.close();
%></xmp>
<%
  } else {
    out.print("Model file: Error, file not exists.<br>");
  }

  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  Date date = new Date();
  String currentDate = dateFormat.format(date);
%>
Application dir: <%=currentDir%><br>
Home dir: <%=homeDir%><br>
Context name: <%=contextName%><br>
Charset: <%=java.nio.charset.Charset.defaultCharset().name()%><br>
Current date: <%=currentDate%><br>
Current timezone: <%=java.util.TimeZone.getDefault().getID()%>
<br>
<%
  Iterator<MemoryPoolMXBean> iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
  while (iter.hasNext())
  {
    MemoryPoolMXBean item = iter.next();
    String name = item.getName();
    MemoryType type = item.getType();
    MemoryUsage usage = item.getUsage();
    MemoryUsage peak = item.getPeakUsage();
    MemoryUsage collections = item.getCollectionUsage();

    if (name.equals("Perm Gen")) {
      out.println("<br>-------------------------------");
      out.println("<br>Memory: " + name);
      out.println("<br>Type: " + type);
      out.println("<br>Max: " + usage.getMax() /1024/1024 + "MB");
      out.println("<br>Usage: " + usage.getUsed() /1024/1024 + "MB");
      out.println("<br>Free: " + (usage.getMax()-usage.getUsed()) /1024/1024 + "MB");
    }
  }

  out.println("<br>"); 

  if (! isWindows()) { 
    InputStream in;
    String error, message;
    String command;
    Process process;

    command = "openssl x509 -in "+homeDir+"/."+contextName+"/certificates/businessunit-"+contextName+".crt -noout -text";
    process = Runtime.getRuntime().exec(command);

    if(process.waitFor() == 0){
      in = process.getInputStream();
      message = convertStreamToString(in);             
      %><pre><%= message%></pre><br><%          
    } else {
      %>Error when executing jar. Error code: <%= process.exitValue()%><br><%      
      in = process.getErrorStream();              
      error = convertStreamToString(in);             
      %>Error when executing jar. Message:  <pre><%= error%></pre><br><%      
    }
    process.destroy();
    in.close();   
  }  
%>
<br>
</body>
</html>
