<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="org.monet.space.kernel.components.ComponentFederation" %>
<%@page import="org.monet.space.kernel.model.Context" %>
<%@page import="org.monet.space.kernel.model.Language" %>
<%@page import="org.monet.space.kernel.model.Banner" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%
    Context context = Context.getInstance();
    long threadId = Thread.currentThread().getId();
    context.setSessionId(threadId, request.getSession().getId());
    context.setUserServerConfig(threadId, request.getServerName(), request.getContextPath(), request.getServerPort());

    Banner banner = ComponentFederation.getInstance().getDefaultLayer().loadBanner();

    final class Translator {
        private Map<String, Map<Integer, String>> translations = null;

        public Translator() {
            init();
        }

        private void init() {
            if (translations != null) return;
            translations = new HashMap<String, Map<Integer, String>>();
            translations.put("es", createSpanishMap());
            translations.put("en", createEnglishMap());
        }

        private Map<Integer, String> createSpanishMap() {
            return new HashMap<Integer, String>() {{
                put(10, "sin tareas activas"); put(11, "1 tarea activa"); put(1100, "%s tareas activas"); put(1101, "Más de 100 tareas activas");
                put(20, "sin tareas pendientes"); put(21, "1 tarea pendiente"); put(2100, "%s tareas pendientes"); put(2101, "Más de 100 tareas pendientes");
                put(3, "ir al espacio");
                put(4, "información del espacio restringida");
            }};
        }

        private Map<Integer, String> createEnglishMap() {
            return new HashMap<Integer, String>() {{
                put(10, "no active tasks"); put(11, "1 active task"); put(1100, "%s active tasks"); put(1101, "More than 100 active tasks");
                put(20, "no pending tasks"); put(21, "1 pending task"); put(2100, "%s pending tasks"); put(2101, "More than 100 pending tasks");
                put(3, "goto space");
                put(4, "space info restricted");
            }};
        }

        String formatGoToSpace() {
            return translations.get(Language.getCurrent()).get(3);
        }

        String formatActiveMessage(int count) {
            String language = Language.getCurrent();

            if (count == -1)
                return "";
            else if (count == 0)
                return translations.get(language).get(10);
            else if (count > 0 && count <= 1)
                return translations.get(language).get(11);
            else if (count <= 100)
                return String.format(translations.get(language).get(1100), count);
            else
                return translations.get(language).get(1101);
        }

        String formatAliveMessage(int count) {
            String language = Language.getCurrent();

            if (count == -1)
                return "";
            if (count == 0)
                return translations.get(language).get(20);
            else if (count > 0 && count <= 1)
                return translations.get(language).get(21);
            else if (count <= 100)
                return String.format(translations.get(language).get(2100), count);
            else
                return translations.get(language).get(2101);
        }

        String formatNoPermissions() {
            String language = Language.getCurrent();
            return translations.get(language).get(4);
        }
    }
%>
<html>
<head>
    <title><%=banner.getTitle()%></title>
    <link type="text/css" rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300">
    <link rel="shortcut icon" href="office/images/favicon.ico" type="image/x-icon"/>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8;"/>
    <meta http-equiv="refresh" content="30">

    <style>
        html {
            font-family: "Roboto",sans-serif;
        }

        body.minimal {
            margin: 0
        }

        body.card .widget {
            background-color: #FFFFFF;
            border-radius: 2px 2px 2px 2px;
            margin: 0.5rem 0 1rem;
            overflow: hidden;
            position: relative;
            box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
        }

        .hidden_true {
        	display: none;
        }

        .widget-image {
            display: none;
        }

        body.card .widget .widget-image {
            padding: 10px;
            position: relative;
            display: block;
            border-bottom: 1px solid #efefef;
        }

        .widget-image img {
            border-radius: 2px 2px 0 0;
            bottom: 0;
            left: 0;
            position: relative;
            right: 0;
            top: 0;
            max-height: 100px;
        }

        body.card .widget .widget-content {
            border-radius: 0 0 2px 2px;
            padding: 10px 20px 12px;
        }

        .widget .widget-content .title {
            display: none;
        }

        body.card .widget .widget-content .title {
            color: #212121;
            font-size: large;
            display: block;
        }

        .widget .widget-content p {
            font-size: small;
            margin: 0;
        }

        .widget .widget-content a {
            display: none;
        }

        body.card .widget .widget-content a {
            text-decoration: none;
            font-size: small;
            display: block;
        }

        .widget .widget-message {
            margin: 1px 0 10px;
        }

        .widget .widget-message div {
            font-size: small;
            color: #555;
        }

    </style>
</head>

<body class="<%=request.getParameter("view")%>">

    <div class="widget hidden_<%=banner.hasPermissions()%>">
        <div class="widget-image">
            <img class="activator" src="<%=banner.getSpaceUrl() + "/model-resource.jsp?path=images/organization.logo.png"%>">
        </div>
        <div class="widget-content">
            <span class="title"><%=banner.getTitle()%>. <%=banner.getSubTitle()%></span>
			<div class="no-permissions"><%=new Translator().formatNoPermissions()%></div>
            <a href="<%=banner.getSpaceUrl()%>"><%=new Translator().formatGoToSpace()%></a>
        </div>
    </div>

    <div class="widget hidden_<%=!banner.hasPermissions()%>">
        <div class="widget-image">
            <img class="activator" src="<%=banner.getSpaceUrl() + "/model-resource.jsp?path=images/organization.logo.png"%>">
        </div>
        <div class="widget-content">
            <span class="title"><%=banner.getTitle()%>. <%=banner.getSubTitle()%></span>
            <div class="widget-message">
                <div class="count-active-tasks"><%=new Translator().formatActiveMessage(banner.getCountActiveTasks())%></div>
                <div class="count-active-tasks"><%=new Translator().formatAliveMessage(banner.getCountAliveTasks())%></div>
            </div>
            <a href="<%=banner.getSpaceUrl()%>"><%=new Translator().formatGoToSpace()%></a>
        </div>
    </div>

</body>
</html>
<% Context.getInstance().clear(Thread.currentThread().getId()); %>