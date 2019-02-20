package client.core.system.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public class FormViewDefinition extends NodeViewDefinition implements client.core.model.definition.views.FormViewDefinition {
	private client.core.model.definition.views.FormViewDefinition.ShowDefinition show;

	@Override
	public client.core.model.definition.views.FormViewDefinition.ShowDefinition getShow() {
		return show;
	}

	public void setShow(client.core.model.definition.views.FormViewDefinition.ShowDefinition show) {
		this.show = show;
	}

	public static class ShowDefinition implements client.core.model.definition.views.FormViewDefinition.ShowDefinition {
		private String layout;
		private String layoutExtended;
		private List<Ref> fields;

		@Override
		public String getLayout() {
			return layout;
		}

		@Override
		public String getLayoutExtended() {
			return layoutExtended;
		}

		public void setLayout(String layout) {
			this.layout = layout;
		}

		public void setLayoutExtended(String layout) {
			this.layoutExtended = layout;
		}

		@Override
		public List<Ref> getFields() {
			return fields;
		}

		public void setFields(List<Ref> fields) {
			this.fields = fields;
		}
	}
}
