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

package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.ApplicationAnalytics;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.model.Kernel;
import org.monet.space.analytics.model.Language;
import org.monet.space.analytics.serializers.CategoriesSerializer;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Session;
import org.sumus.asset.Asset;
import org.sumus.asset.AssetException;
import org.sumus.dictionary.Dictionary;
import org.sumus.dimension.MemberList;
import org.sumus.dimension.category.Category;
import org.sumus.dimension.category.CategoryList;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;
import org.sumus.query.Chart;
import org.sumus.query.Drill;
import org.sumus.selection.Selection;
import org.sumus.selection.SelectionList;
import org.sumus.source.QuerySource;
import org.sumus.taxonomy.Taxonomy;
import org.sumus.time.TimeLapse;
import org.sumus.time.TimeStamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Action {
	private HttpServletRequest request;
	private HttpServletResponse response;

	protected String codeLanguage;
	protected AgentSession agentSession;
	protected AgentLogger agentException;
	protected ActionsFactory actionsFactory;
	protected String idSession;
	protected HashMap<String, Dictionary> sumusDictionaries;

	public Action() {
		this.codeLanguage = null;
		this.request = null;
		this.response = null;
		this.agentSession = AgentSession.getInstance();
		this.agentException = AgentLogger.getInstance();
		this.actionsFactory = ActionsFactory.getInstance();
		this.sumusDictionaries = new HashMap<String, Dictionary>();
	}

	protected Boolean initLanguage() {

		this.codeLanguage = null;
		if (this.codeLanguage == null) this.codeLanguage = this.request.getHeader("Accept-Language").substring(0, 2);

		return true;
	}

	protected String getParameterAsString(String name) {
		String value = this.request.getParameter(name);

		try {
			if (value == null)
				return null;

			String encoding = this.request.getCharacterEncoding();
			if (encoding == null) encoding = "utf-8";
			if (encoding.toLowerCase().equals("utf-8")) return value;
			return new String(value.getBytes(encoding), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}

		return value;
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

	protected HttpServletResponse getResponse() {
		return response;
	}

	protected boolean isLogged() {
		return this.getFederationLayer().isLogged();
	}

	protected FederationLayer getFederationLayer() {
		return ComponentFederation.getInstance().getLayer(new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return request.getSession().getId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationAnalytics.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return null;
			}

			@Override
			public HttpServletRequest getRequest() {
				return request;
			}
		});
	}

	protected String launchAuthenticateAction() {
		Configuration configuration = Configuration.getInstance();
		Session session = this.agentSession.get(this.idSession);
		session.setVariable(Parameter.REDIRECT, configuration.getUrl() + "/" + this.request.getQueryString().replace(Parameter.OPERATION + "=", ""));
		Action action = (Action) this.actionsFactory.get(Actions.SHOW_APPLICATION, this.request, this.response);
		return action.execute();
	}

	protected org.sumus.dictionary.Dictionary getDictionary(Asset asset) {
		String language = Language.getCurrent();

		if (this.sumusDictionaries.containsKey(language))
			return this.sumusDictionaries.get(language);

		this.sumusDictionaries.put(language, asset.getDictionary(language));

		return this.sumusDictionaries.get(language);
	}

	protected IndicatorList getSumusIndicators(Asset asset, IndicatorList indicatorList) {
		Kernel instance = Kernel.Instance();
		IndicatorList kernelIndicatorList = new IndicatorList();

		for (Indicator selectedIndicator : indicatorList) {
			Indicator indicator = instance.getIndicator(asset, selectedIndicator.getName());
			kernelIndicatorList.add(indicator);
		}
		return kernelIndicatorList;
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

	public Boolean initialize() {

		this.response.setContentType("text/html;charset=UTF-8");
		this.initLanguage();

		return true;
	}

	public abstract String execute();

	protected void addChartSelection(Chart chart, String dashboardId, String filters, IndicatorList indicatorList) throws AssetException {
		if (filters == null)
			return;

		SelectionList selectionList = new SelectionList();
		HashMap<String, CategoryList> categoryIds = CategoriesSerializer.fromJson(filters);

		if (categoryIds.size() <= 0)
			return;

		for (Entry<String, CategoryList> taxonomyCategories : categoryIds.entrySet()) {
			CategoryList taxonomyCategoryList = getCategories(taxonomyCategories, dashboardId);
			selectionList.add(new Selection(taxonomyCategoryList.toArray()));
		}

		chart.select(selectionList);
	}

	protected void addChartDrill(Chart chart, String dashboardId, String compare, IndicatorList indicatorList) throws AssetException {
		if (compare == null)
			return;

		MemberList memberList = new MemberList();
		HashMap<String, CategoryList> categoryIds = CategoriesSerializer.fromJson(compare);

		if (categoryIds.size() <= 0 || indicatorList.size() > 1)
			return;

		for (Entry<String, CategoryList> taxonomyCategories : categoryIds.entrySet()) {
			CategoryList taxonomyCategoryList = getCategories(taxonomyCategories, dashboardId);
			memberList.addAll(taxonomyCategoryList);
		}

		chart.drill(new Drill(memberList.toArray()));
	}

	protected CategoryList getCategories(Entry<String, CategoryList> taxonomyCategories, String dashboardId) throws AssetException {
		CategoryList taxonomyCategoryList = new CategoryList();
		Taxonomy taxonomy = Kernel.Instance().getTaxonomy(dashboardId, taxonomyCategories.getKey());
		for (Category category : taxonomyCategories.getValue())
			taxonomyCategoryList.add(taxonomy.searchCategory(category.getName()));
		return taxonomyCategoryList;
	}

	protected TimeLapse getTimeLapse(TimeStamp from, TimeStamp to, Asset asset, IndicatorList indicatorList) {
		TimeLapse range = this.getRange(asset, indicatorList);

		if (from.getDate().before(range.getFrom().getDate()))
			from = new TimeStamp(range.getFrom().getDate(), from.getScale());

		if (to.getDate().after(range.getTo().getDate()))
			to = new TimeStamp(range.getTo().getDate(), to.getScale());

		return new TimeLapse(from, to);
	}

	protected TimeLapse getRange(Asset asset, IndicatorList indicatorList) {
		TimeLapse range = null;
		IndicatorList kernelIndicatorList = this.getSumusIndicators(asset, indicatorList);

		for (Indicator indicator : kernelIndicatorList) {
			for (QuerySource source : indicator.getSources()) {
				TimeLapse timeLapse = source.getTimeLapse();

				if (timeLapse.isEmpty())
					continue;

				if (range == null)
					range = timeLapse;
				else
					range.union(timeLapse);
			}
		}

		return range;
	}

    protected String getSessionId() {
        return this.request.getSession().getId();
    }

}