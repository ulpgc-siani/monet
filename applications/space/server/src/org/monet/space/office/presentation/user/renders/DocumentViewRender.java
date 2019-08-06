package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.DocumentDefinition;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.kernel.model.Node;

import java.util.HashMap;

public class DocumentViewRender extends NodeViewRender {
	protected DocumentDefinition definition;

	public DocumentViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
		this.definition = (DocumentDefinition) this.node.getDefinition();
	}

	protected void initContent(HashMap<String, Object> viewMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String idEntityContainer = this.getParameterAsString("idEntityContainer");
		map.put("id", this.node.getId());
		map.put("idLayer", ((!idEntityContainer.isEmpty()) ? idEntityContainer + "_" : "") + this.node.getId() + "_preview");
		viewMap.put("content", block("content", map));
	}

	protected String initDocumentInForm(HashMap<String, Object> viewMap) {
		Configuration configuration = Configuration.getInstance();

		viewMap.put("id", this.node.getId());
		viewMap.put("label", this.node.getLabel());
		viewMap.put("codeView", "form");
		viewMap.put("previewImageSource", configuration.getApiUrl() + "?op=previewnode&id=" + this.node.getId() + "&thumb=1&r=" + Math.random());
		viewMap.put("content", block("content.form", viewMap));
		return block("view", viewMap);
	}

	protected String initSignTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("signTemplate:client-side", map);
	}

	protected String initDocumentSignView(HashMap<String, Object> viewMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("id", this.node.getId());
		map.put("signTemplate", this.initSignTemplate());

		viewMap.put("id", this.node.getId());
		viewMap.put("label", this.node.getLabel());
		viewMap.put("codeView", "form");
		viewMap.put("content", block("content.signs", map));

		return block("view", viewMap);
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean isLocationView = codeView.equals("location");
		boolean isFormView = codeView.equals("form");
		boolean isSignsView = codeView.equals("signs");

		if (this.node.requirePartnerContext() && this.node.getPartnerContext() == null) {
			String result = this.initPartnerContext();
			if (result != null) return result;
		}

		if (isLocationView) {
			this.initMapWithoutView(map, "location");
			return this.initLocationSystemView(map);
		} else if (codeView.equalsIgnoreCase("ancestor")) {
			return this.initAncestorView(map);
		} else if (isFormView) {
			this.initMapWithoutView(map, "form");
			return this.initDocumentInForm(map);
		} else if (isSignsView) {
			this.initMapWithoutView(map, "signs");
			return this.initDocumentSignView(map);
		}

		this.initMap(map, null);
		this.initContent(map);

		return block("view", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.node.document");
		super.init();
	}

}
