package org.monet.api.space.mailservice.impl;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.api.space.mailservice.MailServiceApi;
import org.monet.api.space.mailservice.impl.library.LibraryRestfull;
import org.monet.api.space.mailservice.impl.model.Email;

import java.net.URLEncoder;
import java.util.HashMap;

public class MailServiceApiImpl implements MailServiceApi {
    private String location;
    private String certificateFilename;
    private String certificatePassword;

    public MailServiceApiImpl(String location, String certificateFilename, String certificatePassword) {
        this.location = location;
        this.certificateFilename = certificateFilename;
        this.certificatePassword = certificatePassword;
    }

    @Override
    public boolean sendMail(Email email) {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("sendmail"));
            parameters.put("from", toStringBody(email.getFrom()));
            parameters.put("body", toStringBody(URLEncoder.encode(email.getBody(), "UTF-8")));
            parameters.put("files", toStringBody(email.getFiles().serializeToXML(), ContentType.TEXT_XML));

            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private StringBody toStringBody(String content) {
        return toStringBody(content, ContentType.TEXT_PLAIN);
    }

    private StringBody toStringBody(String content, ContentType contentType) {
        return new StringBody(content, contentType);
    }
}
