package client.presenters.displays.entity;

import client.core.model.*;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.OperationListDisplay;
import client.presenters.displays.view.LazyLoadViewListDisplay;
import client.presenters.displays.view.NodeViewDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.operations.CompositeOperation;
import client.presenters.operations.ExecuteNodeCommandOperation;
import client.presenters.operations.ShowNodeOperation;
import client.services.TranslatorService;
import client.services.callback.NoteCallback;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class NodeDisplay<T extends Node, V extends NodeView> extends EntityDisplay<T, V> {

	public static final Type TYPE = new Type("NodeDisplay", EntityDisplay.TYPE);

	public static final String USER_NOTE = "userNote";

	protected NodeDisplay(T node, V view) {
		super(node, view);
		addViews();
		addOperations();
		addCurrentView();
	}

	@Override
	public String getLabel() {
		String label = getEntity().getLabel();

		if (!label.isEmpty())
			return label;

		return services.getTranslatorService().translate(TranslatorService.Label.NO_LABEL);
	}

	public int getViewsCount() {
		return getEntity().getViews().size();
	}

	public V getCurrentView() {

		if (getView() != null)
			return getView();

		ViewList<V> views = getEntity().getViews();
        return views.getDefaultView();
	}

	private void addViews() {
		Node node = getEntity();

		if (node.isComponent())
			return;

		ViewList<V> views = node.getViews();
		Map<V, ShowNodeOperation> operations = new HashMap<>();

		for (V nodeView : views)
			operations.put(nodeView, createShowNodeOperation(node, nodeView));

		LazyLoadViewListDisplay viewListDisplay = new LazyLoadViewListDisplay(node, views, operations);
		viewListDisplay.inject(services);
		viewListDisplay.setActiveView(getCurrentView());

		this.addChild(viewListDisplay);
	}

	private ShowNodeOperation createShowNodeOperation(Node node, V nodeView) {
		ShowNodeOperation operation = new ShowNodeOperation(getOperationContext(), node, nodeView);
		operation.inject(services);
		return operation;
	}

	private void addCurrentView() {
		Node node = getEntity();
		V currentView = getCurrentView();

		if (currentView == null)
			return;

		ViewDisplay display = new NodeViewDisplay.Builder<>().build(node, currentView);
		addChild(display);
	}

	protected void addOperations() {
		OperationListDisplay operationListDisplay = new OperationListDisplay();
		addUserCommandsAsOperations(operationListDisplay, getEntity().getCommands());
		addChild(operationListDisplay);
	}

	protected void addUserCommandsAsOperations(Presenter presenter, List<Command> commandList) {
		for (Command command : commandList)
			addUserCommandAsOperation(presenter, command);
	}

	protected void addUserCommandAsOperation(Presenter presenter, Command command) {

		if (command instanceof GroupCommand) {
			addUserGroupCommandToMenu(presenter, (GroupCommand) command);
			return;
		}

		presenter.addChild(createExecuteNodeCommandOperation(command));
	}

	private void addUserGroupCommandToMenu(Presenter presenter, GroupCommand command) {
		CompositeOperation operation = createCompositeOperation(command);
		addUserCommandsAsOperations(operation, command.getCommandList());
		presenter.addChild(operation);
	}

	private CompositeOperation createCompositeOperation(GroupCommand command) {
		CompositeOperation operation = new CompositeOperation(getOperationContext());
		operation.setEnabled(command.isEnabled());
		operation.setVisible(command.isVisible());
		operation.setLabel(command.getLabel());
		operation.inject(services);
		return operation;
	}

	private ExecuteNodeCommandOperation createExecuteNodeCommandOperation(Command command) {
		ExecuteNodeCommandOperation operation = new ExecuteNodeCommandOperation(getOperationContext(), command, getEntity());
		operation.setEnabled(command.isEnabled());
		operation.setVisible(command.isVisible());
		operation.inject(services);
		return operation;
	}

	public String getNote() {
		return getEntity().getNote(USER_NOTE);
	}

	public void setNote(final String value) {
		NoteCallback callback = new NoteCallback() {
			@Override
			public void success(Result note) {
				getEntity().addNote(note.getName(), value);
				notifyNotes(note.getValue());
			}

			@Override
			public void failure(String error) {
				notifyNotesSavingFailure(error);
			}
		};

		services.getNodeService().saveNote(getEntity(), USER_NOTE, value, callback);
	}

	public Node.Type getEntityType() {
		return getEntity().getType();
	}

	public static class Builder {
		public NodeDisplay build(Node node, NodeView view) {

			if (node.isCollection())
				return new CollectionDisplay((Collection) node, (CollectionView)view);

			if (node.isCatalog())
				return new CatalogDisplay((Catalog) node, (CatalogView) view);

			if (node.isContainer())
				return new ContainerDisplay((Container) node, (ContainerView)view);

			if (node.isForm())
				return new FormDisplay((Form) node, (FormView) view);

			if (node.isDesktop())
				return new DesktopDisplay((Desktop) node, (DesktopView) view);

			if (node.isDocument())
				return new DocumentDisplay((Document) node, (DocumentView) view);

			return null;
		}
	}

	public interface Hook extends EntityDisplay.Hook {
		void notes(String notes);
		void notesFailure();
	}

	private void notifyNotes(final String notes) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.notes(notes);
			}
		});
	}

	private void notifyNotesSavingFailure(String error) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not save notes for node " + this.getEntity().getId());
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.notesFailure();
			}
		});
	}

}
