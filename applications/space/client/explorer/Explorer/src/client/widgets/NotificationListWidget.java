package client.widgets;

import client.core.model.List;
import client.core.model.Notification;
import client.presenters.displays.NotificationListDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.toolbox.LoadingMessage;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;
import static com.google.gwt.dom.client.Style.Display;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class NotificationListWidget extends HTMLPanel {
	private final NotificationListDisplay display;
	private final TranslatorService translator;
	private LoadingMessage loadingWidget;
	private HTMLListWidget<Notification> notificationListWidget;

	public NotificationListWidget(NotificationListDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.translator = translator;
		addStyleName(Style.WIDGET_NAME);
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		add(createLoading());
		createNotificationList();

		bind();
	}

	private Widget createLoading() {
		loadingWidget = new LoadingMessage(translator);
		loadingWidget.setVisible(true);
		return loadingWidget;
	}

	private void createNotificationList() {
		notificationListWidget = new HTMLListWidget<>(new NotificationItem.Builder(), translator, "");
		notificationListWidget.addStyleName(Style.NOTIFICATIONS);
		notificationListWidget.setTitle(translator.translate(TranslatorService.Label.NOTIFICATIONS));
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			load();
	}

	private void load() {
		refreshVisibility(-1);
		display.loadNotifications();
	}

	private void refresh() {
		refreshVisibility(-1);
	}

	private void refreshNotifications(List<Notification> notificationList) {
		refreshVisibility(notificationList.size());

		notificationListWidget.clear();
		for (Notification notification : notificationList)
			notificationListWidget.addItem(notification);
	}

	public void refreshVisibility(int countNotifications) {
		Element emptyNotificationsPanel = $(this.getElement()).find(toRule(Style.EMPTY_NOTIFICATIONS)).get(0);
		com.google.gwt.dom.client.Style emptyStyle = emptyNotificationsPanel.getStyle();

		if (countNotifications == -1) {
			loadingWidget.setVisible(true);
			emptyStyle.setDisplay(Display.NONE);
			notificationListWidget.setVisible(false);
			return;
		}

		loadingWidget.setVisible(false);

		if (countNotifications == 0) {
			emptyStyle.setDisplay(Display.BLOCK);
			notificationListWidget.setVisible(false);
			return;
		}

		emptyStyle.setDisplay(Display.NONE);
		notificationListWidget.setVisible(true);
	}

	private void hook() {
		display.addHook(new NotificationListDisplay.Hook() {
			@Override
			public void notifications(List<Notification> notificationList) {
				refreshVisibility(notificationList.size());
				refreshNotifications(notificationList);
			}
		});
	}

	private void bind() {
		bindWidgetToElement(this, notificationListWidget, $(this.getElement()).find(toRule(Style.NOTIFICATIONS)).get(0));

		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(NotificationListDisplay.TYPE);
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new NotificationListWidget((NotificationListDisplay) presenter, theme.getLayout(layout), translator);
		}

	}

	private static class NotificationItem extends HTMLListWidget.ListItem<Notification> {

		public NotificationItem(Notification value) {
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
			label.addStyleName(NotificationListWidget.Style.COMMAND);
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

		public static class Builder extends HTMLListWidget.ListItem.Builder<Notification> {
			@Override
			public HTMLListWidget.ListItem build(Notification value, HTMLListWidget.Mode mode) {
				return new NotificationItem(value);
			}
		}
	}

	private interface Style {
		String WIDGET_NAME = "notification-list-widget";
		String COMMAND = "command";
		String NOTIFICATIONS = "notifications";
		String EMPTY_NOTIFICATIONS = "empty-notifications";
	}

}
