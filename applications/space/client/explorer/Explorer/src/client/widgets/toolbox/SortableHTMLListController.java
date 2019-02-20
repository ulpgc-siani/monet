package client.widgets.toolbox;

import com.allen_sauer.gwt.dnd.client.*;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

public class SortableHTMLListController<T extends HTMLListWidget> {
	private final ComplexPanel boundary;
	private final T dropList;
	private DropController dropController;
	private PickupDragController dragController;
    private List<ChangePositionEvent> changePositionEvents;

    public SortableHTMLListController(ComplexPanel boundary, T dropList, Sortable sortable) {
		super();
		this.boundary = boundary;
		this.dropList = dropList;
        this.changePositionEvents = new ArrayList<>();
		build(sortable);
	}

	public SortableHTMLListController(ComplexPanel boundary, T dropList) {
		this(boundary, dropList, Sortable.VERTICAL);
	}

	public enum Sortable {
		VERTICAL {
			@Override
			protected DropController createDropController(InsertPanel dropContainer) {
				return new VerticalDropController(dropContainer);
			}
		},
		HORIZONTAL {
			@Override
			protected DropController createDropController(InsertPanel dropContainer) {
				return new HorizontalDropController(dropContainer);
			}
		};

		protected abstract DropController createDropController(InsertPanel dropContainer);
	}

	public void addChangePositionEvent(ChangePositionEvent<HTMLListWidget.ListItem> event) {
		changePositionEvents.add(event);
	}

    private void build(Sortable sortable) {
        createDragAndDropControllers(sortable);
        registerListeners();
    }

    private void createDragAndDropControllers(Sortable sortable) {
		createDropController(sortable);
		createDragController();
	}

    private void createDropController(Sortable sortable) {
        dropController = sortable.createDropController(dropList.getDropContainer());
    }

    private void createDragController() {
		dragController = createPickupDragController();

		dragController.setBehaviorConstrainedToBoundaryPanel(true);
        dragController.setBehaviorMultipleSelection(false);
        dragController.registerDropController(dropController);
        dragController.addDragHandler(new DragHandler() {
            @Override
            public void onDragEnd(DragEndEvent event) {
                final Widget widget = event.getContext().selectedWidgets.get(0);
                notifyPositionChange((HTMLListWidget.ListItem) widget, dropList.getDropContainer().getWidgetIndex(widget));
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						RootPanel.get().removeStyleName(StyleName.SORTING);
						removeAllFocus();
					}
				});
            }

            @Override
            public void onDragStart(DragStartEvent event) {
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						RootPanel.get().addStyleName(StyleName.SORTING);
						removeAllFocus();
					}
				});
            }

            @Override
            public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
            }

            @Override
            public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
            }
        });
    }

	private void removeAllFocus() {
		RootPanel.get().getElement().focus();
		RootPanel.get().getElement().blur();
	}

	private PickupDragController createPickupDragController() {
		if (boundary instanceof AbsolutePanel)
            return new PickupDragController((AbsolutePanel) boundary, false);
		return new PickupDragController(RootPanel.get(), false);
	}

    private void notifyPositionChange(HTMLListWidget.ListItem item, int newPosition) {
        for (ChangePositionEvent event : changePositionEvents)
            event.onPositionChange(item, newPosition);
    }

    private void registerListeners() {
        dropList.addStyleName(StyleName.SORTABLE);
		dropList.addAddHandler(new HTMLListWidget.AddHandler() {
			@Override
			public void onAdd(HTMLListWidget.ListItem item) {
				Label move = new Label("");
				move.addStyleName(StyleName.MOVE);
				item.addControlOperation(move);
				dragController.makeDraggable(item, move);
			}
		});
	}

    private static class StyleName {
        private static final String SORTABLE = "sortable";
        private static final String SORTING = "sorting";
	    private static final String MOVE = "move";
    }

    public interface ChangePositionEvent<ListItemType> {
        void onPositionChange(ListItemType item, int newPosition);
    }
}