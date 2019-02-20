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

package org.monet.v2.model.constants;

public abstract class Common {
  
  public static final Integer NO_DEPTH = -1;
  public static final String DEFAULT_LANGUAGE = "es";

  public abstract class ModelDefinition {
    public static final String FORM       = "form";
    public static final String COLLECTION = "collection";
    public static final String CLASSIFIER = "classifier";
  }

  public abstract class FileExtensions {
    public static final String HTML           = ".html";
    public static final String XML            = ".xml";
    public static final String XSL            = ".xsl";
    public static final String LABELS         = ".labels.lang";
    public static final String MESSAGES       = ".messages.lang";
    public static final String ERROR_MESSAGES = ".errors.lang";
    public static final String DEFINITION     = ".xml";
  }

  public abstract class Suffix {
    public static final String VIEW_TITLE    = "_TITLE";
    public static final String ARTIFICIAL_ID = "_ART_ID";
  }

  public abstract class UserStatus {
    public static final String PENDING  = "-1";
    public static final String ENABLED  = "1";
    public static final String DISABLED = "0";
  }
  
  public abstract class Booleans {
    public static final String TRUE  = "1";
    public static final String FALSE = "0";
  }

  public abstract class Genders {
    public static final String MALE   = "male";
    public static final String FEMALE = "female";
  }

  public abstract class ObjectProperty {
    public static final String ID    = "id";
    public static final String ITEMS = "Items";
  }

  public abstract class Lengths {
    public static final int SHORT_LABEL   = 40;
    public static final int SHORT_SECTION = 40;
    public static final int SHORT_DESCRIPTION = 100;
  }

  public abstract class Codes {
    public static final String DUMMY                            = "dummy";
    public static final String TEMPLATE_MODEL_ROOT              = "model";
    public static final String TEMPLATE_MODEL_SELECT_NODE_TYPES = "selectnodetypes";
    public static final String LOG_SUBSCRIBER                   = "logsubscriber";
    public static final String SERVICES_LISTENER                = "serviceslistener";
    public static final String WAIT_TASK_LISTENER               = "waittasklistener";
    public static final String NEWS_POST_LISTENER               = "newspostlistener";
    public static final String BPI                              = "bpi";
  }

  public abstract class Pages {
    public static final String EXTENSION_HELPER_INDEX_NAME = "index";
  }

  public abstract class Sorting {
    public static final String ATTRIBUTE = "attribute";
    public static final String MODE      = "mode";
  }

  public abstract class OrderMode {
		public static final String ASCENDANT  = "ascendant";
		public static final String DESCENDANT = "descendant";
  }

  public abstract class ViewModePrefix {
    public static final String VIEW  = "view.";
    public static final String EDIT  = "edit.";
  }
  
  public abstract class DataStoreField {
    public static final String CODE  = "code";
    public static final String VALUE = "value";
    public static final String BODY  = "body";
  }
  
}