/*
 * 
 * Jsmtpd, Java SMTP daemon
 * Copyright (C) 2005  Jean-Francois POUX, jf.poux@laposte.net
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
package org.jsmtpd.core.common.smtpExtension;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.jsmtpd.core.mail.Email;

/**
 * @author Jean-Francois POUX
 */
public interface IProtocolHandler {

    public void setRelayed(boolean value);
    
    public boolean isRelayed();

    public Socket getSock();

    public void setSock(Socket sock) throws IOException;
    
    public int getErrorCount();
    
    public boolean isSecured();

    public void setSecured(boolean secured);

    public List<String>getCommandHistory();

    public void addCommandHistory(String command);
    
    public void setAuthContext(String context);
    
    public Email getMail ();
}