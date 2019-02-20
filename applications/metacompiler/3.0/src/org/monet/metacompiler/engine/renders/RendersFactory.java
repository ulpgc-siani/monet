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

package org.monet.metacompiler.engine.renders;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.monet.metacompiler.engine.MetaModelTranslator.TranslateMode;

public class RendersFactory {
  private static RendersFactory   instance;
  private HashMap<String, Object> renders;
  
  private RendersFactory() {
    this.renders = new HashMap<String, Object>();
    this.register(TranslateMode.html.toString(), HtmlRender.class);
    this.register(TranslateMode.manifest_java.toString(), ManifestSerializerRender.class);
    this.register(TranslateMode.core_java.toString(), ModelSerializerRender.class);
    this.register(TranslateMode.editor_java.toString(), ModelSerializerRender.class);
    this.register(TranslateMode.editor_gramatic.toString(), EditorGramaticRender.class);
    this.register(TranslateMode.editor_structure.toString(), EditorStructureRender.class);
  }

  public synchronized static RendersFactory getInstance() {
    if (instance == null) instance = new RendersFactory();
    return instance;
  }

  public MetaModelRender get(String mode) {
    Class<?> renderClass;
    MetaModelRender render = null;
   
    try {
      renderClass = (Class<?>)this.renders.get(mode);
      Constructor<?> constructor = renderClass.getConstructor();
      render = (MetaModelRender)constructor.newInstance();
    } catch (NullPointerException oException) {
      System.out.printf("ERROR. No se ha encontrado el render para " + mode + "\n");
    } catch (Exception oException) {
      System.out.printf("ERROR. No se ha encontrado el render para " + mode + "\n");
    }

    return render;
  }

  public Boolean register(String code, Class<?> renderClass) throws IllegalArgumentException {

    if ((renderClass == null) || (code == null)) {
      return false;
    }
    this.renders.put(code, renderClass);

    return true;
  }

}
