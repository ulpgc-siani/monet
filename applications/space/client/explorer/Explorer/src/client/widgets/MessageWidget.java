package client.widgets;

import client.presenters.displays.MessageDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;

public class MessageWidget extends HTML {
	private final MessageDisplay display;
	private TranslatorService translator;
	private static final PopupPanel popup = new PopupPanel(false, true);

	public MessageWidget(MessageDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.translator = translator;
		createComponents();
		hook();
	}

	private void createComponents() {
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                centerMessage();
            }
        });
		bind();
	}

	private void bind() {
		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void alert(String title, String message, MessageDisplay.AlertCallback callback) {
		new AlertDialog(title, message, callback);
	}

	private void confirm(String title, String message, MessageDisplay.ConfirmationCallback callback) {
		new ConfirmDialog(title, message, callback);
	}

	private void alertWithTimeout(String title, String message) {
		final Dialog dialog = new AlertDialog(title, message);
		int closeDialogAfterMillis = 4000;
		final Timer timer = new Timer() {
			@Override
			public void run() {
				dialog.hide();
			}
		};
		$(dialog).mouseenter(new Function() {
			@Override
			public void f() {
				timer.cancel();
			}
		});
		dialog.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				timer.cancel();
			}
		}, MouseOverEvent.getType());
		timer.schedule(closeDialogAfterMillis);
	}

	private void showLoading(String message) {
		popup.clear();
		popup.add(new Label(message));
		popup.setGlassEnabled(true);
		popup.center();
	}

	private void hideLoading() {
		popup.hide();
	}

	private void showMessage(String title, String message) {
		setText(message);
		setTitle(title);
		setVisible(true);
        centerMessage();
    }

    private void centerMessage() {
        getElement().getStyle().setMarginLeft((RootPanel.get().getOffsetWidth() - getOffsetWidth()) / 2, Style.Unit.PX);
    }

    private void hideMessage() {
		setVisible(false);
	}

	private void hook() {
		display.addHook(new MessageDisplay.Hook() {
			@Override
			public void alert(String title, String message, MessageDisplay.AlertCallback callback) {
				MessageWidget.this.alert(title, message, callback);
			}

			@Override
			public void confirm(String title, String message, MessageDisplay.ConfirmationCallback callback) {
				MessageWidget.this.confirm(title, message, callback);
			}

			@Override
			public void alertWithTimeout(String title, String message) {
				MessageWidget.this.alertWithTimeout(title, message);
			}

			@Override
			public void showLoading(String message) {
				MessageWidget.this.showLoading(message);
			}

			@Override
			public void hideLoading() {
				MessageWidget.this.hideLoading();
			}

			@Override
			public void showMessage(String title, String message) {
				MessageWidget.this.showMessage(title, message);
			}

			@Override
			public void hideMessage() {
				MessageWidget.this.hideMessage();
			}
		});
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(MessageDisplay.TYPE);
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new MessageWidget((MessageDisplay) presenter, "", translator);
		}

	}

	public interface StyleName {
		String DIALOG = "dialog";
		String TOOLBAR = "toolbar";
		String COMMAND = "command";
		String BUTTON = "button";
	}

	private abstract class Dialog extends DialogBox {

		public Dialog(String title, String message) {
			init(title, message);
		}

		private void init(String title, String message) {
			addStyleName(StyleName.DIALOG);

			VerticalPanel content = new VerticalPanel();
			content.setSpacing(4);

			addMessage(content, message);
			addToolbar(content);

			setText(title);
			setWidget(content);
			setGlassEnabled(true);
			setAnimationEnabled(true);
			center();
			show();
		}

		protected void addMessage(VerticalPanel container, String message) {
			HTML messageBox = new HTML(message);
			container.add(messageBox);
			container.setCellHorizontalAlignment(messageBox, HasHorizontalAlignment.ALIGN_CENTER);
		}

		protected void addToolbar(VerticalPanel container) {
			HorizontalPanel toolbar = new HorizontalPanel();
			toolbar.addStyleName(StyleName.TOOLBAR);

			container.add(toolbar);
			container.setCellHorizontalAlignment(toolbar, LocaleInfo.getCurrentLocale().isRTL()?HasHorizontalAlignment.ALIGN_LEFT:HasHorizontalAlignment.ALIGN_RIGHT);

			addToolbarOperations(toolbar);
		}

		protected IsWidget createCommand(Button button) {
			HTMLPanel command = new HTMLPanel("");
			$(command).addClass(StyleName.COMMAND, StyleName.BUTTON);
			command.add(button);
			return command;
		}

		protected abstract void addToolbarOperations(HorizontalPanel toolbar);

	}

	private class ConfirmDialog extends Dialog {
		private final MessageDisplay.ConfirmationCallback callback;

		public ConfirmDialog(String title, String message, MessageDisplay.ConfirmationCallback callback) {
			super(title, message);
			this.callback = callback;
		}

		@Override
		protected void addToolbarOperations(HorizontalPanel toolbar) {
			toolbar.add(createAcceptCommand());
			toolbar.add(createCancelCommand());
		}

		protected IsWidget createAcceptCommand() {
			return createCommand(new Button(
				translator.translate(TranslatorService.OperationLabel.ACCEPT), new ClickHandler() {
				public void onClick(ClickEvent event) {
					ConfirmDialog.this.hide();
					callback.accept();
				}
			}));
		}

		private IsWidget createCancelCommand() {
			return createCommand(new Button(
				translator.translate(TranslatorService.OperationLabel.CANCEL), new ClickHandler() {
				public void onClick(ClickEvent event) {
					ConfirmDialog.this.hide();
					callback.cancel();
				}
			}));
		}

	}

	private class AlertDialog extends Dialog {
		private MessageDisplay.AlertCallback callback;

		public AlertDialog(String title, String message) {
			super(title, message);
		}

		public AlertDialog(String title, String message, MessageDisplay.AlertCallback callback) {
			super(title, message);
			this.callback = callback;
		}

		@Override
		protected void addToolbarOperations(HorizontalPanel toolbar) {
			toolbar.add(createCloseCommand());
		}

		protected IsWidget createCloseCommand() {
			return createCommand(new Button(
				translator.translate(TranslatorService.OperationLabel.CLOSE_DIALOG), new ClickHandler() {
				public void onClick(ClickEvent event) {
					AlertDialog.this.hide();
					if (callback != null) callback.close();
				}
			}));
		}
	}


}
