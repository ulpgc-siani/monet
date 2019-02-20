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

import java.util.LinkedList;
import java.util.List;

import org.jsmtpd.config.ModuleNotFoundException;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.common.delivery.IDeliveryService;
import org.jsmtpd.core.common.dnsService.IDNSResolver;
import org.jsmtpd.core.common.filter.FilterTreeNode;
import org.jsmtpd.core.common.inputIPFilter.IFilterIP;
import org.jsmtpd.core.common.smtpExtension.ISmtpExtension;

/**
 * Stores the instances of all plugins. By conveniance this is a singleton.
 * Not synchronized in any way as long as plugins are dispatched within one unique thread.
 * @author Jean-Francois POUX
 */
public class PluginStore {

    private static PluginStore instance = null;
    private IACL acl = null;
    private IDNSResolver resolver = null;
    private IDeliveryService localDeliveryService = null;
    private IDeliveryService remoteDeliveryService = null;
    private LinkedList<LoadedPlugin> plugins = new LinkedList<LoadedPlugin>();
    private FilterTreeNode rootFilter = null;
    private List<IFilterIP> inputIPFilterChain = new LinkedList<IFilterIP>();
    private List<ISmtpExtension> smtpExtensions = new LinkedList<ISmtpExtension>();

    private PluginStore() {}

    public void addPlugin(IGenericPlugin module, String logicalName) {
        LoadedPlugin pl = new LoadedPlugin(module, logicalName);
        plugins.add(pl);
    }

    public List<IGenericPlugin> getFilterList() {
        LinkedList<IGenericPlugin> fl = new LinkedList<IGenericPlugin>();
        for (LoadedPlugin loadedPlugin : plugins) {
        	fl.add(loadedPlugin.getModule());
		}
        return fl;
    }

    public IGenericPlugin getPluginByLogicalName(String logicalName) throws ModuleNotFoundException {
    	for (LoadedPlugin plugin : plugins) {
    		if (plugin.getLogicalName().equals(logicalName))
                return plugin.getModule();
		}
        throw new ModuleNotFoundException(logicalName);
    }

    public static PluginStore getInstance() {
        if (instance == null)
            instance = new PluginStore();

        return instance;
    }

    public IACL getAcl() {
        return acl;
    }

    public void setAcl(IACL acl) {
        this.acl = acl;
    }

    public IDNSResolver getResolver() {
        return resolver;
    }

    public void setResolver(IDNSResolver resolver) {
        this.resolver = resolver;
    }

    public IDeliveryService getLocalDeliveryService() {
        return localDeliveryService;
    }

    public void setLocalDeliveryService(IDeliveryService localDeliveryService) {
        this.localDeliveryService = localDeliveryService;
    }

    public IDeliveryService getRemoteDeliveryService() {
        return remoteDeliveryService;
    }

    public void setRemoteDeliveryService(IDeliveryService remoteDeliveryService) {
        this.remoteDeliveryService = remoteDeliveryService;
    }

    public FilterTreeNode getRootFilter() {
        return rootFilter;
    }

    public void setRootFilter(FilterTreeNode rootFilter) {
        this.rootFilter = rootFilter;
    }

    public List<IFilterIP> getInputIPFilters() {
        return inputIPFilterChain;
    }

    public void addInputIPFilters(IFilterIP in) {
        inputIPFilterChain.add(in);
    }

    public void addSmtpExtensions(ISmtpExtension ext) {
        smtpExtensions.add(ext);
        return;
    }

    public List<ISmtpExtension> getSmtpExtensions() {
        return smtpExtensions;
    }
}