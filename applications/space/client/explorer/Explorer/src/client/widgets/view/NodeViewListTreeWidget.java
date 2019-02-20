package client.widgets.view;

import client.core.model.definition.Type;
import client.core.model.List;
import client.core.system.MonetList;
import client.presenters.displays.view.CollectionViewDisplay;
import client.presenters.displays.view.NodeViewDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.displays.view.ViewListDisplay;
import client.presenters.operations.ShowNodeOperation;
import client.services.TranslatorService;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.*;
import cosmos.presenters.Presenter;

public class NodeViewListTreeWidget extends HTMLPanel {
	private final ViewListDisplay display;
	private final TranslatorService translator;
	private ListDataProvider<NodeViewDisplay> dataProvider;

	public NodeViewListTreeWidget(ViewListDisplay display, String layout, TranslatorService translator) {
		super("");
		this.getElement().addClassName("tree");
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		this.setHeight("500px");
		this.setWidth("100%");
		this.dataProvider = new ListDataProvider<>();
		this.bind();
	}

	private void refresh() {
		this.clear();

		refreshDataProvider();

		CellBrowser cellBrowser = new CellBrowser.Builder<ViewTreeModel>(new ViewTreeModel(dataProvider), null).build();
		cellBrowser.setAnimationEnabled(true);
		cellBrowser.setMinimumColumnWidth(300);

		this.add(cellBrowser);
	}

	private void refreshDataProvider() {
		java.util.List<NodeViewDisplay> list = this.dataProvider.getList();

		list.clear();

		for (ViewDisplay display : this.display.getViews())
            if (display instanceof CollectionViewDisplay)
			    list.add((CollectionViewDisplay)display);
	}

	private void hook() {
		this.display.addHook(new ViewListDisplay.Hook() {
			@Override
			public void activate(ViewDisplay viewDisplay) {
			}
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(ViewListDisplay.TYPE) && design.equals("tree");
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new NodeViewListTreeWidget((ViewListDisplay) presenter, getLayout(layout), translator);
		}

		private String getLayout(String layout) {
			return layout.isEmpty() ? "" : this.theme.getLayout(layout);
		}
	}

	private static class NodeViewDisplayCell extends AbstractCell<NodeViewDisplay> {
		@Override
		public void render(Context context, NodeViewDisplay value, SafeHtmlBuilder sb) {
			if (value == null) return;
			sb.appendHtmlConstant("<div>" + value.getLabel() + "</div>");
		}
	}

	private static class TypeCount {
		private final CollectionViewDisplay display;
		private final Type type;

		public TypeCount(CollectionViewDisplay display, Type type) {
			this.display = display;
			this.type = type;
		}
	}

	private static class TypeCountCell extends AbstractCell<TypeCount> {
		@Override
		public void render(Context context, TypeCount value, SafeHtmlBuilder sb) {
			if (value == null) return;
			sb.appendHtmlConstant("<div>" + value.type.getLabel() + "</div>");
		}
	}

	private static class ShowNodeOperationCell extends AbstractCell<ShowNodeOperation> {

		@Override
		public void render(Context context, final ShowNodeOperation operation, SafeHtmlBuilder sb) {
			if (operation == null)
				return;
			sb.appendHtmlConstant("<div>" + operation.getLabel() + "</div>");
		}
	}

	private static class ViewTreeModel implements TreeViewModel {
		private ListDataProvider<NodeViewDisplay> dataProvider;
		private SingleSelectionModel<ShowNodeOperation> selectionModel;

		public ViewTreeModel(ListDataProvider<NodeViewDisplay> provider) {
			this.dataProvider = provider;
			this.selectionModel = createSelectionModel();
		}

		@Override
		public <T> NodeInfo<?> getNodeInfo(T value) {
			if (value == null) {
				return new DefaultNodeInfo<>(dataProvider, new NodeViewDisplayCell());
			} else if (value instanceof CollectionViewDisplay) {
				CollectionViewDisplay collectionViewDisplay = (CollectionViewDisplay) value;
				List<Type> nodeTypes = new MonetList<>();//collectionViewDisplay.getNodeTypes();
				List<TypeCount> counts = new MonetList<>();
				for (Type type : nodeTypes) {
					TypeCount count = new TypeCount(collectionViewDisplay, type);
					counts.add(count);
				}
				java.util.List<TypeCount> orderedCounts = new MonetList<>(counts);
				return new DefaultNodeInfo<>(new ListDataProvider<>(orderedCounts), new TypeCountCell());
			} else if (value instanceof TypeCount) {
				TypeCount count = (TypeCount) value;
				java.util.List<ShowNodeOperation> showNodeOperations = count.display.getShows();
				ListDataProvider<ShowNodeOperation> dataProvider = new ListDataProvider<>(showNodeOperations);
				ShowNodeOperationCell cell = new ShowNodeOperationCell();
				return new DefaultNodeInfo<>(dataProvider, cell, selectionModel, getSelectionManager(), null);
			}

			String type = value.getClass().getName();
			throw new IllegalArgumentException("Unsupported object type: " + type);
		}

		private SingleSelectionModel<ShowNodeOperation> createSelectionModel() {
			final SingleSelectionModel<ShowNodeOperation> selectionModel = new SingleSelectionModel<>();
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					selectionModel.getSelectedObject().execute();
				}
			});
			return selectionModel;
		}

		private CellPreviewEvent.Handler<ShowNodeOperation> getSelectionManager() {
			return DefaultSelectionEventManager.createDefaultManager();
		}

		@Override
		public boolean isLeaf(Object value) {

            if (value instanceof NodeViewDisplay) {
				return !(value instanceof CollectionViewDisplay);
			}

			if (value instanceof TypeCount)
				return (((TypeCount) value).display.getShows().size() == 0);

			return true;
		}

	}

}
