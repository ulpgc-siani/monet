package client.widgets.view;

import client.core.model.List;
import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.view.FormViewDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.gwt.widgets.Focusable;
import cosmos.gwt.widgets.LayoutRendererWidget;
import cosmos.gwt.widgets.LayoutWidget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class FormViewWidget extends CosmosHtmlPanel {

	private static final String BOX = "<div class='node entity form'>::layout::</div>";
	private static final String LAYOUT = "::layout::";

	private final FormViewDisplay display;
	private LayoutWidget fieldsPanel;
	private LayoutWidget extendedFieldsPanel;

	private HTMLPanel showAll;
	private InlineHTML showAllIcon;
	private Timer lastSaveTimer;

	public FormViewWidget(FormViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		create();
		refresh();
		hook();
        setId(display.getId());
	}

	private void create() {
		createLabel();
		createFieldsPanel();
		createExtendedFieldsPanel();
		createShowAll();
		bind();
	}

	private void createLabel() {
		$(getElement()).find(toRule(StyleName.LABEL)).html(display.getLabel());
	}

	private void createFieldsPanel() {
		fieldsPanel = display.hasLayout() ? new LayoutRendererWidget(display.getLayout()) : new LayoutWidget();
		fieldsPanel.addStyleName(StyleName.FIELDS);
		fieldsPanel.setNavigationHandler(new Focusable.NavigationHandler() {
			@Override
			public void onNavigate(Key key) {
				if (key == Key.ARROW_DOWN) extendedFieldsPanel.focusFirst();
			}
		});
		addFields(fieldsPanel, display.getNonExtendedFields());
	}

	private void createExtendedFieldsPanel() {
		extendedFieldsPanel = display.hasLayoutExtended() ? new LayoutRendererWidget(display.getLayoutExtended()) : new LayoutWidget();
		extendedFieldsPanel.setNavigationHandler(new Focusable.NavigationHandler() {
			@Override
			public void onNavigate(Key key) {
				if (key == Key.ARROW_UP) fieldsPanel.focusLast();
			}
		});
		extendedFieldsPanel.addStyleName(StyleName.FIELDS);
		addFields(extendedFieldsPanel, display.getExtendedFields());
	}

	private void addFields(LayoutWidget container, List<FieldDisplay> displays) {
		for (FieldDisplay fieldDisplay : displays)
			buildWidget(container, fieldDisplay);
	}

	private void buildWidget(LayoutWidget container, FieldDisplay fieldDisplay) {
		cosmos.gwt.presenters.Presenter.Builder builder = Builder.getFieldBuilder();
		if (!builder.canBuild(fieldDisplay, null)) return;

		FieldWidget widget = (FieldWidget) builder.build(fieldDisplay, null, null);
		if (widget == null) return;
		container.add(widget);
	}

	private void createShowAll() {
		showAllIcon = new InlineHTML();
		showAllIcon.setStyleName(StyleName.SHOW_ALL_ICON);
		showAll = new HTMLPanel("");
		showAll.addDomHandler(showAllClickHandler(), ClickEvent.getType());
		showAll.setStyleName(StyleName.SHOW_ALL);
		showAll.setVisible(display.hasExtendedFields());
		showAll.add(showAllIcon);
	}

	private ClickHandler showAllClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				$(showAllIcon).toggleClass(StyleName.SHOW_ALL_ICON, StyleName.SHOW_LESS_ICON);
				display.toggleShowMore();
			}
		};
	}

	private void bind() {
		bind(fieldsPanel, toRule(StyleName.FIELDS));
		bind(extendedFieldsPanel, toRule(StyleName.EXTENDED_FIELDS));
		bind(showAll, $(getElement()).find(toRule(StyleName.FORM)).children(toRule(StyleName.SHOW_ALL)).get(0));
		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void refresh() {
		showAll.getElement().setInnerHTML(showAllIcon.getElement().getString() + display.getShowAllLabel());
		extendedFieldsPanel.setVisible(display.getShowAllValue());
	}

	private void hook() {
		display.addHook(new FormViewDisplay.Hook() {

			@Override
			public void show() {
				refresh();
			}

			@Override
			public void failure(String error) {
			}

			@Override
			public void scroll() {
                if (display.getShowAllValue())
					display.scroll(showAll.getAbsoluteTop() - 50);
				else
					display.scroll(fieldsPanel.getAbsoluteTop() - 50);
			}

			@Override
			public void timer(int delay) {
				if (lastSaveTimer == null) createTimer();
				lastSaveTimer.schedule(delay);
			}

			@Override
			public void cancelTimer() {
				if (lastSaveTimer != null) lastSaveTimer.cancel();
			}
		});
	}

	private void createTimer() {
		lastSaveTimer = new Timer() {
			@Override
			public void run() {
				display.refreshMessageWhenSave();
			}
		};
	}

	private static String getHtml(String layout, TranslatorService translator) {
		return translator.translateHTML(BOX.replaceAll(LAYOUT, layout));
	}

	public static class Builder extends NodeViewWidget.Builder {
		public static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(FormViewDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new FormViewWidget((FormViewDisplay) presenter, getLayout(StyleName.VIEW_FORM, layout), translator);
		}
	}

	public interface StyleName {
		String EXTENDED_FIELDS = "extended-fields";
		String FIELDS = "fields";
		String FORM = "form";
		String LABEL = "label";
		String SHOW_ALL = "show-all";
		String SHOW_ALL_ICON = "show-all-icon";
		String SHOW_LESS_ICON = "show-less-icon";
		String VIEW_FORM = "view-form";
	}
}
