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

import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.FilterProperty;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Role.Nature;
import org.monet.space.kernel.model.news.Post;

import java.util.List;
import java.util.Map;

public interface RenderLink {

	Node loadNode(String id);

	Node loadNodeRevision(String id, String idRevision);

	Revision loadRevision(String id);

	Node locateNode(String code);

	Source<SourceDefinition> loadSource(String id);

	Source<SourceDefinition> locateSource(String code, FeederUri uri);

	SourceList loadSourceList(String code, String partnerContext);

	Map<String, FederationUnit> loadSourceListPartners(List<String> ontologies);

	Map<String, Integer> loadReferenceAttributeValuesCount(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition);

	int requestNodeListItemsCount(String idNode, NodeDataRequest dataRequest);

	Map<String, Node> requestNodeListItems(String idNode, NodeDataRequest dataRequest);

	TermList loadSourceTerms(String id, DataRequest dataRequest);

	List<Post> loadNews(int startPos, int limit);

	Task loadTask(String id);

	TaskList loadTasks(String code);

	TaskFilters loadTaskFilters(String language);

	RoleList loadTasksRoleList();

	UserList loadTasksOwnerList();

	UserList loadTasksSenderList();

	UserList loadTasksSenderList(String idOwner);

	TaskOrder loadTaskOrder(String id);

	int requestTaskOrderListItemsCount(String id);

	RoleList loadTaskOrdersRoleList(String idTask);

	RoleList loadNonExpiredRoleList(String code, Nature nature);

	Dashboard loadDashboard(String code);

	TaskList searchTasks(Account account, TaskSearchRequest request);

	int searchTasksCount(Account account, TaskSearchRequest request);
	
}
