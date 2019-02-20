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

package org.monet.metamodel.internal;

import org.monet.metamodel.*;
import org.monet.metamodel.interfaces.Language;

public class TaskOrderDefinition extends FormDefinition {

	public static class SuggestedStartDateProperty extends DateFieldProperty {
		public static final String CODE = "_vrwtkw";

		public SuggestedStartDateProperty() {
			super();
			this._code = SuggestedStartDateProperty.CODE;
			this._name = "SuggestedStartDate";
			this._label = Language.SUGGESTED_START_DATE;
			this._precision = DateFieldPropertyBase.PrecisionEnumeration.DAYS;
		}
	}

	public static class SuggestedEndDateProperty extends DateFieldProperty {
		public static final String CODE = "_7vewhg";

		public SuggestedEndDateProperty() {
			super();
			this._code = SuggestedEndDateProperty.CODE;
			this._name = "SuggestedEndDate";
			this._label = Language.SUGGESTED_END_DATE;
			this._precision = DateFieldPropertyBase.PrecisionEnumeration.DAYS;
		}
	}

	public static class CommentsProperty extends MemoFieldProperty {
		public static final String CODE = "_y__kmq";

		public CommentsProperty() {
			super();
			this._code = CommentsProperty.CODE;
			this._name = "Comments";
			this._label = Language.COMMENTS;
		}
	}

	public static class UrgentProperty extends BooleanFieldProperty {
		public static final String CODE = "_ss7omw";

		public UrgentProperty() {
			super();
			this._code = UrgentProperty.CODE;
			this._name = "Urgent";
			this._label = Language.URGENT;
		}
	}

	public static final SuggestedStartDateProperty SuggestedStartDate = new SuggestedStartDateProperty();
	public static final SuggestedEndDateProperty SuggestedEndDate = new SuggestedEndDateProperty();
	public static final CommentsProperty Comments = new CommentsProperty();
	public static final UrgentProperty Urgent = new UrgentProperty();

	public static final String CODE = "_i1ppeg";
	public static final String NAME = "org.monet.metamodel.internal.TaskOrderDefinition";

	public TaskOrderDefinition() {
		this._code = TaskOrderDefinition.CODE;
		this._name = TaskOrderDefinition.NAME;
		this._isAbstract = null;
		this._label = Language.TASK_ORDER_TITLE;

		this.addDateFieldProperty(SuggestedStartDate);
		this.addDateFieldProperty(SuggestedEndDate);
		this.addMemoFieldProperty(Comments);
		this.addBooleanFieldProperty(Urgent);

		org.monet.metamodel.FormDefinition.FormViewProperty viewDefinition = new org.monet.metamodel.FormDefinition.FormViewProperty();
		viewDefinition.setCode("_vqfkjg");
		viewDefinition.setName("View");
		viewDefinition.setIsDefault(true);

		org.monet.metamodel.FormDefinition.FormViewProperty.ShowProperty showDefinition = new org.monet.metamodel.FormDefinition.FormViewProperty.ShowProperty();
		showDefinition.getField().add(new org.monet.metamodel.internal.Ref("SuggestedStartDate", TaskOrderDefinition.NAME, ""));
		showDefinition.getField().add(new org.monet.metamodel.internal.Ref("SuggestedEndDate", TaskOrderDefinition.NAME, ""));
		showDefinition.getField().add(new org.monet.metamodel.internal.Ref("Urgent", TaskOrderDefinition.NAME, ""));
		showDefinition.getField().add(new org.monet.metamodel.internal.Ref("Comments", TaskOrderDefinition.NAME, ""));
		viewDefinition.setShow(showDefinition);

		this.addView(viewDefinition);
		this.init();
	}

}