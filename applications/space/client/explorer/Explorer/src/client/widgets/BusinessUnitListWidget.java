package client.widgets;

import client.core.model.BusinessUnit;
import client.presenters.displays.BusinessUnitListDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.toolbox.LoadingMessage;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class BusinessUnitListWidget extends HTMLPanel {
	private final BusinessUnitListDisplay display;
	private final TranslatorService translator;
	private HTMLListWidget<BusinessUnit> memberListWidget;
	private LoadingMessage loadingWidget;
	private boolean loaded = false;

	public BusinessUnitListWidget(BusinessUnitListDisplay display, String layout, TranslatorService translator) {
		super(getHtml(display, layout, translator));
		this.display = display;
		this.translator = translator;
		this.addStyleName(Style.WIDGET_NAME);
		this.createComponents();
		this.hook();
		this.refresh();
	}

	private void createComponents() {
		add(createLoading());
		createMembersList();

		bind();
	}

	private Widget createLoading() {
		loadingWidget = new LoadingMessage(translator);
		loadingWidget.setVisible(true);
		return loadingWidget;
	}

	private Widget createMembersList() {
		memberListWidget = new HTMLListWidget<>(new BusinessUnitItem.Builder(), translator, translator.translate(TranslatorService.Label.EMPTY_MEMBERS));
		memberListWidget.addStyleName(Style.MEMBERS);
		memberListWidget.setTitle(translator.translate(TranslatorService.Label.MEMBERS));
		memberListWidget.addClickHandler(new HTMLListWidget.ClickHandler<BusinessUnit>() {
			@Override
			public void onClick(int index, BusinessUnit value) {
				showBusinessUnit(value);
			}
		});

		return memberListWidget;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible && !loaded)
			load();
	}

	private void showBusinessUnit(BusinessUnit value) {
		display.showBusinessUnit(value);
	}

	private void refresh() {
		loadingWidget.setVisible(true);
	}

	private void load() {
		display.loadMembers();
		loaded = true;
	}

	private void refreshMembers(BusinessUnit[] members) {
		memberListWidget.clear();
		for (BusinessUnit member : members)
			memberListWidget.addItem(member);
	}

	private void hook() {
		display.addHook(new BusinessUnitListDisplay.Hook() {
			@Override
			public void members(BusinessUnit[] members) {
				loadingWidget.setVisible(false);
				refreshMembers(members);
			}

			@Override
			public void partners(BusinessUnit[] partners) {
			}
		});
	}

	private void bind() {
		bindWidgetToElement(this, memberListWidget, $(this.getElement()).find(toRule(Style.MEMBERS)).get(0));

		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(BusinessUnitListDisplay display, String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(BusinessUnitListDisplay.TYPE);
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new BusinessUnitListWidget((BusinessUnitListDisplay) presenter, theme.getLayout(layout), translator);
		}

	}

	private static class BusinessUnitItem extends HTMLListWidget.ListItem<BusinessUnit> {

		public BusinessUnitItem(BusinessUnit value) {
			super(value);
			init();
		}

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
			Anchor label = new Anchor(value.getLabel());
			label.addStyleName(BusinessUnitListWidget.Style.COMMAND);
			label.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
				}
			});
			return label;
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}

		public static class Builder extends HTMLListWidget.ListItem.Builder<BusinessUnit> {
			@Override
			public HTMLListWidget.ListItem build(BusinessUnit value, HTMLListWidget.Mode mode) {
				return new BusinessUnitItem(value);
			}
		}
	}

	private interface Style {
		String WIDGET_NAME = "business-unit-list-widget";
		String MEMBERS = "members";
		String COMMAND = "command";
	}

}
