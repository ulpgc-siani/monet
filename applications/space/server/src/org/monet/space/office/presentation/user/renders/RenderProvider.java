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
import org.monet.space.kernel.components.layers.*;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Role.Nature;
import org.monet.space.kernel.model.news.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenderProvider implements RenderLink {
	private NodeLayer nodeLayer;
	private SourceLayer sourceLayer;
	private NewsLayer newsLayer;
	private TaskLayer taskLayer;
	private RoleLayer roleLayer;
	private DashboardLayer dashboardLayer;

	public RenderProvider(NodeLayer nodeLayer, SourceLayer sourceLayer, NewsLayer newsLayer, TaskLayer taskLayer, RoleLayer roleLayer, DashboardLayer dashboardLayer) {
		this.nodeLayer = nodeLayer;
		this.sourceLayer = sourceLayer;
		this.newsLayer = newsLayer;
		this.taskLayer = taskLayer;
		this.roleLayer = roleLayer;
		this.dashboardLayer = dashboardLayer;
	}

	@Override
	public Node loadNode(String id) {
		return this.nodeLayer.loadNode(id);
	}

	@Override
	public Node loadNodeRevision(String id, String idRevision) {
		return this.nodeLayer.loadNodeRevision(id, idRevision);
	}

	@Override
	public Revision loadRevision(String id) {
		return this.nodeLayer.loadRevision(id);
	}

	@Override
	public ArrayList<Post> loadNews(int startPos, int limit) {
		return this.newsLayer.getPosts(startPos, limit);
	}

	@Override
	public Node locateNode(String code) {
		return this.nodeLayer.locateNode(code);
	}

	@Override
	public Source<SourceDefinition> loadSource(String id) {
		return this.sourceLayer.loadSource(id);
	}

	@Override
	public Source<SourceDefinition> locateSource(String code, FeederUri uri) {
		return this.sourceLayer.locateSource(code, uri);
	}

	@Override
	public SourceList loadSourceList(String code, String partner) {
		return this.sourceLayer.loadSourceList(code, partner);
	}

	@Override
	public Map<String, FederationUnit> loadSourceListPartners(List<String> ontologies) {
		return this.sourceLayer.loadSourceListPartners(ontologies);
	}

	@Override
	public Map<String, Integer> loadReferenceAttributeValuesCount(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition) {
		return this.nodeLayer.loadReferenceAttributeValuesCount(ownerId, codeReference, codeAttribute, filterNodes, filterAttributesDefinition);
	}

	@Override
	public int requestNodeListItemsCount(String idNode, NodeDataRequest dataRequest) {
		return this.nodeLayer.requestNodeListItemsCount(idNode, dataRequest);
	}

	@Override
	public Map<String, Node> requestNodeListItems(String idNode, NodeDataRequest dataRequest) {
		return this.nodeLayer.requestNodeListItems(idNode, dataRequest);
	}

	@Override
	public TermList loadSourceTerms(String id, DataRequest dataRequest) {
		Source<SourceDefinition> source = this.sourceLayer.loadSource(id);
		return this.sourceLayer.loadSourceTerms(source, dataRequest, true);
	}

	@Override
	public TaskList loadTasks(String code) {
		return this.taskLayer.loadTasks(code);
	}

	@Override
	public TaskFilters loadTaskFilters(String language) {
		return this.taskLayer.loadTasksFilters(language);
	}

	@Override
	public RoleList loadTasksRoleList() {
		return this.taskLayer.loadTasksRoleList();
	}

	@Override
	public UserList loadTasksOwnerList() {
		return this.taskLayer.loadTasksOwnerList();
	}

	@Override
	public UserList loadTasksSenderList() {
		return this.taskLayer.loadTasksSenderList();
	}

	@Override
	public UserList loadTasksSenderList(String idOwner) {
		return this.taskLayer.loadTasksSenderList(idOwner);
	}

	@Override
	public Task loadTask(String id) {
		return this.taskLayer.loadTask(id);
	}

	@Override
	public TaskOrder loadTaskOrder(String id) {
		return this.taskLayer.loadTaskOrder(id);
	}

	@Override
	public int requestTaskOrderListItemsCount(String id) {
		return this.taskLayer.requestTaskOrderListItemsCount(id);
	}

	@Override
	public RoleList loadTaskOrdersRoleList(String idTask) {
		return this.taskLayer.loadTaskOrdersRoleList(idTask);
	}

	@Override
	public RoleList loadNonExpiredRoleList(String codeRole, Nature nature) {
		DataRequest dataRequest = new DataRequest();
		dataRequest.setStartPos(0);
		dataRequest.setLimit(-1);
		dataRequest.addParameter(DataRequest.NATURE, nature.toString());
		dataRequest.addParameter(DataRequest.NON_EXPIRED, "true");
		return this.roleLayer.loadRoleList(codeRole, dataRequest);
	}

	@Override
	public Dashboard loadDashboard(String code) {
		return this.dashboardLayer.load(code);
	}

	@Override
	public TaskList searchTasks(Account account, TaskSearchRequest request) {
		return this.taskLayer.searchTasks(account, request);
	}

	@Override
	public int searchTasksCount(Account account, TaskSearchRequest request) {
		return this.taskLayer.searchTasksCount(account, request);
	}

}