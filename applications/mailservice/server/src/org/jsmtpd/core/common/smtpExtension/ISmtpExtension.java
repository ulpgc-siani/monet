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

import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.InputSizeToBig;

/**
 * A Smtp extension is a plugin that will add or change normal smtp behavior.
 * smtpPreTrigger method is invoked for every extension loaded, before usual behavior (to change something existing)
 * smtpTrigger method is invoked after the normal chat (to add new smtp command support)
 * 
 * @author Jean-Francois POUX
 */
public interface ISmtpExtension extends IGenericPlugin {

    /**
     * Invoked after normal chat, if the main smtp implementation does not known what to do.
     * Return true if passed command is handled, false otherwise. 
     * Example of extension relying on this : TLS and authenticator.
     * When parsing an unkown command, the engine will pass it to this method
     * ! Use a streamparser from IO package to ensure integrity of what is read.
     * @param command
     * @param protocol
     * @return true if command has been handled
     */
    public boolean smtpTrigger(String command, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, BareLFException;

    /**
     * Exposed during HELO chat
     * @return the string to expose during HELO Chat
     */
    public String getWelcome();
    
    /**
     * Overide normal behavior.
     * It's executed before normal chat. Return true if handled, false otherwise.
     * It is used by SPF plugin, by example, to override default RCPT command in some cases. 
     * @param command
     * @param protocol
     * @return false : continue with normal behavior ; true : extension overide normal behavior.
     * @throws SmtpExtensionException
     * @throws IOException
     * @throws InputSizeToBig
     * @throws BareLFException
     */
    public boolean smtpPreTrigger (String command, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, BareLFException;
}