package cosmos.gwt.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepContent;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepStyles;

public class CosmosHtmlPanel extends HTMLPanel {

    public CosmosHtmlPanel() {
        this("");
    }

    public CosmosHtmlPanel(String layout) {
        super(layout);
    }

    public void addStyleName(String... styles) {
        for (String style : styles)
            addStyleName(style);
    }

    public void setId(String id) {
        getElement().setId(id);
    }

    protected void bind(Widget widget, String styleRule) {
        bind(widget, $(getElement()).find(styleRule).get(0));
    }
    
    protected void bindKeepingStyles(Widget widget, String styleRule) {
        bindKeepingStyles(widget, $(getElement()).find(styleRule).get(0));
    }

    protected void bindKeepingContent(Widget widget, String styleRule) {
        bindWidgetToElementAndKeepContent(this, widget, $(getElement()).find(styleRule).get(0));
    }

    protected void bind(Widget widget, Element element) {
        bindWidgetToElement(this, widget, element);
    }

    protected void bindKeepingStyles(Widget widget, Element element) {
        bindWidgetToElementAndKeepStyles(this, widget, element);
    }
}
