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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.common.delivery.IDeliveryService;
import org.jsmtpd.core.common.dnsService.IDNSResolver;
import org.jsmtpd.core.common.filter.FilterTreeNode;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.common.inputIPFilter.IFilterIP;
import org.jsmtpd.core.common.smtpExtension.ISmtpExtension;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Loads the plugins, configure them using reflection
 * @author Jean-Francois POUX
 * @see org.jsmtpd.core.common.PluginStore
 */
public class PluginLoader implements ErrorHandler {
    private PluginStore store = PluginStore.getInstance();

    private SAXParseException lastEx = null;
    private Document document;
    private FilterTreeNode filterRoot = null;
    private Log log = LogFactory.getLog(PluginLoader.class);

    private String xmlFileName;
    
    /**
     * Initialise : set logger and parse xml file, checking against the xsd file.
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ElementNotFoundException thrown when the xml file does not contain an element
     */
    public void init() throws SAXException, IOException, ParserConfigurationException, ElementNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);

        try {
            factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        } catch (IllegalArgumentException x) {
            log.error(x);
        }
        InputStream xsd = this.getClass().getClassLoader().getResourceAsStream("jsmtpd-plugin-config.xsd");
        if (xsd == null)
            throw new IOException("File jsmtpd-plugin-config.xsd not found, not in classpath");
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsd);

        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        builder.setErrorHandler(this);

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
        if (in == null)
            throw new IOException("File "+xmlFileName+" not found, not in classpath");

        document = builder.parse(in);

        if (lastEx != null) {
            SAXException toThrow = new SAXException(lastEx);
            lastEx = null;
            throw toThrow;
        }

    }

    public void loadDNSService() throws PluginLoaderException {
        NodeList tmp = document.getElementsByTagName("DNSSetup");
        Node dnsset = tmp.item(0);
        NamedNodeMap map = dnsset.getAttributes();
        String className = map.getNamedItem("class").getNodeValue();
        IGenericPlugin dns = loadGenericPlugin(className, dnsset);
        try {
            dns.initPlugin();
        } catch (PluginInitException e) {
            throw new PluginLoaderException("The plugin failed to initialize itsefl",e);
        }
        store.setResolver((IDNSResolver) dns);
    }

    public void loadLocalDeliveryService() throws PluginLoaderException {

        NodeList tmp = document.getElementsByTagName("LocalDeliveryService");
        Node ldsset = tmp.item(0);
        NamedNodeMap map = ldsset.getAttributes();
        String className = map.getNamedItem("class").getNodeValue();
        IGenericPlugin lds = loadGenericPlugin(className, ldsset);
        try {
            lds.initPlugin();
        } catch (PluginInitException e) {
            throw new PluginLoaderException("The plugin failed to initialize itsefl",e);
        }
        store.setLocalDeliveryService((IDeliveryService) lds);
    }

    public void loadRemoteDeliveryService() throws PluginLoaderException {

        NodeList tmp = document.getElementsByTagName("RemoteDeliveryService");
        Node rdsset = tmp.item(0);
        NamedNodeMap map = rdsset.getAttributes();
        String className = map.getNamedItem("class").getNodeValue();
        IGenericPlugin rds = loadGenericPlugin(className, rdsset);
        try {
            rds.initPlugin();
        } catch (PluginInitException e) {
            throw new PluginLoaderException("The plugin failed to initialize itsefl",e);
        }
        store.setRemoteDeliveryService((IDeliveryService) rds);
    }

    public void loadACL() throws PluginLoaderException {

        NodeList tmp = document.getElementsByTagName("ACLSetup");
        Node aclset = tmp.item(0);
        NamedNodeMap map = aclset.getAttributes();
        String className = map.getNamedItem("class").getNodeValue();
        IGenericPlugin acl = loadGenericPlugin(className, aclset);
        try {
            acl.initPlugin();
        } catch (PluginInitException e) {
            throw new PluginLoaderException("The plugin failed to initialize itsefl",e);
        }
        store.setAcl((IACL) acl);
    }

    /**
     * Builds an image of the filterchain tree specified in the xml config file,
     * and associates the logical names of filters to instances stored in pluginstore
     * @throws ModuleNotFoundException
     */
    public void loadFilterTree() throws ModuleNotFoundException {
        NodeList tmp = document.getElementsByTagName("bodyFilterTree");
        Node treeRoot = tmp.item(0);

        NodeList childs = treeRoot.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            Node flt = childs.item(i);
            if (flt.getNodeName().equals("filter")) {
                NamedNodeMap mp = flt.getAttributes();
                String logicalName = mp.getNamedItem("name").getNodeValue();

                log.debug("[tree] Loading filtree with root node [i]" + logicalName);
                IGenericPlugin plugin = (IGenericPlugin) PluginStore.getInstance().getPluginByLogicalName(logicalName);
                if (plugin instanceof IFilter) {
                    IFilter module = (IFilter) plugin;
                    filterRoot = new FilterTreeNode();
                    filterRoot.setFilter(module);
                    appendFilterTreeNode(flt, filterRoot);
                } else {
                    log.fatal("The filter " + logicalName + " is not a body filter tree plugin !");
                    throw new ModuleNotFoundException(logicalName);
                }
            }
        }
        PluginStore.getInstance().setRootFilter(filterRoot);
    }

    public void appendFilterTreeNode(Node node, FilterTreeNode parentNode) throws ModuleNotFoundException {
        NodeList childs = node.getChildNodes();
        if (childs == null)
            return;

        for (int i = 0; i < childs.getLength(); i++) {
            Node tmp = childs.item(i);
            NodeList tchilds = tmp.getChildNodes();
            if (tchilds != null) {
                for (int j = 0; j < tchilds.getLength(); j++) {
                    Node ch = tchilds.item(j);
                    if (ch.getNodeName().equals("filter")) {
                        NamedNodeMap mp = ch.getAttributes();
                        String modName = mp.getNamedItem("name").getNodeValue();
                        log.debug("[tree] adding " + modName + " as "+tmp.getNodeName()+" node to node " + parentNode.getFilter().getPluginName());
                        IGenericPlugin plugin = (IGenericPlugin) PluginStore.getInstance().getPluginByLogicalName(modName);
                        if (plugin instanceof IFilter) {
                            IFilter flt = (IFilter) plugin;
                            FilterTreeNode newNode = new FilterTreeNode();
                            newNode.setFilter(flt);
                            if (tmp.getNodeName().equals("true"))
                            	parentNode.setTrueNode(newNode);
                            else
                            	parentNode.setFalseNode(newNode);
                            newNode.setParent(parentNode);
                            appendFilterTreeNode(ch, newNode);
                        } else {
                            log.debug( "The filter " + modName + " is not a body filter tree plugin !");
                            throw new ModuleNotFoundException(modName);
                        }
                    }
                }
            }
        }
    }

    public void loadFilters() throws PluginLoaderException {
        NodeList tmp = document.getElementsByTagName("filtersetup");
        Node filterSetup = tmp.item(0);
        NodeList filters = filterSetup.getChildNodes();
        for (int i = 0; i < filters.getLength(); i++) {
            Node current = filters.item(i);
            if (current.getNodeName().equals("filterInit")) {
                NamedNodeMap map = current.getAttributes();
                String className = map.getNamedItem("class").getNodeValue();
                String logicalName = map.getNamedItem("name").getNodeValue();
                log.debug("\tTrying to load [c]" + className + " as [i]" + logicalName);
                IGenericPlugin module = loadGenericPlugin(className, current);
                try {
                    module.initPlugin();
                } catch (PluginInitException e) {
                    throw new PluginLoaderException("The plugin failed to initialize itsefl",e);
                }
                store.addPlugin(module, logicalName);
                log.debug("\tLoaded [c]" + className + " as [i]" + logicalName);
            }
        }
    }

    public void loadInputIPFilterChain() throws ModuleNotFoundException {
        NodeList tmp = document.getElementsByTagName("inputIPFilterChain");
        Node treeRoot = tmp.item(0);
        NodeList childs = treeRoot.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            Node flt = childs.item(i);
            if (flt.getNodeName().equals("ipFilter")) {
                NamedNodeMap mp = flt.getAttributes();
                String logicalName = mp.getNamedItem("name").getNodeValue();

                IGenericPlugin plugin = (IGenericPlugin) PluginStore.getInstance().getPluginByLogicalName(logicalName);
                if (plugin instanceof IFilterIP) {
                    IFilterIP module = (IFilterIP) plugin;
                    PluginStore.getInstance().addInputIPFilters(module);
                } else {
                    log.fatal("Plugin " + logicalName + "is not an input IP Filter");
                    throw new ModuleNotFoundException(logicalName);
                }
            }
        }
        PluginStore.getInstance().setRootFilter(filterRoot);
    }

    public void loadSmtpExtensions() throws PluginLoaderException {
        NodeList tmp = document.getElementsByTagName("smtpExtensions");
        Node treeRoot = tmp.item(0);
        NodeList childs = treeRoot.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            Node extension = childs.item(i);
            if (extension.getNodeName().equals("smtpExtension")) {
                NamedNodeMap mp = extension.getAttributes();
                String logicalName = mp.getNamedItem("name").getNodeValue();
                String className = mp.getNamedItem("class").getNodeValue();
                log.info("Loading SMTP Extension plugin " + logicalName + " of class " + className);
                IGenericPlugin plugin = loadGenericPlugin(className, extension);
                try {
                    plugin.initPlugin();
                } catch (PluginInitException e) {
                    throw new PluginLoaderException("The plugin failed to initialize itsefl",e);
                }
                if (plugin instanceof ISmtpExtension) {
                    ISmtpExtension module = (ISmtpExtension) plugin;
                    PluginStore.getInstance().addSmtpExtensions(module);
                    log.info("Loaded SMTP Extension plugin " + plugin.getPluginName() + " as " + logicalName);
                } else {
                    log.fatal("Plugin " + logicalName + "is not a valid smtp extension");
                    throw new PluginLoaderException("The plugin "+logicalName+" is not a valid smtp extension");
                }
            }
        }
        PluginStore.getInstance().setRootFilter(filterRoot);
    }

    private IGenericPlugin loadGenericPlugin(String className, Node pNode) throws PluginLoaderException {

        IGenericPlugin module = null;
        try {
            module = (IGenericPlugin) Class.forName(className).newInstance();
        } catch (InstantiationException e1) {
            throw new PluginLoaderException("Could not instanciate target class",e1);
        } catch (IllegalAccessException e1) {
            throw new PluginLoaderException("Illegal access",e1);
        } catch (ClassNotFoundException e1) {
            throw new PluginLoaderException("The plugin class does not exists in classpath",e1);
        }

        NodeList tmp = pNode.getChildNodes();
        for (int i = 0; i < tmp.getLength(); i++) {
            Node cur = tmp.item(i);
            if (cur.getNodeName().equals("propertyset")) {
                NamedNodeMap props = cur.getAttributes();
                setParam(module, props.getNamedItem("name").getNodeValue(), props.getNamedItem("value").getNodeValue());
            }
        }

        return module;
    }

    private void setParam(IGenericPlugin plug, String name, String value) throws PluginLoaderException {
        String rName = name.substring(0,1).toUpperCase()+name.substring(1);
        String lookup = "set" + rName;
        Method method = methodLookup(plug.getClass(), lookup);
        invoke(plug, method, value);
        log.debug("\t[set][c]:" + plug.getClass() + ", set " + name + "=" + value);
    }

    @SuppressWarnings("rawtypes")
	private Method methodLookup(Class clazz, String methodName) throws PluginLoaderException {
        Method[] meths = clazz.getDeclaredMethods();
        String ucMethodName = "set"+methodName.substring(3,4).toUpperCase() + methodName.substring(4); // replace first charg with UC.
        for (int i = 0; i < meths.length; i++) {
            Method method = meths[i];
            if (method.getName().equals(methodName) || method.getName().equals(ucMethodName)) {
                if (Modifier.isPublic(method.getModifiers())) {
                    Class[] params = method.getParameterTypes();
                    if ((params == null) || (params.length != 1))
                        throw new PluginLoaderException("Method " + method.getName() + " must have only one parameter");
                    return method;
                } else {
                    throw new PluginLoaderException("Method " + method.getName() + " is not public");
                }
            }
        }
        //Perform lookup in the parent class if exists
        Class parent = clazz.getSuperclass();
        if (parent == null)
            throw new PluginLoaderException("Method " + methodName + " not found in parent types");
        else
            return methodLookup(parent, methodName);
    }

    @SuppressWarnings("rawtypes")
	private void invoke(IGenericPlugin plug, Method meth, String value) throws PluginLoaderException {
        Class[] params = meth.getParameterTypes();
        Object[] effectiveParams = new Object[1];

        try {
            if (params[0].getName().equals("java.lang.String")) {
                effectiveParams[0] = (Object) value;
                meth.invoke(plug, effectiveParams);
                return;
            }

            if (params[0].getName().equals("int")) {
                effectiveParams[0] = new Integer(value);
                meth.invoke(plug, effectiveParams);
                return;
            }

            if (params[0].getName().equals("boolean")) {
                effectiveParams[0] = new Boolean(value);
                meth.invoke(plug, effectiveParams);
                return;
            }

            if (params[0].getName().equals("long")) {
                effectiveParams[0] = new Long(value);
                meth.invoke(plug, effectiveParams);
                return;
            }
        } catch (NumberFormatException e) {
            throw new PluginLoaderException("Could not parse the value from string to number",e);
        } catch (IllegalArgumentException e) {
            throw new PluginLoaderException ("Illegal argument",e);
        } catch (IllegalAccessException e) {
            throw new PluginLoaderException ("Illegal acces",e);
        } catch (InvocationTargetException e) {
            throw new PluginLoaderException ("Invocation target error",e);
        }
        
        throw new PluginLoaderException("This kind of parameter is not supported");
    }

    public void warning(SAXParseException exception) throws SAXException {
        lastEx = exception;
    }

    public void error(SAXParseException exception) throws SAXException {
        lastEx = exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        lastEx = exception;
    }

    public FilterTreeNode getFilterRoot() {
        return filterRoot;
    }
    public String getXmlFileName() {
        return xmlFileName;
    }
    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }
}