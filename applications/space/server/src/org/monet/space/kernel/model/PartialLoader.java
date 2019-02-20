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

import java.util.EventObject;
import java.util.HashSet;

public class PartialLoader {
	private HashSet<String> loadedAttributes;
	private ILoadListener listener;
	private static Boolean partialLoading = true;

	public PartialLoader() {
		this.loadedAttributes = new HashSet<String>();
		this.listener = null;
	}

	public Boolean isPartialLoading() {
		return PartialLoader.partialLoading;
	}

	public Boolean enablePartialLoading() {
		PartialLoader.partialLoading = true;
		return true;
	}

	public Boolean disablePartialLoading() {
		PartialLoader.partialLoading = false;
		return true;
	}

	public Boolean reload(String sAttribute) {
		this.loadedAttributes.remove(sAttribute);
		return true;
	}

	public ILoadListener getLoadListener() {
		return this.listener;
	}

	public Boolean linkLoadListener(ILoadListener oListener) {
		this.listener = oListener;
		return true;
	}

	public Boolean unlinkLoadListener() {
		this.listener = null;
		return true;
	}

	public HashSet<String> getLoadedAttributes() {
		return this.loadedAttributes;
	}

	public Boolean setLoadedAttributes(HashSet<String> loadedAttributes) {
		this.loadedAttributes = loadedAttributes;
		return true;
	}

	public Boolean addLoadedAttribute(String sAttribute) {
		this.loadedAttributes.add(sAttribute);
		return true;
	}

	public Boolean removeLoadedAttribute(String sAttribute) {
		this.loadedAttributes.remove(sAttribute);
		return true;
	}

	public void onLoad(Object oSender, String sAttribute) {
		EventObject oEventObject;

		if (!PartialLoader.partialLoading) return;
		if (this.loadedAttributes.contains(sAttribute)) return;
		if (this.listener == null) return;

		oEventObject = new EventObject(oSender);
		this.listener.loadAttribute(oEventObject, sAttribute);
		this.addLoadedAttribute(sAttribute);
	}

}