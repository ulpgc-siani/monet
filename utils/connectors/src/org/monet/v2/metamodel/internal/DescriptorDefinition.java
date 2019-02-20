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

package org.monet.v2.metamodel.internal;

import org.monet.v2.metamodel.ReferenceDefinition;



import org.monet.v2.metamodel.ReferenceDefinition;

import org.monet.v2.metamodel.AttributeDeclaration;
import org.monet.v2.metamodel.ReferenceDefinition;

public class DescriptorDefinition extends ReferenceDefinition {
  
  public static final String ATTRIBUTE_ID_NODE      = "id_node";
  public static final String ATTRIBUTE_EDITABLE     = "editable";
  public static final String ATTRIBUTE_ID_OWNER     = "id_owner";
  public static final String ATTRIBUTE_ID_PARENT    = "id_parent";
  public static final String ATTRIBUTE_CODE         = "code";
  public static final String ATTRIBUTE_LABEL        = "label";
  public static final String ATTRIBUTE_DESCRIPTION  = "description";
  public static final String ATTRIBUTE_ORDERING     = "ordering";
  public static final String ATTRIBUTE_CREATE_DATE  = "create_date";
  public static final String ATTRIBUTE_UPDATE_DATE  = "update_date";
  public static final String ATTRIBUTE_DELETE_DATE  = "delete_date";
  public static final String ATTRIBUTE_HIGHLIGHTED  = "highlighted";
  public static final String ATTRIBUTE_PROTOTYPE    = "prototype";
  
  public static final String CODE = "td";

  public static class DescriptorAttributeDeclaration extends AttributeDeclaration {
    
    public DescriptorAttributeDeclaration(String code, AttributeDeclaration.TypeEnumeration type) {
      this._code = code;
      this._name = code;
      this._type = type;
    }
    
  }
  
  public DescriptorDefinition() {
    this._code = DescriptorDefinition.CODE;
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_ID_NODE, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_ID_NODE, AttributeDeclaration.TypeEnumeration.INTEGER));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_EDITABLE, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_EDITABLE, AttributeDeclaration.TypeEnumeration.BOOLEAN));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_ID_OWNER, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_ID_OWNER, AttributeDeclaration.TypeEnumeration.INTEGER));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_ID_PARENT, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_ID_PARENT, AttributeDeclaration.TypeEnumeration.INTEGER));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_CODE, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_CODE, AttributeDeclaration.TypeEnumeration.TEXT));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_LABEL, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_LABEL, AttributeDeclaration.TypeEnumeration.TEXT));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, AttributeDeclaration.TypeEnumeration.TEXT));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_ORDERING, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_ORDERING, AttributeDeclaration.TypeEnumeration.INTEGER));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_CREATE_DATE, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_CREATE_DATE, AttributeDeclaration.TypeEnumeration.DATETIME));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE, AttributeDeclaration.TypeEnumeration.DATETIME));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_DELETE_DATE, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_DELETE_DATE, AttributeDeclaration.TypeEnumeration.DATETIME));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED, AttributeDeclaration.TypeEnumeration.BOOLEAN));
    this.addAttribute(DescriptorDefinition.ATTRIBUTE_PROTOTYPE, new DescriptorAttributeDeclaration(DescriptorDefinition.ATTRIBUTE_PROTOTYPE, AttributeDeclaration.TypeEnumeration.BOOLEAN));
  }
  
  private void addAttribute(String code, AttributeDeclaration attributeDeclaration) {
    this._attributeDeclarationList.add(attributeDeclaration);
    this.attributeMap.put(code, attributeDeclaration);
  }
  
}