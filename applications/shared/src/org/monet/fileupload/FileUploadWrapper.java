package org.monet.fileupload;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.monet.http.HttpRequest;
import org.monet.http.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileUploadWrapper {

    private final FileUpload fileUpload;

    public FileUploadWrapper() {
        this.fileUpload = new FileUpload();
    }

    public FileUploadWrapper(FileItemFactory fileItemFactory) {
        this.fileUpload = new FileUpload(fileItemFactory);
    }

    public static final boolean isMultipartContent(Request request) {
        if (isHttpRequest(request)) return ServletFileUpload.isMultipartContent(((HttpRequest) request).raw());
        return FileUpload.isMultipartContent(contextOf(request));
    }

    public List parseRequest(Request request) throws FileUploadException {
        return this.fileUpload.parseRequest(contextOf(request));
    }

    public FileItemIterator getItemIterator(Request request) throws FileUploadException, IOException {
        return this.fileUpload.getItemIterator(contextOf(request));
    }

    private static RequestContext contextOf(final Request request) {
        if (isHttpRequest(request)) return new ServletRequestContext(((HttpRequest) request).raw());

        return new RequestContext() {
            @Override
            public String getCharacterEncoding() {
                return request.getCharacterEncoding();
            }

            @Override
            public String getContentType() {
                return request.getContentType();
            }

            @Override
            public int getContentLength() {
                return request.getContentLength();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return request.getInputStream();
            }
        };
    }

    private static boolean isHttpRequest(Request request) {
        return request instanceof HttpRequest;
    }
}