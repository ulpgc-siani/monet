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

package org.monet.space.kernel.agents;

import org.monet.space.kernel.listeners.IListenerPushService;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.PushEvent;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class AgentPushServiceNotifier {
	private static AgentPushServiceNotifier instance;
	LinkedHashMap<String, IListenerPushService> listeners;
	Context context;
	HashSet<Long> disabledThreads;

	protected AgentPushServiceNotifier() {
		this.listeners = new LinkedHashMap<String, IListenerPushService>();
		this.context = Context.getInstance();
		this.disabledThreads = new HashSet<Long>();
	}

	public synchronized static AgentPushServiceNotifier getInstance() {
		if (instance == null) {
			instance = new AgentPushServiceNotifier();
		}
		return instance;
	}

	public Boolean register(String code, Class<?> listenerClass) {
		IListenerPushService listenerInstance = null;

		try {
			Constructor<?> oConstructor = listenerClass.getConstructor();
			listenerInstance = (IListenerPushService) oConstructor.newInstance();
			this.listeners.put(code, listenerInstance);
		} catch (Exception ex) {
			AgentLogger.getInstance().error(ex);
			return false;
		}

		return true;
	}

	public IListenerPushService get(String code) {
		return this.listeners.get(code);
	}

	public Boolean register(String code, IListenerPushService listener) {
		this.listeners.put(code, listener);
		return true;
	}

	public void enable(Long idThread) {
		this.disabledThreads.remove(idThread);
	}

	public void disable(Long idThread) {
		this.disabledThreads.add(idThread);
	}

	public Boolean notify(PushEvent event) {
		Iterator<String> iter = this.listeners.keySet().iterator();
		IListenerPushService listener;

		if (this.disabledThreads.contains(Thread.currentThread().getId())) return true;

		while (iter.hasNext()) {
			listener = this.listeners.get(iter.next());
			if (event.getCode().equals(PushEvent.REMOVE_CLIENT)) listener.removeClient(event);
		}

		return true;
	}

}