/*
 *
 * Copyright (C) 2005  Pierre-Alexandre Losson, plosson@users.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.jsmtpd.plugins.filters.snippets;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.tools.ByteArrayTool;

/**
 * This filter adds a signature to mail bodies
 * 
 * @author Pierre-Alexandre Losson, plosson@users.sourceforge.net
 * 
 * jfp : format to jsmtpd coding style, added logs, relative path in patterns fix.
 * 
 *
 * 
 */
public class ReplaceSnippetFilter implements IFilter {

    protected static final int BUFFER_SIZE = 5 * 1024; // 5k
    protected static final String CR_LF = "\r\n";
    protected static final String SNIPPET_RE = "[A-Za-z0-9]+";
    private Log log = LogFactory.getLog(ReplaceSnippetFilter.class);

    private String suffix;
    private String prefix;
    private String path;
    private Pattern pattern;
    private String encoding = "utf-8";
    
    public ReplaceSnippetFilter() {
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void initPlugin() throws PluginInitException {
        pattern = Pattern.compile(prefix + "(" + SNIPPET_RE + ")" + suffix, Pattern.MULTILINE);
        log.debug("Filter initialised with pattern " + pattern.pattern());
    }

    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException {
        try {
            MimeMessage part = new MimeMessage(null, new ByteArrayInputStream(input.getDataAsByte()));
            replaceSnippets(input.getFrom().getUser(), part);
            part.saveChanges();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            part.writeTo(bos);
            input.setDataBuffer(ByteArrayTool.crlfFix(bos.toByteArray()));
        } catch (Exception e) {
            log.error("An error occured", e);
        }
        return true;
    }

    private boolean replaceSnippets(String user, MimePart part) throws MessagingException, IOException {
        if (part.isMimeType("text/plain")) {
            addToText(user, part);
            return true;
        } else if (part.isMimeType("text/html")) {
            addToHTML(user, part);
            return true;
        } else if (part.isMimeType("multipart/mixed")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            MimeBodyPart firstPart = (MimeBodyPart) multipart.getBodyPart(0);
            boolean isFooterAttached = replaceSnippets(user, firstPart);
            //We have to do this because of a bug in JavaMail (ref id 4404733)
            part.setContent(multipart);
            return isFooterAttached;
        } else if (part.isMimeType("multipart/alternative")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int count = multipart.getCount();
            boolean isFooterAttached = false;
            for (int index = 0; index < count; index++) {
                MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(index);
                isFooterAttached |= replaceSnippets(user, mimeBodyPart);
            }
            //We have to do this because of a bug in JavaMail (ref id 4404733)
            part.setContent(multipart);
            return isFooterAttached;
        } else {
            //Give up... we won't attach the footer to this MimePart
            log.error("Can't attach signature to mail ");
            return false;
        }
    }

    public String getPluginName() {
        return "Replace Snippet Filter";
    }

    public void shutdownPlugin() {
    }

    /**
     * Prepends the content of the MimePart as HTML to the existing footer
     *
     * @param part the MimePart to attach
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     */
    protected void addToHTML(String user, MimePart part) throws MessagingException, IOException {
        String content = part.getContent().toString();
        part.setContent(replaceSnippets(user, content, "text/html"), part.getContentType());
    }

    protected void addToText(String user, MimePart part) throws MessagingException, IOException {
        String content = part.getContent().toString();
        part.setText(replaceSnippets(user, content, "text/plain"),encoding);
    }

    protected String replaceSnippets(String user, String content, String contentType) {
        Matcher m = pattern.matcher(content);
        Set<String> snippets = new HashSet<String>();
        while (m.find()) {
            String match = m.group(1);
            if (hasSnippet(user, match, contentType)) {
                snippets.add(match);
            }
        }

        Iterator<String> it = snippets.iterator();
        String newContent = content;
        while (it.hasNext()) {
            String match = it.next();
            String pattern = prefix + match + suffix;
            String replace = getSnippet(user, match, contentType);
            if (replace != null) {
                newContent = Pattern.compile(pattern, Pattern.MULTILINE).matcher(newContent).replaceAll(replace);
            } else {
                log.error("Could not replace content for snippet" + match + " in " + contentType + " for user " + user);
                return content;
            }
        }
        return newContent;
    }

    public boolean hasSnippet(String user, String pattern, String contentType) {
        
        if (pattern.contains("../"))
            return false;
        
        String snippetPath = getSnippetPath(user, pattern, contentType);
        boolean result = new File(snippetPath).exists();
        if (!result) {
            log.debug("Signature " + snippetPath + " not found");
        }
        return result;
    }

    public String getSnippet(String user, String pattern, String contentType) {
        StringBuffer content = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(getSnippetPath(user, pattern, contentType)));
            String str;
            while ((str = in.readLine()) != null) {
                content.append(str);
                content.append(CR_LF);
            }
            in.close();
        } catch (IOException e) {
            log.error("Error getting snippet content", e);
            return null;
        }
        return content.toString();
    }

    private String getSnippetPath(String user, String pattern, String contentType) {
        String snippet = path + File.separator + user + File.separator + pattern;
        if (contentType.equals("text/html"))
            snippet = snippet + ".html";
        else
            snippet = snippet + ".txt";
        return snippet;
    }

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
