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

package org.jsmtpd;

/**
 * Simply starts an instance of Controler
 * @author Jean-Francois POUX
 */
public class Launcher {

    public static void main(String[] args) {
        try {
            GetOpt options = new GetOpt (args);
            Controler controler = new Controler();
            if (options.containsKey("xmlPluginFile"))
                controler.setXmlPluginFileName((String)options.get("xmlPluginFile"));
            if (options.containsKey("configFile"))
                controler.setConfigFile((String)options.get("configFile"));
            controler.startup();
        } catch (OptException e) {
            System.out.println("Sorry, can't startup Jsmtpd. I do not understaind the command line options you passed me");
            System.out.println("Please use a '-optionName value' syntax");
        }
        
    }
}