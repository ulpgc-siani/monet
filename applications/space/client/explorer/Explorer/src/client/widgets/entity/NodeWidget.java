package client.widgets.entity;

import client.core.model.Node;
import client.core.model.NodeView;
import client.presenters.displays.entity.EnvironmentDisplay;
import client.presenters.displays.entity.NodeDisplay;
import client.services.TranslatorService;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRuleCheckingTags;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class NodeWidget extends HTMLPanel implements HoldAble {
	private final NodeDisplay display;
	private TextArea notes;

	private static final String BOX = "::layout::";
	private static final String LAYOUT = "::layout::";

	public NodeWidget(NodeDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;

		createComponents();
		refresh();
		hook();
	}

	@Override
	public void onHold(PresenterHolder holder) {
		setHeight("90%");
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(NodeDisplay.TYPE) || presenter.is(EnvironmentDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new NodeWidget((NodeDisplay) presenter, getLayout(presenter, layout), translator);
		}

		private String getLayout(Presenter presenter, String layout) {
			NodeDisplay<Node, NodeView> display = (NodeDisplay) presenter;
			Node node = display.getEntity();
			String definitionClass = node.getDefinitionClass();
			String nodeType = Node.Type.NODE.toString();
			String layoutName;

			if (presenter.is(EnvironmentDisplay.TYPE) && ((EnvironmentDisplay)presenter).isRoot()) {
				layoutName = layout + " " + definitionClass + (presenter.is(EnvironmentDisplay.TYPE) ? "-root" : "");

				if (!this.theme.existsLayout(layoutName))
					layoutName = layout + " " + nodeType + (presenter.is(EnvironmentDisplay.TYPE) ? "-root" : "");
			}
			else {
				String suffix = node.isComponent() ? "-component" : "";
				layoutName = layout + " " + definitionClass + suffix;

				if (!this.theme.existsLayout(layoutName))
					layoutName = layout + " " + nodeType + suffix;

				if (!this.theme.existsLayout(layoutName))
					layoutName = layout;
			}

			return layout.isEmpty() ? "<div class='error'>Layout not defined for node widget</div>" : this.theme.getLayout(layoutName);
		}
	}

	private void createComponents() {
		$(this.getElement()).addClass(display.getEntityType().toString(), Style.ENTITY, Style.VIEWS_COUNT + display.getViewsCount());
		createNotes();
		this.bind();
	}

	private void createNotes() {

		if (!this.existsNotesInTemplate())
			return;

		notes = new TextArea();
		notes.addStyleName(Style.NOTES);
		notes.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				notes.selectAll();
			}
		});
		notes.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				display.setNote(notes.getValue());
			}
		});
	}

	private void bind() {

		if (this.existsNotesInTemplate())
			bindWidgetToElement(this, notes, $(this.getElement()).find(toRuleCheckingTags(Style.NOTES, Style.NOTES_TEXT_AREA)).get(0));

		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private boolean existsNotesInTemplate() {
		return $(this.getElement()).find("." + Style.NOTES + " " + Style.NOTES_TEXT_AREA).length() > 0;
	}

	private void refresh() {
		this.refreshNotes(display.getNote());
	}

	private void refreshNotes(String notes) {
		if (this.notes == null)
			return;

		this.notes.setValue(notes);
	}

	private void hook() {
		display.addHook(new NodeDisplay.Hook() {
			@Override
			public void notes(String notes) {
				refreshNotes(notes);
			}

			@Override
			public void notesFailure() {
			}
		});
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(BOX.replaceAll(LAYOUT, layout));
	}

	private class Style {
		private static final String ENTITY = "entity node";
		private static final String VIEWS_COUNT = "vc";
		private static final String NOTES = "notes";
		private static final String NOTES_TEXT_AREA = "textarea";
	}
}
