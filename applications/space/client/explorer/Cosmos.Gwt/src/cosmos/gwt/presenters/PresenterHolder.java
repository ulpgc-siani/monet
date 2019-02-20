package cosmos.gwt.presenters;

import com.google.gwt.dom.client.Element;
import cosmos.presenters.Holder;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class PresenterHolder implements Holder {

	private final Element element;
	private final Presenter.Type presenterType;
	private final String design;
	private final String layout;
	private final String styles;

	public static final String STYLE = "holder";

	public PresenterHolder(Element element) {
		Parser parser = new Parser(element);
		this.element = element.getParentElement();
		this.layout = parser.getLayout();
		this.presenterType = parser.getType();
		this.design = parser.getDesign();
		this.styles = parser.getStyles();
		this.element.removeChild(element);
	}

	public Element getElement() {
		return element;
	}

	public Presenter.Type getPresenterType() {
		return presenterType;
	}

	public String getDesign() {
		return design;
	}

	public String getLayout() {
		return layout;
	}

	public void appendChild(com.google.gwt.user.client.ui.Widget widget) {
		Element widgetElement = widget.getElement();
		addStyles(widget);
		this.element.appendChild(widgetElement);
	}

	public void clear() {
		this.element.setInnerHTML("");
	}

	public boolean canHold(Presenter presenter) {
		return presenter.is(this.getPresenterType());
	}

	private void addStyles(com.google.gwt.user.client.ui.Widget widget) {
		if (this.styles.isEmpty())
			return;
		widget.addStyleName(this.styles);
	}

	private class Parser {
		private final static String PRESENTER = "presenter";
		private final static String DESIGN = "design";
		private final static String LAYOUT = "layout";

		private Element element;

		private Parser(Element element) {
			this.element = element;
		}

		public Presenter.Type getType() {
			Element element = getChild(PRESENTER);
			return element != null ? new Presenter.Type(element.getInnerText(), null) : null;
		}

		public String getDesign() {
			Element element = getChild(DESIGN);
			return element != null ? element.getInnerText() : null;
		}

		public String getLayout() {
			Element element = getChild(LAYOUT);
			return element != null ? element.getInnerHTML() : "";
		}

		public String getStyles() {
			return element.getClassName().replace(PresenterHolder.STYLE, "").trim();
		}

		private Element getChild(String className) {
            return $(element).children(toRule(className)).get(0);
		}
	}
}
