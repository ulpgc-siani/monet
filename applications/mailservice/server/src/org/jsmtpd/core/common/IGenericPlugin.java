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
package org.jsmtpd.core.common;

/**
 * Any plugins must implement this interface
 * plugins may be used from multiple threads,
 * depending on your configuration.<br>
 * 
 * <b>You have to ensure thread safety</b><br>
 * 
 * Methods are not marked synchronized
 * because trivial plugins may not need it.
 * <br><br>
 * <b>Plugin format</b>
 * <br>
 * Plugins are configured via the etc/plugin-config.xml file.<br>
 * Each propertyset element is applied to the plugin<br>
 * The plugin loader expects to find a plugin public method corresponding to the name attribute of the propertyset.
 * The name is case sensitive, (first letter uppercase, fo java conventions).<br><br>
 * 
 * Example :<br>
 *	<pre>
 *		&ltACLSetup name="Basic ACL" class="org.jsmtpd.plugins.acls.SimpleACL"&gt
 *			&ltpropertyset name="RelayedHosts" value="127.0.0.1,localhost,172.16.0.20,172.16.0.23"&gt&lt/propertyset&gt
 *			&ltpropertyset name="ValidUsers" value="*@localhost,*@mail.taldius.ath.cx"&gt&lt/propertyset&gt
 *		&lt/ACLSetup&gt
 *	 </pre>
 *
 *	Will load the class "org.jsmtpd.plugins.acls.SimpleACL" under the name "Basic ACL" <br>
 *	It will then invoque the method setRelayedHosts with the string parameter "127.0.0.1,localhost,172.16.0.20,172.16.0.23" <br>
 * 
 * @see org.jsmtpd.plugins.acls.SimpleACL
 * @see org.jsmtpd.config.PluginLoader
 * @author Jean-Francois POUX
 *
 */
public interface IGenericPlugin {
    /**
     * @return this plugin's name
     */
    public String getPluginName();

    /**
     * Perform initialisation of the plugin. 
     * if an error
     * occurs, the plugin is not functional
     * @throws PluginInitException
     */
    public void initPlugin() throws PluginInitException;
    /**
     * Invoked on server shutdown 
     */
    public void shutdownPlugin();

}