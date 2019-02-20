package client.widgets.view.workmap;

import client.core.model.Fact;
import client.core.model.MonetLink;
import client.presenters.displays.entity.workmap.TaskHistoryDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

public class TaskHistoryWidget extends VerticalPanel {
	private final TaskHistoryDisplay display;
	private final TranslatorService translator;
	private HTMLListWidget<Fact> facts;
	private Anchor more;

	public TaskHistoryWidget(TaskHistoryDisplay display, TranslatorService translator) {
		super();
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.hook();
	}

	private void createComponents() {
		createFactsPanel();
		createMoreFactsOperation();
		this.bind();
	}

	private void createFactsPanel() {
		facts = new HTMLListWidget<>(new FactItem.Builder(new FactItemHandler() {
			@Override
			public void executeLink(MonetLink link) {
				display.executeLink(link);
			}
		}, translator), translator);
		facts.addStyleName(StyleName.FACTS);
		add(facts);
	}

	private void createMoreFactsOperation() {
		more = new Anchor();
		more.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.nextPage();
			}
		});
		more.addStyleName(StyleName.MORE);
		more.setVisible(false);
		add(more);
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void clearPages() {
		facts.clear();
		more.setVisible(false);
	}

	private void addPage(TaskHistoryDisplay.Page page) {

		for (Fact fact : page.getEntries())
			facts.addItem(fact);

		more.setVisible(display.hasMorePages());
	}

	private void hook() {
		this.display.addHook(new TaskHistoryDisplay.Hook() {
			@Override
			public void clearPages() {
				TaskHistoryWidget.this.clearPages();
			}

			@Override
			public void page(TaskHistoryDisplay.Page page) {
				TaskHistoryWidget.this.addPage(page);
			}

			@Override
			public void pageFailure(String error) {
			}
		});
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(TaskHistoryDisplay.TYPE);
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskHistoryWidget((TaskHistoryDisplay) presenter, translator);
		}

	}

	public static class FactItem extends HTMLListWidget.ListItem<Fact> {
		private final FactItemHandler handler;
		private final TranslatorService translator;

		public FactItem(Fact value, FactItemHandler handler, TranslatorService translator) {
			super(value);
			this.handler = handler;
			this.translator = translator;
			init();
		}

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
			HTMLPanel component = new HTMLPanel("");
			component.add(createSubTitle());
			component.add(createTitle());
			component.add(createLinks());
			return component;
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}

		private Widget createSubTitle() {
			HTMLPanel subTitle = new HTMLPanel(translator.translateFullDateByUser(value.getCreateDate(), value.getUser()));
			subTitle.addStyleName(StyleName.SUBTITLE);
			return subTitle;
		}

		private Widget createTitle() {
			HTMLPanel title = new HTMLPanel(value.getTitle());
			title.addStyleName(StyleName.TITLE);
			return title;
		}

		private Widget createLinks() {
			HTMLPanel links = new HTMLPanel("");

			for (MonetLink monetLink : value.getLinks())
				links.add(createLink(monetLink));

			return links;
		}

		private Widget createLink(final MonetLink link) {
			Anchor linkAnchor = new Anchor(link.getLabel());
			linkAnchor.addStyleName(StyleName.TITLE);
			linkAnchor.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					handler.executeLink(link);
				}
			});
			return linkAnchor;
		}

		public static class Builder extends HTMLListWidget.ListItem.Builder<Fact> {
			private final FactItemHandler handler;
			private final TranslatorService translator;

			public Builder(FactItemHandler handler, TranslatorService translator) {
				this.handler = handler;
				this.translator = translator;
			}

			@Override
			public HTMLListWidget.ListItem<Fact> build(Fact value, HTMLListWidget.Mode mode) {
				return new FactItem(value, handler, translator);
			}
		}

		protected interface StyleName extends HTMLListWidget.ListItem.StyleName {
			String TITLE = "title";
			String SUBTITLE = "subtitle";
		}

	}

	public interface FactItemHandler {
		void executeLink(MonetLink link);
	}

	private interface StyleName {
		String FACTS = "facts";
		String MORE = "more";
	}

}
