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

package org.monet.space.kernel.model;

import org.monet.space.kernel.constants.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRequest {
	protected String code;
	protected String path;
	protected Integer start;
	protected Integer limit;
	protected String condition;
	protected String conditionTag;
	protected Map<String, String> parameters;
	protected List<SortBy> sortsBy;
	protected List<GroupBy> groupsBy;

	public static final String MODE = "mode";
	public static final String FLATTEN = "flatten";
	public static final String DEPTH = "depth";
	public static final String FROM = "from";
	public static final String FILTERS = "filters";
	public static final String NATURE = "nature";
	public static final String NON_EXPIRED = "nonexpired";
	public static final String LOCATION_ID = "locationid";

	public static final String OPERATOR_SEPARATOR = "__";

	public DataRequest() {
		this.code = Strings.EMPTY;
		this.path = Strings.EMPTY;
		this.start = 0;
		this.limit = 10;
		this.condition = Strings.EMPTY;
		this.conditionTag = Strings.EMPTY;
		this.parameters = new HashMap<>();
		this.setSortsBy(new ArrayList<SortBy>());
		this.setGroupsBy(new ArrayList<GroupBy>());
	}

	public interface Mode {
		String ALL = "all";
		String TREE = "tree";
		String FLATTEN = "flatten";
	}

	public String getCode() {
		return this.code;
	}

	public Boolean setCode(String code) {
		this.code = code;
		return true;
	}

	public Integer getStartPos() {
		return this.start;
	}

	public Boolean setStartPos(Integer startPos) {
		this.start = startPos;
		return true;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public Boolean setLimit(Integer limit) {
		this.limit = limit;
		return true;
	}

	public String getCondition() {
		return this.condition != null ? this.condition : "";
	}

	public Boolean setCondition(String condition) {
		this.condition = condition;
		return true;
	}

	public String getConditionTag() {
		return this.conditionTag != null ? this.conditionTag : "";
	}

	public Boolean setConditionTag(String conditionTag) {
		this.conditionTag = conditionTag;
		return true;
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}

	public String getParameter(String name) {
		return this.parameters.get(name);
	}

	public void addParameter(String name, String value) {
		this.parameters.put(name, value);
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	public String hasCode() {
		String result = "";

		result += this.code.hashCode();
		result += this.path.hashCode();
		result += this.start.hashCode();
		result += this.limit.hashCode();
		result += this.condition.hashCode();

		for (String value : this.parameters.values()) {
			result += value != null ? value.hashCode() : 0;
		}

		return result;
	}

	public List<SortBy> getSortsBy() {
		return sortsBy;
	}

	public void setSortsBy(List<SortBy> sortsBy) {
		this.sortsBy = sortsBy;
	}

	public List<GroupBy> getGroupsBy() {
		return groupsBy;
	}

	public void setGroupsBy(List<GroupBy> groupsBy) {
		this.groupsBy = groupsBy;
	}

	public interface SortBy {
		String attribute();
		String mode();
	}

	public interface GroupBy {
		String attribute();
		List<Object> values();
		<T> T value(int pos);
		Operator operator();

		enum Operator { Eq, Gt, Lt }
	}
}
