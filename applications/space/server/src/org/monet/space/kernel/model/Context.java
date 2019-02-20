/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.model;

import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.Strings;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Context extends BaseObject {
	private static Context instance;
	private String frameworkName;
	private String frameworkDir;
	private AgentSession agentSession;
	private Map<String, Map<Long, String>> caches = new HashMap<>();

	private static final int CACHE_SIZE = 10000;
	private static final String CONNECTION_TYPES_CACHE = "ConnectionTypesCache";
	private static final String SESSIONS_CACHE = "SessionsCache";
	private static final String REMOTE_HOSTS_CACHE = "RemoteHostsCache";
	private static final String APPLICATIONS_CACHE = "ApplicationsCache";
	private static final String INTERFACES_CACHE = "InterfacesCache";
	private static final String PORTS_CACHE = "PortsCache";
	private static final String DOMAINS_CACHE = "DomainsCache";
	private static final String PATHS_CACHE = "PathsCache";

	public class ContextInfo {
		public String ApplicationDir;
		public String HomeDir;
		public String ContextName;
		public String Charset;
		public Date Date;
	}

	private Context() {
		super();
		this.frameworkName = "";
		this.frameworkDir = "";
		this.agentSession = AgentSession.getInstance();
	}

	public synchronized static Context getInstance() {
		if (instance == null) {
			instance = new Context();
		}
		return instance;
	}

	public Boolean setFrameworkName(String sPath) {
		this.frameworkName = sPath;
		return true;
	}

	public String getFrameworkName() {
		return this.frameworkName;
	}

	public Boolean setFrameworkDir(String sDirectory) {
		this.frameworkDir = sDirectory;
		return true;
	}

	public String getFrameworkDir() {
		return this.frameworkDir;
	}

	public String getDatabaseConnectionType(Long idThread) {
		Map<Long, String> cache = getCache(CONNECTION_TYPES_CACHE);
		if (!cache.containsKey(idThread)) return Database.ConnectionTypes.AUTO_COMMIT;
		return cache.get(idThread);
	}

	public Boolean setDatabaseConnectionType(Long idThread, String sDBConnectionType) {
		Map<Long, String> cache = getCache(CONNECTION_TYPES_CACHE);
		cache.put(idThread, sDBConnectionType);
		return true;
	}

	public Boolean autoCommit() {
		String sDBConnectionType = this.getDatabaseConnectionType(Thread.currentThread().getId());
		return sDBConnectionType.equals(Database.ConnectionTypes.AUTO_COMMIT);
	}

	public String getIdSession(Long idThread) {
		Map<Long, String> cache = getCache(SESSIONS_CACHE);
		if (!cache.containsKey(idThread)) return null;
		return cache.get(idThread);
	}

	public Session getCurrentSession() {
		return this.getSession(Thread.currentThread().getId());
	}

	public Session getSession(Long idThread) {
		Map<Long, String> cache = getCache(SESSIONS_CACHE);
		if (!cache.containsKey(idThread)) return null;
		return this.agentSession.get(cache.get(idThread));
	}

	public Boolean setSessionId(Long idThread, String idSession) {
		Map<Long, String> cache = getCache(SESSIONS_CACHE);
		cache.put(idThread, idSession);
		return true;
	}

	public String getHost(Long idThread) {
		Map<Long, String> cache = getCache(REMOTE_HOSTS_CACHE);
		if (!cache.containsKey(idThread)) return null;
		return cache.get(idThread);
	}

	public String getApplication(Long idThread) {
		Map<Long, String> cache = getCache(APPLICATIONS_CACHE);
		if (!cache.containsKey(idThread)) return Strings.EMPTY;
		return cache.get(idThread);
	}

	public String getApplicationInterface(Long idThread) {
		Map<Long, String> cache = getCache(INTERFACES_CACHE);
		if (!cache.containsKey(idThread)) return Strings.EMPTY;
		return cache.get(idThread);
	}

	public Boolean setApplication(Long idThread, String sHost, String sApplication, String sInterface) {
		Map<Long, String> remoteHostsCache = getCache(REMOTE_HOSTS_CACHE);
		Map<Long, String> applicationsCache = getCache(APPLICATIONS_CACHE);
		Map<Long, String> interfacesCache = getCache(INTERFACES_CACHE);

		remoteHostsCache.put(idThread, sHost);
		applicationsCache.put(idThread, sApplication);
		interfacesCache.put(idThread, sInterface);

		return true;
	}

	public String getUrl(Long idThread, Boolean useSSL, Boolean bExcludePath) {
		Integer port = getPort(idThread);
		String sUrl = "";

		if (getDomain(idThread) == null)
			return null;

		if (useSSL) sUrl = "https://" + getDomain(idThread);
		else sUrl = "http://" + getDomain(idThread) + ((port != 80) ? ":" + port : "");

		if (!bExcludePath) sUrl += getPath(idThread);

		return sUrl;
	}

	public String getDomain(Long idThread) {
		Map<Long, String> cache = getCache(DOMAINS_CACHE);
		if (!cache.containsKey(idThread)) return null;
		return cache.get(idThread);
	}

	public String getPath(Long idThread) {
		Map<Long, String> cache = getCache(PATHS_CACHE);
		if (!cache.containsKey(idThread)) return null;
		return cache.get(idThread);
	}

	public Integer getPort(Long idThread) {
		Map<Long, String> cache = getCache(PORTS_CACHE);
		if (!cache.containsKey(idThread)) return null;
		return Integer.valueOf(cache.get(idThread));
	}

	public Boolean setUserServerConfig(Long idThread, String sDomain, String sPath, Integer iPort) {
		Map<Long, String> domainsCache = getCache(DOMAINS_CACHE);
		Map<Long, String> pathsCache = getCache(PATHS_CACHE);
		Map<Long, String> portsCache = getCache(PORTS_CACHE);

		try {
			domainsCache.put(idThread, sDomain);
			pathsCache.put(idThread, sPath);
			portsCache.put(idThread, String.valueOf(iPort));
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return true;
	}

	public Boolean clear(Long idThread) {
		return true; // Controlled by a weak map implementation
	}

	public ContextInfo getInfo() {
		ContextInfo contextInfo = new ContextInfo();

		try {
			contextInfo.ApplicationDir = new File(this.getFrameworkDir()).getCanonicalPath();
			contextInfo.HomeDir = new File(".").getCanonicalPath();
		} catch (IOException e) {
		}

		contextInfo.ContextName = this.getFrameworkName();
		contextInfo.Charset = java.nio.charset.Charset.defaultCharset().name();
		contextInfo.Date = new Date();

		return contextInfo;
	}

	public String getVersionFilename() {
		return this.getFrameworkDir() + "/WEB-INF/version.txt";
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	private Map<Long, String> getCache(String name) {
		if (!this.caches.containsKey(name))
			this.caches.put(name, createCache(name));
		return this.caches.get(name);
	}

	private Map<Long, String> createCache(String name) {
		return Collections.synchronizedMap(new LRUCache<Long, String>(CACHE_SIZE));
	}

	private class LRUCache<A, B> extends LinkedHashMap<A, B> {
		private final int maxEntries;

		public LRUCache(final int maxEntries) {
			super(maxEntries + 1, 1.0f, true);
			this.maxEntries = maxEntries;
		}

		@Override
		protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
			return super.size() > maxEntries;
		}
	}

}
