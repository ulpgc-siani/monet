package client.widgets;

import client.presenters.displays.AccountDisplay;
import client.services.TranslatorService;
import client.widgets.popups.PopUpWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.gwt.utils.StyleUtils;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;
import cosmos.utils.StringUtils;

import static com.google.gwt.dom.client.Style.Unit;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.StyleUtils.toRuleCheckingTags;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class AccountWidget extends CosmosHtmlPanel {
	private final AccountDisplay display;
	private final TranslatorService translator;
	private Image photo;
	private Dialog dialog;
	private Anchor businessUnitListCommand;
	private Anchor notificationListCommand;
	private int lastActiveDialog = 0;

	public AccountWidget(AccountDisplay display, String layout, TranslatorService translator) {
		super(translator.translateHTML(layout));
		addStyleName(StyleName.WIDGET);
		this.display = display;
		this.translator = translator;
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		createPhoto();
		createDialog();
		createVerticalToolbar();

		bind();
	}

	private void createPhoto() {
		photo = new Image();
	}

	private void createDialog() {
		dialog = new Dialog();
	}

	private void createVerticalToolbar() {
		createBusinessUnitListCommand();
		createNotificationListCommand();
	}

	private void createBusinessUnitListCommand() {
		businessUnitListCommand = new Anchor();
		businessUnitListCommand.setTitle(translator.translate(TranslatorService.OperationLabel.SHOW_BUSINESS_UNIT_LIST));
		businessUnitListCommand.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (dialog.isVisible() && lastActiveDialog == 0)
					dialog.hide();
				else
					showBusinessUnitList();

				lastActiveDialog = 0;

				event.stopPropagation();
			}
		});
	}

	private void createNotificationListCommand() {
		notificationListCommand = new Anchor();
		notificationListCommand.setTitle(translator.translate(TranslatorService.OperationLabel.SHOW_NOTIFICATION_LIST));
		notificationListCommand.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (dialog.isVisible() && lastActiveDialog == 1)
					dialog.hide();
				else
					showNotificationList();

				lastActiveDialog = 1;

				event.stopPropagation();
			}
		});
	}

	private void showNotificationList() {
		dialog.show();
		dialog.showNotificationList();
	}

	private void showBusinessUnitList() {
		dialog.show();
		dialog.showBusinessUnitList();
	}

	private void refresh() {
		refreshPhoto();
		refreshEmail();
		refreshFullName();
	}

	private void refreshPhoto() {
		photo.setUrl(display.getPhoto());
		if (display.isDefaultPhoto())
			photo.addStyleName(StyleName.DEFAULT);
		else
			photo.removeStyleName(StyleName.DEFAULT);
	}

	private void refreshEmail() {
		$(getElement()).find(toRule(StyleName.EMAIL)).html(display.getEmail());
	}

	private void refreshFullName() {
		$(getElement()).find(toRule(StyleName.FULL_NAME))
                .html(StringUtils.shortContent(display.getFullName(), 25))
                .prop("title", display.getFullName());
	}

	private void hook() {
		display.addHook(new AccountDisplay.Hook() {
			@Override
			public void update() {
				refresh();
			}
		});
	}

	private void bind() {
        bind(photo, toRuleCheckingTags(StyleName.PHOTO, StyleUtils.IMAGE));
        bindKeepingStyles(businessUnitListCommand, toRule(StyleName.BUSINESS_UNIT_LIST_COMMAND));
        bindKeepingStyles(notificationListCommand, toRule(StyleName.NOTIFICATION_LIST_COMMAND));
        bindKeepingContent(dialog, toRule(StyleName.DIALOG));

        onAttach();
		RootPanel.detachOnWindowClose(this);
	}

    public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(AccountDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new AccountWidget((AccountDisplay) presenter, getLayout(layout), translator);
		}

		private String getLayout(String layout) {
			return layout.isEmpty() ? "<div class='info'><div class='username'></div></div>" : theme.getLayout(layout);
		}
	}

	private class Dialog extends PopUpWidget<HTMLPanel> {

		public Dialog() {
			super();
			locate();
			createSizeCalculator();
			init();
		}

		@Override
		protected HTMLPanel createContent(Element container) {
			HTMLPanel content = new HTMLPanel("");
			bindWidgetToElement(this, content, container);
			return content;
		}

		private void createSizeCalculator() {
			setSizeCalculator(new PopUpWidget.SizeCalculator() {
				@Override
				public int getWidth() {
					return -1;
				}

				@Override
				public int getHeight() {
					return -1;
				}
			});
		}

		@Override
		public void show() {
			super.show();
			locate();
		}

		public void showNotificationList() {
			$(getElement()).find(toRule(AccountWidget.StyleName.BUSINESS_UNIT_LIST)).hide();
			$(getElement()).find(toRule(AccountWidget.StyleName.NOTIFICATION_LIST)).show();
		}

		public void showBusinessUnitList() {
			$(getElement()).find(toRule(AccountWidget.StyleName.BUSINESS_UNIT_LIST)).show();
			$(getElement()).find(toRule(AccountWidget.StyleName.NOTIFICATION_LIST)).hide();
		}

		private void locate() {
			com.google.gwt.dom.client.Style style = getStyleElement().getStyle();
			style.setRight(0, Unit.PX);
			style.setMarginRight(-(getOffsetWidth()+14), Unit.PX);
		}
	}

	private interface StyleName {
		String DEFAULT = "default";
		String EMAIL = "email";
		String FULL_NAME = "full-name";
		String WIDGET = "account";
		String DIALOG = "dialog";
		String PHOTO = "photo";
		String NOTIFICATION_LIST = "notification-list-widget";
		String NOTIFICATION_LIST_COMMAND = "show-notifications";
		String BUSINESS_UNIT_LIST = "business-unit-list-widget";
		String BUSINESS_UNIT_LIST_COMMAND = "show-business-units";
	}
}
