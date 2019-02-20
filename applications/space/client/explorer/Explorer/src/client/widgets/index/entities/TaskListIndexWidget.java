package client.widgets.index.entities;

import client.core.model.*;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.TaskListIndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexWidget;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.utils.StyleUtils;

import static client.widgets.toolbox.HTMLListWidget.Mode;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.*;
import static cosmos.utils.StringUtils.shortContent;

public abstract class TaskListIndexWidget extends IndexWidget<TaskListIndexDisplay, TaskListIndexEntry> {

	public TaskListIndexWidget(TaskListIndexDisplay display, LayoutHelper layoutHelper, TranslatorService translator) {
		super(display, layoutHelper, translator);
	}

	@Override
	protected IndexItem.Builder getBuilder() {
		return new TaskListIndexItem.Builder(display.getAccount(), new TaskListIndexItem.Handler() {
			@Override
			public void unSetTaskOwner(TaskListIndexEntry entry) {
				display.setTaskOwner(entry, null);
			}

			@Override
			public void setTaskUrgent(TaskListIndexEntry entry) {
				display.setTaskUrgent(entry, true);
			}

			@Override
			public void unSetTaskUrgent(TaskListIndexEntry entry) {
				display.setTaskUrgent(entry, false);
			}

		}, layoutHelper, translator);
	}

	@Override
	protected void hook() {
		display.addHook(new TaskListIndexDisplay.Hook() {
			@Override
			public void owner(TaskListIndexEntry entry) {
				TaskListIndexItem item = (TaskListIndexItem) list.getItem(entry);
				item.refresh();
			}

			@Override
			public void urgent(TaskListIndexEntry entry) {
				TaskListIndexItem item = (TaskListIndexItem) list.getItem(entry);
				item.refresh();
			}

			@Override
			public void clear() {
				clearPages();
			}

			@Override
			public void loadingPage() {
			}

			@Override
			public void page(IndexDisplay.Page<TaskListIndexEntry> page) {
				addPage(page);
			}

			@Override
			public void pagesCount(int count) {
			}

			@Override
			public void pageEntryAdded(TaskListIndexEntry entry) {
				addPageEntry(entry);
			}

			@Override
			public void pageEntryDeleted() {
			}

			@Override
			public void pageFailure() {
			}

			@Override
			public void pageEntryUpdated(TaskListIndexEntry entry) {
			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {
				facet.refreshEntityView(display.getActive(), entityViewDisplay);
			}

			@Override
			public void selectEntries(List<TaskListIndexEntry> entries) {
			}

			@Override
			public void selectOptions(Filter filter) {
				refreshFilters();
			}

			@Override
			public void loadingOptions() {
			}

			@Override
			public void options(Filter filter, List<Filter.Option> options) {
			}
		});
	}

	public static class TaskListIndexItem extends IndexItem<TaskListIndexEntry> {
		protected LayoutHelper helper;
		private Account account;
		private Handler handler;
		private Anchor urgentComponent;
		private HTMLPanel ownerPanel;
		private Label ownerPanelLabel;
		private Label label;

		public TaskListIndexItem(TaskListIndexEntry value, Account account, Handler handler, LayoutHelper helper, TranslatorService translator) {
			super(value, translator, false);
			this.account = account;
			this.helper = helper;
			this.handler = handler;
			init();
			refresh();
		}

		public void refresh() {
			refreshOwner();
			refreshUrgent();
		}

		public interface Handler {
			void unSetTaskOwner(TaskListIndexEntry entry);
			void setTaskUrgent(TaskListIndexEntry entry);
			void unSetTaskUrgent(TaskListIndexEntry entry);
		}

		@Override
		protected Widget createComponent() {
			HTMLPanel component = new HTMLPanel(helper.getIndexEntryLayout(Mode.LIST));

			addType(component);
			addContent(component);
			addTimeLine(component);
			addFlags(component);

			return component;
		}

		private void addType(HTMLPanel component) {
			Image image = new Image(translator.getTaskTypeIcon(value.getType()));
			image.setTitle(value.getLabel());

			replaceRegion(component, toRule(StyleName.TYPE) + " " + toRuleCheckingTags(StyleUtils.IMAGE), image);
		}

		private void addContent(HTMLPanel component) {
			addLabel(component);
			addOwner(component);
			addDates(component);
			addNewMessages(component);
		}

		private void addLabel(HTMLPanel component) {
			label = new Label(shortContent(value.getLabel(), 100));

			label.setTitle(value.getLabel());
			label.addStyleName(StyleName.LABEL);
			label.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
				}
			});

