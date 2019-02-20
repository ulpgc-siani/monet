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
package org.jsmtpd.config;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Loads the config file<br>
 * Should be initialised in thread safe environnement<br>
 * @author Jean-Francois POUX
 */
public class ReadConfig extends Properties {
    private static ReadConfig instance = null;
    private static Log log = LogFactory.getLog(ReadConfig.class);
	private ReadConfig() {}

    public static ReadConfig getInstance() {
        if (instance == null)
            instance = new ReadConfig();
        return instance;
    }

    public void loadConfig(String configFile) throws ConfigErrorException {
        try {
            this.load(new FileInputStream(configFile));
        } catch (Exception e) {
            throw new ConfigErrorException(e);
        }
    }

    public int getIntegerProperty (String name) {
    	if (getProperty(name)==null)
    		log.error ("Property "+name+" is missing from jsmtpd.ini");
    	return Integer.parseInt(getProperty(name));
    }
    
    public boolean getBooleanProperty (String name) {
    	if (getProperty(name)==null)
    		log.error ("Property "+name+" is missing from jsmtpd.ini");
    	return Boolean.parseBoolean(getProperty(name));
    }
}