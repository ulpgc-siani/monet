package org.monet.space.applications.servlets;

import org.apache.commons.io.IOUtils;
import org.monet.space.applications.OutputMessage;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.utils.MimeTypes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class HttpOutputMessage implements OutputMessage {
    protected final HttpServletResponse response;

    public HttpOutputMessage(HttpServletResponse response) {
        this.response = response;
    }

    public void write(String content) {
        try {
            final PrintWriter out = response.getWriter();

            response.setHeader("Access-Control-Allow-Origin", "*"); //$NON-NLS-1$ //$NON-NLS-2$
            response.setContentType("text/plain"); //$NON-NLS-1$
            response.setCharacterEncoding("utf-8"); //$NON-NLS-1$

            out.println(content);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(File file) {
        try {
            InputStream content = getInputStream(file);
            this.response.setContentType(MimeTypes.getInstance().getFromFile(file));
            this.response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            IOUtils.copy(content, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getInputStream(File file) throws IOException {
        return AgentFilesystem.getInputStream(file.getAbsolutePath());
    }

}
