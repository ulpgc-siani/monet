package client.widgets.popups;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;

import java.util.ArrayList;
import java.util.List;

import static com.google.gwt.dom.client.Style.Unit;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public abstract class PopUpWidget<WidgetType extends Widget> extends CosmosHtmlPanel {

	private static final String TEMPLATE = "<span class=\"close\"></span><div class=\"content\"></div>";
    private final List<PopupHandler> handlers;
    protected WidgetType content;
    protected SizeCalculator sizeCalculator;

	public PopUpWidget() {
		this(TEMPLATE);
	}

    public PopUpWidget(String layout) {
	    this(layout, null);
    }

    public PopUpWidget(WidgetType content) {
        this(TEMPLATE, content);
    }

	public PopUpWidget(String layout, WidgetType content) {
		super(layout);
		this.content = content;
		handlers = new ArrayList<>();
	}

    protected void init() {
        createComponents();
        resizeOnFrameResize();
        addStyleName(StyleName.POPUP);
        addHandlers();
        hide();
    }

    private void createComponents() {
		$(getElement()).children(toRule(StyleName.CLOSE)).click(new Function() {
			@Override
			public void f() {
				hide();
			}
		});
        content = createContent($(this).children(toRule(StyleName.CONTENT)).get(0));
	}

	protected abstract WidgetType createContent(Element container);

	public WidgetType getContent() {
		return content;
	}

    public void show() {
        setVisible(true);
        if (sizeCalculator == null) return;
        updateWidth();
        updateHeight();
    }

    public void addHandler(PopupHandler handler) {
        handlers.add(handler);
    }

    public void hide() {
        setVisible(false);
    }

    public void setSizeCalculator(SizeCalculator sizeCalculator) {
        this.sizeCalculator = sizeCalculator;
    }

    public void addKeyUpHandler(KeyUpHandler handler) {
        content.addDomHandler(handler, KeyUpEvent.getType());
    }

    public void addKeyDownHandler(KeyDownHandler handler) {
        content.addDomHandler(handler, KeyDownEvent.getType());
    }

    public void propagateEventToWidgets(GwtEvent event, List<Widget> widgets) {
        for (Widget widget : widgets)
            widget.fireEvent(event);
    }

    protected boolean clickedOutsideContent(NativeEvent event) {
        return contentOutsideX(content, event.getClientX()) || contentOutsideY(content, event.getClientY());
    }

    protected boolean clickedOutside(Widget widget, NativeEvent event) {
        return contentOutsideX(widget, event.getClientX()) || contentOutsideY(widget, event.getClientY());
    }

    protected ClickHandler onClickOutsideContent() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!isVisible() || !clickedOutsideContent(event.getNativeEvent())) return;

                hide();
                for (PopupHandler handler : handlers)
                    handler.onOutsideClick();

                event.stopPropagation();
            }
        };
    }

	private void addHandlers() {
        RootPanel.get().addDomHandler(onClickOutsideContent(), ClickEvent.getType());
		addDomHandler(onClickInsideContent(), ClickEvent.getType());
		content.addDomHandler(onClickInsideContent(), ClickEvent.getType());
    }

    protected void updateWidth() {
        getElement().getStyle().setWidth(sizeCalculator.getWidth() - 20, Unit.PX);
    }

    private void updateHeight() {
        if (sizeCalculator.getHeight() > 0)
            getElement().getStyle().setHeight(sizeCalculator.getHeight(), Unit.PX);
    }

    private ClickHandler onClickInsideContent() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                if (clickedOutsideContent(event.getNativeEvent()))
                    hide();
                propagateEventToWidgets(event, $(getElement()).find(toRule(StyleName.POPUP)).widgets());
                event.stopPropagation();
			}
		};
	}

    private boolean contentOutsideX(Widget widget, int clientX) {
        int contentLeft = widget.getAbsoluteLeft();
        int contentWidth = widget.getOffsetWidth();
        return contentLeft > clientX || (contentLeft + contentWidth) < clientX;
    }

    private boolean contentOutsideY(Widget widget, int clientY) {
        int contentTop = widget.getAbsoluteTop();
        int contentHeight = widget.getOffsetHeight();
        return contentTop > clientY || (contentTop + contentHeight) < clientY;
    }

    private void resizeOnFrameResize() {
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                resize();
            }
        });
    }

    private void resize() {
        if (!isVisible()) return;
        hide();
        updateWidth();
        show();
    }

    public interface SizeCalculator {
        int getWidth();
        int getHeight();
    }

    public interface StyleName {
        String CLOSE = "close";
        String CONTENT = "content";
        String POPUP = "popup";
    }

    public interface PopupHandler {
        void onOutsideClick();
    }
}
