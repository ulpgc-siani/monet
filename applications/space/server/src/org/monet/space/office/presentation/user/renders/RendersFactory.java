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

package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.metamodel.internal.LayoutDefinition;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.*;
import org.monet.space.office.core.constants.ErrorCode;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class RendersFactory {
	private static RendersFactory instance;
	private HashMap<String, Object> renders;

	private static final String PAGE_MODE = "mode=page";
	private static final String DEFINITION_MODE = "mode=definition";
	private static final String PRINT_MODE = "mode=print";

	private RendersFactory() {
		this.renders = new HashMap<>();
		this.register("form_view", FormViewRender.class);
		this.register("form_page", FormPageRender.class);
		this.register("form_print", FormPrintRender.class);
		this.register("desktop_view", DesktopViewRender.class);
		this.register("desktop_page", DesktopPageRender.class);
		this.register("container_view", ContainerViewRender.class);
		this.register("container_page", ContainerPageRender.class);
		this.register("container_print", ContainerPrintRender.class);
		this.register("collection_view", CollectionViewRender.class);
		this.register("collection_page", CollectionPageRender.class);
		this.register("collection_print", CollectionPrintRender.class);
		this.register("document_view", DocumentViewRender.class);
		this.register("document_page", DocumentPageRender.class);
		this.register("catalog_view", CatalogViewRender.class);
		this.register("catalog_page", CatalogPageRender.class);
		this.register("catalog_print", CatalogPrintRender.class);
		this.register("tasklist_page", TaskListPageRender.class);
		this.register("tasklist_view", TaskListViewRender.class);
		this.register("tasklist_print", TaskListPrintRender.class);
		this.register("rolelist_page", RoleListPageRender.class);
		this.register("rolelist_view", RoleListViewRender.class);
		this.register("role_page", RolePageRender.class);
		this.register("role_view", RoleViewRender.class);
		this.register("activity_page", ActivityPageRender.class);
		this.register("job_page", JobPageRender.class);
		this.register("service_page", ServicePageRender.class);
		this.register("activity_view", ActivityViewRender.class);
		this.register("job_view", JobViewRender.class);
		this.register("service_view", ServiceViewRender.class);
		this.register("notificationlist_page", NotificationListPageRender.class);
		this.register("sourcelist_page", SourceListPageRender.class);
		this.register("sourcelist_view", SourceListViewRender.class);
		this.register("source_page", SourcePageRender.class);
		this.register("source_view", SourceViewRender.class);
		this.register("news_page", NewsPageRender.class);
		this.register("trash_page", TrashPageRender.class);
		this.register("trash_view", TrashViewRender.class);
		this.register("dashboard_page", DashboardPageRender.class);
		this.register("dashboard_view", DashboardViewRender.class);
		this.register("composite_field_view", CompositeFieldViewRender.class);
		this.register("text_field_view", TextFieldViewRender.class);
		this.register("memo_field_view", MemoFieldViewRender.class);
		this.register("boolean_field_view", BooleanFieldViewRender.class);
		this.register("number_field_view", NumberFieldViewRender.class);
		this.register("date_field_view", DateFieldViewRender.class);
		this.register("file_field_view", FileFieldViewRender.class);
		this.register("picture_field_view", PictureFieldViewRender.class);
		this.register("select_field_view", SelectFieldViewRender.class);
		this.register("link_field_view", LinkFieldViewRender.class);
		this.register("check_field_view", CheckFieldViewRender.class);
		this.register("node_field_view", NodeFieldViewRender.class);
		this.register("serial_field_view", SerialFieldViewRender.class);
		this.register("summation_field_view", SummationFieldViewRender.class);
		this.register("businessmodel_definition", BusinessModelDefinitionRender.class);
		this.register("desktop_definition", DesktopDefinitionRender.class);
		this.register("collection_definition", CollectionDefinitionRender.class);
		this.register("catalog_definition", NodeDefinitionRender.class);
		this.register("container_definition", ContainerDefinitionRender.class);
		this.register("document_definition", NodeDefinitionRender.class);
		this.register("form_definition", FormDefinitionRender.class);
		this.register("task_definition", TaskDefinitionRender.class);
		this.register("layout_view", LayoutViewRender.class);
		this.register("news_view", NewsViewRender.class);
		this.register("suggestion_page", SuggestionPageRender.class);
		this.register("fieldcheckoptions_view", FieldCheckOptionsViewRender.class);
		this.register("collectiongroupbyoptions_view", CollectionGroupByOptionsViewRender.class);
		this.register("cataloggroupbyoptions_view", CatalogGroupByOptionsViewRender.class);
	}

	public synchronized static RendersFactory getInstance() {
		if (instance == null) instance = new RendersFactory();
		return instance;
	}

	public <T extends OfficeRender> T get(Object object, String template, RenderLink renderLink, Account account) {
		Class<?> renderClass;
		OfficeRender render;
		String mode, code = "";

		if (object instanceof Node) {
			Node node = (Node) object;
			if (node.isCatalog()) code = "catalog";
			else if (node.isCollection()) code = "collection";
			else if (node.isContainer()) code = "container";
			else if (node.isDesktop()) code = "desktop";
			else if (node.isDocument()) code = "document";
			else if (node.isForm()) code = "form";
		} else if (object instanceof Field) {
			FieldProperty fieldDeclaration = ((Field)object).getFieldDefinition();
			if (fieldDeclaration.isBoolean()) code = "boolean_field";
			else if (fieldDeclaration.isCheck()) code = "check_field";
			else if (fieldDeclaration.isComposite()) code = "composite_field";
			else if (fieldDeclaration.isDate()) code = "date_field";
			else if (fieldDeclaration.isFile()) code = "file_field";
			else if (fieldDeclaration.isLink()) code = "link_field";
			else if (fieldDeclaration.isMemo()) code = "memo_field";
			else if (fieldDeclaration.isNode()) code = "node_field";
			else if (fieldDeclaration.isNumber()) code = "number_field";
			else if (fieldDeclaration.isPicture()) code = "picture_field";
			else if (fieldDeclaration.isSelect()) code = "select_field";
			else if (fieldDeclaration.isSerial()) code = "serial_field";
			else if (fieldDeclaration.isSummation()) code = "summation_field";
			else if (fieldDeclaration.isText()) code = "text_field";
		}
		else if (object instanceof Task) {
			Task task = (Task) object;
			if (task.isActivity()) code = "activity";
			else if (task.isJob()) code = "job";
			else if (task.isService()) code = "service";
		} else if (object instanceof Cube) code = "cube";
		else if (object instanceof TaskList) code = "tasklist";
		else if (object instanceof RoleList) code = "rolelist";
		else if (object instanceof UserRole) code = "role";
		else if (object instanceof Trash) code = "trash";
		else if (object instanceof Dashboard) code = "dashboard";
		else if (object instanceof NotificationList) code = "notificationlist";
		else if (object instanceof SourceList) code = "sourcelist";
		else if (object instanceof Source) code = "source";
		else if (object instanceof News) code = "news";
		else if (object instanceof Team) code = "team";
		else if (object instanceof BusinessModel) code = "businessmodel";
		else if (object instanceof CatalogDefinition) code = "catalog";
		else if (object instanceof CollectionDefinition) code = "collection";
		else if (object instanceof ContainerDefinition) code = "container";
		else if (object instanceof DesktopDefinition) code = "desktop";
		else if (object instanceof DocumentDefinition) code = "document";
		else if (object instanceof FormDefinition) code = "form";
		else if (object instanceof TaskDefinition) code = "task";
		else if (object instanceof Suggestion) code = "suggestion";
		else if (object instanceof RevisionList) code = "revisionlist";
		else if (object instanceof LayoutDefinition) code = "layout";
		else if (object instanceof String) code = (String) object;

		if (code.isEmpty()) return null;

		if (template.contains(PAGE_MODE)) mode = "page";
		else if (template.contains(DEFINITION_MODE)) mode = "definition";
		else if (template.contains(PRINT_MODE)) mode = "print";
		else mode = "view";

		code += "_" + mode;

		try {
			renderClass = (Class<?>) this.renders.get(code);
			Constructor<?> constructor = renderClass.getConstructor();
			render = (OfficeRender) constructor.newInstance();
			render.setRenderLink(renderLink);
			render.setTarget(object);
			render.setTemplate(template);
			render.setAccount(account);
		} catch (Exception exception) {
			throw new SystemException(ErrorCode.RENDERS_FACTORY, code, exception);
		}

		return (T) render;
	}

	public Boolean register(String code, Class<?> renderClass) throws IllegalArgumentException {

		if ((renderClass == null) || (code == null)) {
			return false;
		}
		this.renders.put(code, renderClass);

		return true;
	}

}
