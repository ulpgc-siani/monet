<%@page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.monet.space.kernel.model.BusinessUnit" %>
<%@ page import="org.monet.space.kernel.model.Context" %>
<%@ page import="org.monet.space.kernel.model.Language" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%
  Context.getInstance().setSessionId(Thread.currentThread().getId(), request.getSession().getId());
  Language.fillCurrentLanguage(request);
  String label = BusinessUnit.getInstance().getLabel();
  String accept_language = Language.getCurrent();

  Map<String, Map> aLanguages = new HashMap<String, Map>();
  Map<String, String> aMessages_es = new HashMap<String, String>();
  Map<String, String> aMessages_en = new HashMap<String, String>();

  if (label.isEmpty())
    label = "Monet";

  aMessages_es.put("BUSINESS_UNIT_STOPPED", label + " est&aacute; parado");
  aMessages_es.put("BUSINESS_UNIT_SERVICES_STOPPED", "Todos los servicios de " + label + " est&aacute;n deshabilitados");

  aMessages_en.put("BUSINESS_UNIT_STOPPED", label + " is stopped");
  aMessages_en.put("BUSINESS_UNIT_SERVICES_STOPPED", "All " + label + " services are disabled");

  aLanguages.put("es", aMessages_es);
  aLanguages.put("en", aMessages_en);
%>
<html>
<head>
  <title><%=label%>
  </title>
  <link rel="shortcut icon" href="../images/favicon.ico" type="image/x-icon"/>

  <style type="text/css">
    BODY {
      margin: 0;
      padding: 0;
      font-family: "Trebuchet MS", Tahoma, Arial, Serif;
    }

    INPUT {
      font-size: 25pt;
      height: 80px;
      width: 300px;
      cursor: pointer;
    }

    TD.TOP {
      background: white;
      color: white;
      font-family: Tahoma;
      font-size: 8pt;
      padding: 3px;
      border-bottom: 1px solid Black;
    }

    TD.TOP .LOGO {
      float: left;
      margin: 0px 15px;
    }

    TD.BOTTOM .MESSAGE {
      font-size: 18pt;
      margin: 20px 20px 0px;
      color: #1D4191;
    }

    TD.BOTTOM .INFO {
      color: #666;
      font-size: 10pt;
      margin-bottom: 20px;
    }

    TD.BOTTOM .RESOURCES {
      border: 1px solid #1D4191;
      margin-top: 20px;
      text-align: left;
      width: 400px;
      font-size: 11pt;
    }

    TD.BOTTOM .RESOURCES .LABEL {
      background-color: #1D4191;
      color: white;
      padding: 5px;
    }

    TD.BOTTOM .RESOURCES SPAN {
      font-size: 11pt;
      font-weight: bold;
    }

    TD.BOTTOM .RESOURCES TR {
      height: 30px;
    }

    TD.BOTTOM .RESOURCES SELECT {
      width: 265px;
      cursor: pointer;
    }
  </style>

</head>

<body>

<table cellpadding="0" cellspacing="0" width="100%" height="95%">
  <tr>
    <td class="TOP">
      <img class="LOGO" src="../images/logo.gif">
    </td>
  </tr>
  <tr>
    <td width="100%" height="100%" align="center" class="BOTTOM">
      <div class="MESSAGE"><%= aLanguages.get(accept_language).get("BUSINESS_UNIT_STOPPED") %>
      </div>
      <div class="INFO"><%= aLanguages.get(accept_language).get("BUSINESS_UNIT_SERVICES_STOPPED")%>
      </div>
    </td>
  </tr>
</table>

</body>
</html>
<% Context.getInstance().clear(Thread.currentThread().getId()); %>