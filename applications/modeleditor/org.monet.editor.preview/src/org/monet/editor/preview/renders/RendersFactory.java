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

package org.monet.editor.preview.renders;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.monet.editor.preview.model.Dictionary;
import org.monet.metamodel.CatalogDefinition;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.CubeDefinition;
import org.monet.metamodel.DesktopDefinition;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;

public class RendersFactory {
  private static RendersFactory   instance;
  private HashMap<String, Object> renders;
  
  private static final String PAGE_MODE = "page";
  private static final String DATA_MODE = "data";

  private RendersFactory() {
    this.renders = new HashMap<String, Object>();
    this.register("dictionary_view", DictionaryRender.class);
    this.register("dictionary_page", DictionaryRender.class);
    this.register("desktop_view", DesktopViewRender.class);
    this.register("desktop_page", DesktopPageRender.class);
    this.register("desktop_data", DataRender.class);
    this.register("container_view", ContainerViewRender.class);
    this.register("container_page", ContainerPageRender.class);
    this.register("container_data", DataRender.class);
    this.register("collection_view", CollectionViewRender.class);
    this.register("collection_page", CollectionPageRender.class);
    this.register("collection_data", CollectionDataRender.class);
    this.register("form_view", FormViewRender.class);
    this.register("form_page", FormPageRender.class);
    this.register("form_data", DataRender.class);
    this.register("field_view", FieldViewRender.class);
    this.register("document_view", DocumentViewRender.class);
    this.register("document_page", DocumentPageRender.class);
    this.register("document_data", DocumentDataRender.class);
    this.register("catalog_view", CatalogViewRender.class);
    this.register("catalog_page", CatalogPageRender.class);
    this.register("catalog_data", DataRender.class);
    this.register("cube_page", CubePageRender.class);
    this.register("cube_view", CubeViewRender.class);
    this.register("cube_data", DataRender.class);
  }

  public synchronized static RendersFactory getInstance() {
    if (instance == null) instance = new RendersFactory();
    return instance;
  }

  public PreviewRender get(Object object, String template, String language) {
    Class<?> renderClass;
    PreviewRender render = null;
    String code = "";
    String mode = "";
    
    if (object instanceof Dictionary) code = "dictionary";
    else if (object instanceof DesktopDefinition) code = "desktop";
    else if (object instanceof ContainerDefinition) code = "container";
    else if (object instanceof FormDefinition) code = "form";
    else if (object instanceof CollectionDefinition) code = "collection";
    else if (object instanceof FieldProperty) code = "field";
    else if (object instanceof CatalogDefinition) code = "catalog";
    else if (object instanceof DocumentDefinition) code = "document";
    else if (object instanceof CatalogDefinition) code = "catalog";
    else if (object instanceof CubeDefinition) code = "cube";

    if (code.isEmpty()) return null;
    
    if (template.indexOf(PAGE_MODE) != -1) mode = "page";
    else if (template.indexOf(DATA_MODE) != -1) mode = "data";
    else mode = "view";
    
    code += "_" + ((mode != null)?mode:"");

    try {
      renderClass = (Class<?>)this.renders.get(code);
      Constructor<?> constructor = renderClass.getConstructor(String.class);
      render = (PreviewRender)constructor.newInstance(language);
      render.setTarget(object);
      render.setTemplate(template);
    } catch (NullPointerException exception) {
      System.out.println(exception);
    } catch (Exception exception) {
      System.out.println(exception);
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
