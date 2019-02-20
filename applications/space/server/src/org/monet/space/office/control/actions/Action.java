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

package org.monet.space.office.control.actions;

import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.SignatureProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.SetDefinition;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.ComponentSecurity;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.DataRequest.GroupBy;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.office.ApplicationOffice;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.OfficeProfile;
import org.monet.space.office.presentation.user.agents.AgentRender;
import org.monet.space.office.presentation.user.renders.RenderLink;
import org.monet.space.office.presentation.user.renders.RenderProvider;
import org.monet.space.office.presentation.user.renders.RendersFactory;
import org.monet.space.office.presentation.user.views.ViewsFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public abstract class Action {
	protected String codeLanguage;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected AgentSession agentSession;
	protected AgentRender agentRender;
	protected AgentLogger agentException;
	protected ActionsFactory actionsFactory;
	protected ViewsFactory viewsFactory;
	protected RendersFactory rendersFactory;
	protected String idSession;
	protected ComponentSecurity componentSecurity;

	public Action() {
		this.codeLanguage = null;
		this.request = null;
		this.response = null;
		this.agentSession = AgentSession.getInstance();
		this.agentRender = new AgentRender();
		this.agentException = AgentLogger.getInstance();
		this.actionsFactory = ActionsFactory.getInstance();
		this.viewsFactory = ViewsFactory.getInstance();
		this.rendersFactory = RendersFactory.getInstance();
		this.componentSecurity = ComponentSecurity.getInstance();
	}

	protected Boolean initLanguage() {
		Session session = this.agentSession.get(idSession);

		this.codeLanguage = null;
		if (session != null) this.codeLanguage = this.getFederationLayer().getUserLanguage();
		if (this.codeLanguage == null) this.codeLanguage = this.request.getLocale().getLanguage();

		return true;
	}

	protected FederationLayer getFederationLayer() {
		return ComponentFederation.getInstance().getLayer(new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return request.getSession().getId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationOffice.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return Configuration.getInstance().getFederationLogoUrl();
			}

			@Override
			public HttpServletRequest getRequest() {
				return request;
			}
		});
	}

	protected RenderLink getRenderLink() {
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		ComponentFederation componentFederation = ComponentFederation.getInstance();
		ComponentDatawareHouse componentDatawareHouse = ComponentDatawareHouse.getInstance();
		return new RenderProvider(componentPersistence.getNodeLayer(),
			componentPersistence.getSourceLayer(),
	                          /*ComponentFederation.getInstance().getLayer(ApplicationOffice.NAME, ApplicationOffice.getCallbackUrl(), this.request, this.request.getSession().getId()),*/
			componentPersistence.getNewsLayer(),
			componentPersistence.getTaskLayer(),
			componentFederation.getRoleLayer(),
			componentDatawareHouse.getDashboardLayer());
	}

	protected Boolean loadProfile(Account account) {
		OfficeProfile profile;
		profile = new OfficeProfile();
		account.setProfile(Profile.OFFICE, profile);
		return true;
	}

	protected HashMap<String, String> getRequestParameters() {
		Enumeration<String> enumeration = LibraryRequest.getParameterNames(this.request);
		HashMap<String, String> result = new HashMap<String, String>();

		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			try {
				result.put(name, URLDecoder.decode(LibraryRequest.getParameter(name, this.request), "utf8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public Session getSession() {
		return AgentSession.getInstance().get(request.getSession().getId());
	}

	public Account getAccount() {
		Account account = this.getFederationLayer().loadAccount();

        if (account == null)
            return null;

		OfficeProfile profile = (OfficeProfile) account.getProfile(Profile.OFFICE);
		if (profile == null)
			this.loadProfile(account);

		return account;
	}

	public OfficeProfile getProfile() {
		return (OfficeProfile) this.getAccount().getProfile(Profile.OFFICE);
	}

	public Boolean setRequest(HttpServletRequest request) {
		this.request = request;
		this.idSession = request.getSession().getId();
		return true;
	}

	public Boolean setResponse(HttpServletResponse response) {
		this.response = response;
		return true;
	}

	protected List<SortBy> getSortsBy(String sortsBy) {
		List<SortBy> result = new ArrayList<>();
		String[] sortsByArray;

		if (sortsBy == null) return result;
		if (sortsBy.equals(Strings.EMPTY)) return result;

		sortsByArray = sortsBy.split("_f_");
		for (int i = 0; i < sortsByArray.length; i++) {
			final String[] sortByArray = sortsByArray[i].split(Strings.COLON);
			result.add(new SortBy() {
				@Override
				public String attribute() {
					return sortByArray[0];
				}

				@Override
				public String mode() {
					return sortByArray[1];
				}
			});
		}

		return result;
	}

	protected List<GroupBy> getGroupsBy(String data) {
		List<GroupBy> result = new ArrayList<>();
		String[] groupsByArray;

		if (data == null) return result;
		if (data.equals(Strings.EMPTY)) return result;

		groupsByArray = data.split("_f_");
		for (int i = 0; i < groupsByArray.length; i++) {
			final String[] groupByArray = groupsByArray[i].split(Strings.COLON);
			if (groupByArray.length <= 1) continue;
			else result.add(new GroupBy() {
				@Override
				public String attribute() {
					return groupByArray[0];
				}

				@Override
				public List<Object> values() {
					return new ArrayList<Object>() {{ add(groupByArray[1]); }};
				}

				@Override
				public <T> T value(int pos) {
					return (T) values().get(pos);
				}

				@Override
				public Operator operator() {
					return Operator.Eq;
				}
			});
		}

		return result;
	}

	protected Map<String, GroupBy> getGroupsByMap(String data) {
		Map<String, GroupBy> result = new HashMap<>();
		for (GroupBy groupBy : getGroupsBy(data)) result.put(groupBy.attribute(), groupBy);
		return result;
	}

	public Boolean initialize() {

		this.response.setContentType("text/html;charset=UTF-8");
		this.initLanguage();

		return true;
	}

	protected boolean hasRole(SignatureProperty signatureDefinition) {
		ArrayList<Ref> forDefinitionList = signatureDefinition.getFor();
		RoleList roleList = this.getAccount().getRoleList();
		Dictionary dictionary = Dictionary.getInstance();

		for (Ref forDefinition : forDefinitionList) {
			if (roleList.exist(dictionary.getDefinitionCode(forDefinition.getValue())))
				return true;
		}

		return false;
	}

	protected IndexDefinition getIndexDefinition(String set) {
		String nameReference = DescriptorDefinition.CODE;
		Dictionary dictionary = Dictionary.getInstance();
		NodeDefinition definition = dictionary.getNodeDefinition(set);

		if (definition.isSet()) {
			SetDefinition setDefinition = ((SetDefinition) definition);
			if (setDefinition.getIndex() != null)
				nameReference = setDefinition.getIndex().getValue();
		} else
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_ITEMS);

		return dictionary.getIndexDefinition(nameReference);
	}

	protected NodeDataRequest createNodeDataRequest(String code, String domain) {
		NodeDataRequest dataRequest = new NodeDataRequest();
		HashMap<String, String> parameters = this.getRequestParameters();

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		try {
			dataRequest.setConditionTag(new String(parameters.get(Parameter.CONDITION_TAG)));
		} catch (NullPointerException oException) {
			dataRequest.setConditionTag(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setConditionTag(Strings.EMPTY);
		}

		try {
			dataRequest.setStartPos(Integer.valueOf(parameters.get(Parameter.START)));
		} catch (NumberFormatException oException) {
			dataRequest.setStartPos(0);
		} catch (ClassCastException oException) {
			dataRequest.setStartPos(0);
		}

		try {
			dataRequest.setLimit(Integer.valueOf(parameters.get(Parameter.LIMIT)));
		} catch (NumberFormatException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		} catch (ClassCastException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		}

		dataRequest.setParameters(parameters);
		dataRequest.setCode(code);
		dataRequest.setCodeDomainNode(domain);
		dataRequest.setSortsBy(getSortsBy(parameters.get(Parameter.SORTS_BY)));
		dataRequest.setGroupsBy(getGroupsBy(parameters.get(Parameter.GROUPS_BY)));

		return dataRequest;
	}

	public abstract String execute() throws IOException;

}