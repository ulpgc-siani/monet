package cosmos.gwt.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class WidgetUtils {

    public static boolean isVisible(Widget widget) {
        if (widget.isVisible() && widget.getParent() != null)
            return isVisible(widget.getParent());
        return widget.isVisible();
    }

    public static void bindWidgetToElement(HTMLPanel container, Widget component, Element element) {
        bindWidgetToElement(container, component, element, false);
    }

    public static void bindWidgetToElementAndKeepContent(HTMLPanel container, Widget component, Element element) {
        bindWidgetToElement(container, component, element, true);
    }

	public static void bindWidgetToElementAndKeepStyles(HTMLPanel container, Widget component, Element element) {
        if (element == null) return;
		String className = element.getClassName();
		bindWidgetToElement(container, component, element, false);
		component.addStyleName(className);
	}

	public static void bindWidgetToElement(Widget component, Element element) {
		element.getParentElement().replaceChild(component.getElement(), element);
	}

    private static void bindWidgetToElement(HTMLPanel container, Widget component, Element element, boolean keepElementContent) {
        if (element == null) return;
        String innerHTML = element.getInnerHTML();
        container.addAndReplaceElement(component, element);

        if (keepElementContent && !innerHTML.isEmpty())
            component.getElement().setInnerHTML(innerHTML);
    }
}
