package client.widgets.view;

import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.view.DocumentViewDisplay;
import client.services.TranslatorService;
import client.widgets.view.document.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.$$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class DocumentViewWidget extends HTMLPanel {

    private static final String BOX = "<div class='node entity document'><div class='component'>::layout::</div></div>";
	private static final String LAYOUT = "::layout::";

	private static final String HAS_PENDING_OPERATIONS = "hasPendingOperations";
	private static final String NUMBER_OF_PAGES = "numberOfPages";
	private static final String PAGES = "pages";
	private static final String ID = "id";
	private static final String ASPECT_RATIO = "aspectRatio";
    private static final String ESTIMATED_TIME_TO_FINISH = "estimatedTimeToFinish";

    private final DocumentViewDisplay display;
    private CellPanel component;
	private PagesViewerWidget pagesViewer;
	private PagesPreviewViewerWidget previewsViewer;

	public DocumentViewWidget(DocumentViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
        create(translator);
		refresh();
		hook();
	}

	private void create(TranslatorService translator) {
		addStyleName(StyleName.DOCUMENT_VIEWER);
		createLabel();
		createComponent(translator);
		bind();
	}

	private void createLabel() {
		Element labelBox = $(getElement()).find(toRule(StyleName.LABEL)).get(0);
		if (labelBox != null)
			labelBox.setInnerHTML(display.getLabel());
	}

	private void createComponent(TranslatorService translator) {
		component = new HorizontalPanel();
		createPagesViewer(translator);
		createThumbsViewer();
		component.add(pagesViewer);
		component.add(previewsViewer);
	}

	private void createPagesViewer(TranslatorService translator) {
		pagesViewer = new PagesViewerWidget(translator);
		pagesViewer.setPageChangeHandler(new PagesViewerWidget.PageChangeHandler() {
            @Override
            public void pageChanged(int pageNumber) {
                previewsViewer.selectThumb(pageNumber);
            }
        });
	}

	private void createThumbsViewer() {
		previewsViewer = new PagesPreviewViewerWidget(new VerticalThumbsViewerWidget());
		previewsViewer.setSelectThumbHandler(new ThumbsViewerWidget.SelectThumbHandler() {
			@Override
			public void selected(int page) {
				pagesViewer.showPage(page);
				previewsViewer.selectThumb(page);
			}
		});
		previewsViewer.setPageChangeHandler(new PagesControlsWidget.PageChangeHandler() {
			@Override
			public void selectedPage(int page) {
				pagesViewer.showPage(page);
				previewsViewer.selectThumb(page);
			}
		});
		previewsViewer.setZoomPageHandler(new PagesControlsWidget.ZoomPageHandler() {
            @Override
            public void zoom(int value) {
                pagesViewer.setZoom(value);
            }
        });
	}

	private void bind() {
		bindWidgetToElement(this, component, $(getElement()).find(toRule(StyleName.COMPONENT)).get(0));
		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void refresh() {
		loadPages();
	}

	private void loadPages() {
		Ajax.ajax(Ajax.createSettings()
                        .setUrl(display.getBaseUrl())
                        .setDataType("json")
                        .setType("get")
                        .setData($$("type: JSON"))
                        .setSuccess(new Function() {
                            public void f() {
                                processResponse(this.<Properties>getArgumentJSO(0));
                            }
                        })
        );
	}

	private void processResponse(Properties response) {
        pagesViewer.hideLoading();
		if (response.getBoolean(HAS_PENDING_OPERATIONS))
            showLoading(response.getInt(ESTIMATED_TIME_TO_FINISH));
        else
			showPages(response.getInt(NUMBER_OF_PAGES), new JSONObject(response.getJavaScriptObject(PAGES)));
    }

    private void showLoading(int secondsRemaining) {
        pagesViewer.showLoading(secondsRemaining);
        new Timer() {
            @Override
            public void run() {
                loadPages();
            }
        }.schedule(secondsRemaining / 2 * 1000);
    }

    private void showPages(int numberOfPages, JSONObject pages) {
		previewsViewer.setMaxNumberOfPages(numberOfPages);
		for (int i = 1; i <= numberOfPages; i++)
			addPage(page(pages, i).get(ID).isNumber().doubleValue(), page(pages, i).get(ASPECT_RATIO).isNumber().doubleValue());
		previewsViewer.selectThumb(0);
	}

    private JSONObject page(JSONObject pages, int i) {
        return pages.get(String.valueOf(i)).isObject();
    }

    private void addPage(double pageId, double aspectRatio) {
        pagesViewer.addPage(getUrl() + "&page=" + pageId + "&r=" + aspectRatio);
		previewsViewer.addThumb(getUrl() + "&thumb=" + pageId + "&r=" + aspectRatio);
	}

	private String getUrl() {
		return display.getBaseUrl();
	}

	private void hook() {
		display.addHook(new DocumentViewDisplay.Hook() {
		});
	}

	private static String getHtml(String layout, TranslatorService translator) {
		return translator.translateHTML(BOX.replaceAll(LAYOUT, layout));
	}

	public static class Builder extends NodeViewWidget.Builder {
		public static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(DocumentViewDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new DocumentViewWidget((DocumentViewDisplay) presenter, getLayout(StyleName.VIEW_DOCUMENT, layout), translator);
		}
	}

	public interface StyleName {
		String COMPONENT = "component";
		String DOCUMENT_VIEWER = "document-viewer";
		String LABEL = "label";
		String VIEW_DOCUMENT = "view-document";
	}
}
