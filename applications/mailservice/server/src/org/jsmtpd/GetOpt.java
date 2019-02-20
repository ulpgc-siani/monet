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

import java.util.HashMap;



/**
 * @author Jean-Francois POUX
 */
public class GetOpt extends HashMap<String,String> {
    
    public GetOpt (String[] args) throws OptException {
        if (args==null)
            return;
        String key=null;
        for (int i = 0; i < args.length; i++) {
            String parsed = args[i];
            if (parsed.startsWith("-")) {
                key = parsed.replaceAll("-","");
            } else {
                if (key==null)
                    throw new OptException ("Invalid command line arguments. Use -optionName value");
                this.put(key,parsed);
                key=null;
            }
            
        }
    }
}
