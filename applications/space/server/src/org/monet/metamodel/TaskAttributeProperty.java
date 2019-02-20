package org.monet.metamodel;

import java.util.ArrayList;

/**
 * TaskAttributeProperty Esta propiedad se utiliza para añadir atributo de
 * tareas a un índice
 */

public class TaskAttributeProperty extends ReferenceableProperty {

	public enum TypeEnumeration {
		BOOLEAN, STRING, INTEGER, REAL, DATE, TERM, LINK, CHECK, PICTURE, CATEGORY
	}

	protected ArrayList<TypeEnumeration> _type = new ArrayList<TypeEnumeration>();

	public ArrayList<TypeEnumeration> getType() {
		return _type;
	}

	public void setType(ArrayList<TypeEnumeration> value) {
		_type = value;
	}

	public void merge(TaskAttributeProperty child) {
		super.merge(child);

		if (child._type != null)
			this._type.addAll(child._type);

	}

	public Class<?> getMetamodelClass() {
		return TaskAttributeProperty.class;
	}

}