			replaceRegion(component, toRule(StyleName.LABEL), label);
		}

		private void addOwner(HTMLPanel component) {
			ownerPanelLabel = new Label();

			Anchor unSetCommand = new Anchor();
			unSetCommand.setTitle(translator.translate(TranslatorService.OperationLabel.UN_SET_TASK_OWNER));
			unSetCommand.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (handler != null)
						handler.unSetTaskOwner(value);
				}
			});

			ownerPanel = new HTMLPanel("");
			ownerPanel.addStyleName(StyleName.OWNER);
			ownerPanel.add(ownerPanelLabel);
			ownerPanel.add(unSetCommand);

			replaceRegion(component, toRule(StyleName.OWNER), ownerPanel);
		}

		private void addDates(HTMLPanel component) {
			String message = translator.getTaskDateMessage(value.getCreateDate(), value.getUpdateDate());

			Label label = new Label();
			label.addStyleName(StyleName.DATES);
			label.setText(message);

			replaceRegion(component, toRule(StyleName.DATES), label);
		}

		private void addNewMessages(HTMLPanel component) {
			String message = translator.getTaskNotificationsMessage(value.getMessagesCount());
			Label label = null;

			if (message != null) {
				label = new Label();
				$(label).addClass(StyleName.MESSAGE, StyleName.MESSAGE_NEW);
				label.setText(message);
			}

			replaceRegion(component, toCombinedRule(StyleName.MESSAGE, StyleName.MESSAGE_NEW), label);
		}

		private void addTimeLine(HTMLPanel component) {
			String timeLineImage = value.getTimeLineImageUrl();
			Image image = null;

			if (timeLineImage != null) {
				image = new Image(timeLineImage);
				image.setTitle(value.getLabel());
			}

			replaceRegion(component, toRule(StyleName.TIME_LINE) + " " + toRuleCheckingTags(StyleUtils.IMAGE), image);
		}

		private void addFlags(HTMLPanel component) {
			urgentComponent = new Anchor();
			urgentComponent.addStyleName(StyleName.URGENT);

			urgentComponent.setTitle(translator.translate(TranslatorService.OperationLabel.TOGGLE_TASK_URGENT));
			urgentComponent.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (handler == null)
						return;

					if (value.isUrgent())
						handler.unSetTaskUrgent(value);
					else
						handler.setTaskUrgent(value);
				}
			});

			replaceRegion(component, toRuleCheckingTags(StyleName.URGENT), urgentComponent);
		}

		private String getTaskAssignedMessage(User currentUser, User owner, User sender) {

			if (owner == null)
				return null;

			if (owner == currentUser)
				return sender != null?translator.getTaskAssignedToMeBySenderMessage(sender):translator.getTaskAssignedToMeMessage();

			return sender!=null?translator.getTaskAssignedToUserBySenderMessage(owner, sender):translator.getTaskAssignedToUserMessage(owner);
		}

		private void refreshOwner() {
			ownerPanel.setVisible(value.getOwner() != null);

			User owner = value.getOwner();
			User sender = value.getSender();
			User currentUser = account.getUser();
			String message = getTaskAssignedMessage(currentUser, owner, sender);

			if (message != null)
				ownerPanelLabel.setText(translator.translate(message));
		}

		private void refreshUrgent() {
			urgentComponent.removeStyleName(StyleName.URGENT_ACTIVE);

			if (value.isUrgent())
				urgentComponent.addStyleName(StyleName.URGENT_ACTIVE);
		}

		public interface StyleName extends IndexItem.StyleName {
			String TYPE = "type";
			String LABEL = "label";
			String TIME_LINE = "timeLine";
			String URGENT = "urgent";
			String OWNER = "owner";
			String DATES = "dates";
			String MESSAGE = "message";
			String MESSAGE_NEW = "new";
			String URGENT_ACTIVE = "active";
		}

		public static class Builder extends IndexItem.Builder<TaskListIndexEntry> {
			private final Account account;
			private final Handler handler;
			private final LayoutHelper helper;

			public Builder(Account account, Handler handler, LayoutHelper helper, TranslatorService translator) {
				super(translator);
				this.account = account;
				this.handler = handler;
				this.helper = helper;
			}

			@Override
			public HTMLListWidget.ListItem<TaskListIndexEntry> build(TaskListIndexEntry value, Mode mode) {
				return new TaskListIndexItem(value, account, handler, helper, translator);
			}

			@Override
			public Mode[] getAcceptedModes() {
				return new Mode[] { Mode.LIST };
			}
		}
	}

	public abstract static class Builder extends IndexWidget.Builder<TaskListIndexDisplay> {
	}

}
