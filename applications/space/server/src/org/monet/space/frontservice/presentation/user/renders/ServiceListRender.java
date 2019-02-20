package org.monet.space.frontservice.presentation.user.renders;

import org.monet.metamodel.Definition;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.ServiceType;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class ServiceListRender extends FrontserviceRender {

	public ServiceListRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		loadCanvas("page");

		addMark("businessServices", this.initServices(block("business$label", map), ServiceType.Business, (List<Definition>) this.getParameter("businessServices")));
		addMark("sourceServices", this.initServices(block("source$label", map), ServiceType.Source, (List<Definition>) this.getParameter("sourceServices")));
	}

	private String initServices(String label, ServiceType type, List<Definition> definitionList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String items = "";

		if (definitionList.size() == 0) return "";

		for (Definition definition : definitionList) {
			map.put("label", definition.getLabelString());
			map.put("description", definition.getDescription());
			String url = "";
			try {
				url = this.getParameterAsString("baseUrl") + type.toString().toLowerCase() + "/" + URLEncoder.encode(definition.getName(), "UTF-8") + "/";
			} catch (Exception e) {
				AgentLogger.getInstance().error(e);
			}
			map.put("url", url);
			items += block("services$item", map);
			map.clear();
		}

		map.put("label", label);
		map.put("items", items);

		return block("services", map);
	}

}
