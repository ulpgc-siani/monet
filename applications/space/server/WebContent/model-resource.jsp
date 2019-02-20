<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.monet.space.kernel.model.*" %>
<%@ page import="org.monet.space.kernel.exceptions.DataException" %>
<%@ page import="org.monet.space.kernel.constants.Strings" %>
<%@ page import="org.monet.space.kernel.agents.AgentFilesystem" %>
<%@ page import="org.monet.space.kernel.utils.MimeTypes" %>
<%
    Context context = Context.getInstance();
    String filename = request.getParameter("path");
    BusinessModel businessModel = BusinessUnit.getInstance().getBusinessModel();
    String absoluteFilename = businessModel.getAbsoluteFilename(filename);
    MimeTypes mimeTypes = MimeTypes.getInstance();
    String formatCode = mimeTypes.getFromFilename(filename);
    long threadId = Thread.currentThread().getId();
    String content = null;

    context.setSessionId(threadId, request.getSession().getId());
    context.setUserServerConfig(threadId, request.getServerName(), request.getContextPath(), request.getServerPort());

    if (formatCode == null)
        throw new DataException("format unknown!", "format unknown!");

    filename = filename.replace(Strings.BAR45, Strings.UNDERLINED);

    try {
        response.setContentType(formatCode);
        response.setHeader("Content-Disposition", "inline; filename=" + filename);

        if (absoluteFilename != null)
            response.getOutputStream().write(AgentFilesystem.getBytesFromFile(absoluteFilename));
        else
            response.getOutputStream().write(content.getBytes("UTF-8"));

        response.getOutputStream().flush();
        response.getOutputStream().close();
    } catch (Exception exception) {
        throw new DataException("model resource not found", exception.getMessage());
    }
%>
<% Context.getInstance().clear(Thread.currentThread().getId()); %>