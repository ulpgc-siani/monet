package client.widgets.index;

import client.core.model.Filter;
import client.core.model.IndexEntry;
import client.core.model.List;
import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.LinkFieldIndexDisplay;
import client.presenters.displays.SetIndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class IndexReportWidget<D extends IndexDisplay, E extends IndexEntry> extends InlineHTML {
	protected final D display;
	private final TranslatorService translator;

	public IndexReportWidget(D display, TranslatorService translator) {
		super(getHtml("", translator));
		this.getElement().addClassName("index-report-widget");
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		this.bind();
	}

	private void refresh() {
		setText(translator.getCountLabel(display.getEntriesCount()));
	}

	protected void hook() {
		display.addHook(new D.Hook<E>() {
			@Override
			public void clear() {
			}

			@Override
			public void loadingPage() {
			}

			@Override
			public void page(D.Page<E> page) {
				refresh();
			}

			@Override
			public void pagesCount(int count) {
				refresh();
			}

			@Override
			public void pageEntryAdded(E entry) {
				refresh();
			}

			@Override
			public void pageEntryDeleted() {
			}

			@Override
			public void pageFailure() {
			}

			@Override
			public void pageEntryUpdated(E entry) {
			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {
			}

			@Override
			public void selectEntries(List<E> entries) {
			}

			@Override
			public void selectOptions(Filter filter) {
			}

			@Override
			public void loadingOptions() {
			}

			@Override
			public void options(Filter filter, List<Filter.Option> options) {
			}
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends IndexWidget.Builder<IndexDisplay> {

		public static final String DESIGN = "report";

		public static void register() {
			registerBuilder(SetIndexDisplay.TYPE.toString() + IndexReportWidget.Builder.DESIGN + ViewDefinition.Design.DOCUMENT.toString(), new Builder());
			registerBuilder(LinkFieldIndexDisplay.TYPE.toString() + IndexReportWidget.Builder.DESIGN + ViewDefinition.Design.DOCUMENT.toString(), new Builder());
		}

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(SetIndexDisplay.TYPE) && (design!=null && design.equals(DESIGN));
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new IndexReportWidget((IndexDisplay) presenter, translator);
		}

	}

	public interface Style {
	}

}
