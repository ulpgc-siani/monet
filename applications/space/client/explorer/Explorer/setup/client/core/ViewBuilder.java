package client.core;

import client.core.definitions.FormViewDefinitionBuilder;
import client.core.model.*;
import client.core.model.definition.views.DocumentViewDefinition;
import client.core.model.definition.views.SetViewDefinition;
import client.core.model.definition.views.ViewDefinition;
import client.core.system.Key;
import client.core.system.MonetList;
import client.core.system.definition.views.ContainerViewDefinition;

public class ViewBuilder {

	public static DesktopView buildDesktopView(String code, String label, Node scope, boolean isDefault, Entity[] shows) {
		client.core.system.DesktopView view = new client.core.system.DesktopView(new Key(code), label, isDefault, scope);
		view.setShows(new MonetList<>(shows));
		return view;
	}

	public static FormView buildFormView(String code, String label, Node scope, boolean isDefault, Field[] shows) {
		client.core.system.FormView view = new client.core.system.FormView(new Key(code), label, isDefault, scope);
		view.setShows(new MonetList<>(shows));
		return view;
	}

	public static FormView buildFormViewLayout(String code, String label, Node scope, boolean isDefault, Field[] shows) {
		client.core.system.FormView view = new client.core.system.FormView(new Key(code), label, isDefault, scope);
		view.setShows(new MonetList<>(shows));
		view.setDefinition(FormViewDefinitionBuilder.buildLayout());
		return view;
	}

	public static CollectionView buildCollectionView(String code, final String label, Node scope, boolean isDefault, final ViewDefinition.Design design) {
		client.core.system.CollectionView view = new client.core.system.CollectionView(new Key(code), label, isDefault, scope);
		view.setDefinition(new SetViewDefinition() {
			@Override
			public ShowDefinition getShow() {
				return null;
			}

			@Override
			public AnalyzeDefinition getAnalyze() {
				return null;
			}

			@Override
			public SelectDefinition getSelect() {
				return null;
			}

			@Override
			public boolean isDefault() {
				return false;
			}

			@Override
			public Design getDesign() {
				return design;
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return SetViewDefinition.CLASS_NAME;
			}
		});
		return view;
	}

	public static CollectionView buildCollectionView(String code, String label, Node scope, boolean isDefault) {
		return buildCollectionView(code, label, scope, isDefault, ViewDefinition.Design.LIST);
	}

	public static TaskView buildTaskStateView(String code, String label) {
		return new client.core.system.TaskStateView(new Key(code), label, false);
	}

	public static TaskListView buildTaskListView(String code, String label, boolean isDefault, TaskList.Situation situation) {
		return new client.core.system.TaskListView(new Key(code), label, isDefault, situation);
	}

	public static DocumentView buildDocumentView(final String code, final String label, Node scope, final boolean isDefault) {
		final DocumentView documentView = new client.core.system.DocumentView(new Key(code), label, isDefault, scope);
		documentView.setDefinition(new DocumentViewDefinition() {
			@Override
			public boolean isDefault() {
				return isDefault;
			}

			@Override
			public Design getDesign() {
				return null;
			}

			@Override
			public String getCode() {
				return code;
			}

			@Override
			public String getName() {
				return code;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return DocumentView.CLASS_NAME;
			}
		});
		return documentView;
	}

	public static NodeView buildContainerView(String code, String label, NodeView hostView, Node scope, boolean isDefault) {
		final ContainerView containerView = new client.core.system.ContainerView(new Key(code), label, isDefault, scope, hostView);
		client.core.system.definition.views.ContainerViewDefinition definition = new ContainerViewDefinition();
		definition.setDefault(isDefault);
		containerView.setDefinition(definition);
		return containerView;
	}
}
