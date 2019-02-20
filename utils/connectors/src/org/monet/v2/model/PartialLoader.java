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

package org.monet.v2.model;

import java.util.EventObject;
import java.util.HashSet;

public class PartialLoader {
  private HashSet<String> hsLoadedAttributes;
  private ILoadListener oListener;
  private static Boolean bPartialLoading = true;

  public PartialLoader() {
    this.hsLoadedAttributes = new HashSet<String>();
    this.oListener = null;
  }
  
  public Boolean isPartialLoading() {
    return PartialLoader.bPartialLoading;
  }
  
  public Boolean enablePartialLoading() {
    PartialLoader.bPartialLoading = true;
    return true;
  }

  public Boolean disablePartialLoading() {
    PartialLoader.bPartialLoading = false;
    return true;
  }

  public Boolean reload(String sAttribute) { 
    this.hsLoadedAttributes.remove(sAttribute);
    return true;
  }

  public ILoadListener getLoadListener() { 
    return this.oListener;
  }

  public Boolean linkLoadListener(ILoadListener oListener) { 
    this.oListener = oListener;
    return true;
  }

  public Boolean unlinkLoadListener() {
    this.oListener = null;
    return true;
  }

  public Boolean addLoadedAttribute(String sAttribute) {
    this.hsLoadedAttributes.add(sAttribute);
    return true;
  }

  public Boolean removeLoadedAttribute(String sAttribute) {
    this.hsLoadedAttributes.remove(sAttribute);
    return true;
  }

  public void onLoad(Object oSender, String sAttribute) {
    EventObject oEventObject;
    
    if (! PartialLoader.bPartialLoading) return;
    if (this.hsLoadedAttributes.contains(sAttribute)) return;
    if (this.oListener == null) return;
    
    oEventObject = new EventObject(oSender);
    this.oListener.loadAttribute(oEventObject, sAttribute);
    this.addLoadedAttribute(sAttribute);
  }
  
}