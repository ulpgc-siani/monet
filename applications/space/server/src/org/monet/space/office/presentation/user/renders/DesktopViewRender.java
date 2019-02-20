package org.monet.space.office.presentation.user.renders;

import edu.emory.mathcs.backport.java.util.Collections;
import org.monet.metamodel.DashboardDefinitionBase.DashboardViewProperty;
import org.monet.metamodel.Definition;
import org.monet.metamodel.DesktopDefinition;
import org.monet.metamodel.DesktopDefinitionBase.ViewProperty;
import org.monet.metamodel.DesktopDefinitionBase.ViewProperty.ShowProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DesktopViewRender extends NodeViewRender {
	private DesktopDefinition definition;

	public DesktopViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
		this.definition = (DesktopDefinition) this.node.getDefinition();
	}

	protected ArrayList<Entity<?>> locateEntities(String code) {
		Definition definition = this.dictionary.getDefinition(code);
		ArrayList<Entity<?>> entities = new ArrayList<Entity<?>>();

		if (definition.isDisabled())
			return entities;

		if (definition instanceof NodeDefinition) {
			if (((NodeDefinition) definition).isSingleton()) {
				Node node = this.renderLink.locateNode(definition.getCode());
				entities.add(node);
			} else {
				String childId = this.node.getIndicatorValue("[" + definition.getCode() + "].value");
				Node childNode = null;

				try {
					childNode = this.renderLink.loadNode(childId);
				} catch (Exception ex) {
				}

				entities.add(childNode);
			}
		} else if (definition instanceof SourceDefinition) {
			SourceList sourceList = this.renderLink.loadSourceList(definition.getCode(), this.node.getPartnerContext());

			for (Source<SourceDefinition> source : sourceList)
				entities.add(source);
		}

		return entities;
	}

	private String showEntityLinks(ShowProperty showDefinition) {
		String showBlockName = "";
		StringBuilder links = new StringBuilder();

		if (showDefinition == null || showDefinition.getLink() == null)
			return links.toString();

		for (Ref reference : showDefinition.getLink()) {
			ArrayList<Entity<?>> entitiesList = this.locateEntities(reference.getValue());
			StringBuilder entities = new StringBuilder();

			Collections.sort(entitiesList, new Comparator<Entity<?>>() {
				public int compare(Entity<?> entity1, Entity<?> entity2) {
					String entity1Label = entity1.getInstanceLabel();
					String entity2Label = entity2.getInstanceLabel();
					return entity1Label.compareTo(entity2Label);
				}
			});

			for (Entity<?> entity : entitiesList) {
				HashMap<String, Object> localMap = new HashMap<String, Object>();

				if (entity == null) {
					entities.append(block("link.entity$notFound", localMap));
					continue;
				}

				String description = entity.getDescription();
				if (entity instanceof Node)
					showBlockName = "link.entity";
				else if (entity instanceof Source)
					showBlockName = "link.entity.source";
				else
					continue;

				localMap.put("entityId", entity.getId());
				localMap.put("entityCode", entity.getCode());
				localMap.put("entityLabel", entity.getInstanceLabel());
				localMap.put("entityDescription", description);

				entities.append(block(showBlockName, localMap));
				localMap.clear();
			}

			if (entitiesList.size() <= 0)
				continue;

			String blockName = (entitiesList.size() > 1) ? "link.multiple" : "link.simple";
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			Entity<?> entity = entitiesList.get(0);

			localMap.put("entityLabel", entity != null ? entity.getLabel() : "");
			localMap.put("entityDescription", entity != null ? entity.getDescription() : "");
			localMap.put("entities", entities.toString());
			links.append(block(blockName, localMap));
		}

		return links.toString();
	}

	protected String showDashboard(ViewProperty viewDefinition, ShowProperty showDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String result = "";

		if (showDefinition == null || showDefinition.getDashboard() == null)
			return "";

		map.put("idNode", this.node.getId());
		map.put("codeView", viewDefinition.getCode());
		map.put("type", "");

		if (showDefinition.getDashboard().size() > 0) {
			Ref dashboardRef = showDefinition.getDashboard().get(0);
			Dashboard dashboard = this.renderLink.loadDashboard(dashboardRef.getDefinition());
			DashboardViewProperty dashboardViewDefinition = dashboard.getDefinition().getViewMap().get(dashboardRef.getValue());

			if (dashboard != null) {
				OfficeRender render = this.rendersFactory.get(dashboard, "preview.html?mode=view", this.renderLink, account);
				render.setParameters(this.getParameters());
				render.setParameter("id", this.node.getId());
				render.setParameter("view", dashboardViewDefinition.getCode());
				result = render.getOutput();
			}
		}

		map.put("render(view.dashboard)", result);

		return block("show.dashboard", map);
	}

	protected void initContent(HashMap<String, Object> viewMap, DesktopDefinition.ViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String shows = "", links = "";
		ShowProperty showDefinition = viewDefinition.getShow();

		links += this.showEntityLinks(showDefinition);

		shows += this.showDashboard(viewDefinition, showDefinition);

		map.put("links", links);
		map.put("shows", shows);

		viewMap.put("content", block("content", map));
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		DesktopDefinition.ViewProperty viewDefinition = (DesktopDefinition.ViewProperty) this.definition.getNodeView(codeView);

		if (this.node.requirePartnerContext() && this.node.getPartnerContext() == null) {
			String result = this.initPartnerContext();
			if (result != null) return result;
		}

		boolean isLocationView = codeView.equals("location");
		if (isLocationView) {
			this.initMapWithoutView(map, "location");
			return this.initLocationView(map);
		} else if (viewDefinition == null) {
			map.put("codeView", codeView);
			map.put("labelDefinition", this.definition.getLabelString());
			return block("view.undefined", map);
		}

		this.initMap(map, viewDefinition);

		if (this.isSystemView(viewDefinition)) {
			return this.initSystemView(map, viewDefinition);
		}

		this.initContent(map, viewDefinition);

		return block("view", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.node.desktop");
		super.init();
	}

}
