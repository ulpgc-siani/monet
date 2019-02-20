package client.widgets.toolbox;

import client.core.model.Order;
import client.services.TranslatorService;
import client.widgets.entity.components.InputKeyFilter;
import client.widgets.popups.FieldPopupWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepStyles;

public class OrdersWidget extends HTMLPanel {

    private final InputKeyFilter display = new InputKeyFilter().allowArrows();
    private final String placeholder;
    private OrderSelectHandler orderSelectHandler;
    private OrdersPopUpWidget options;

    public OrdersWidget(String layout, TranslatorService translator) {
        super(translator.translateHTML(layout));
        this.placeholder = translator.translate(TranslatorService.Label.ADD_ORDER);
        create();
    }

    public void addOrder(Order order) {
        options.addOrder(order);
    }

    public void clearOrders() {
        options.clearOrders();
    }

    public void setOrderSelectHandler(OrderSelectHandler orderSelectHandler) {
        this.orderSelectHandler = orderSelectHandler;
    }

    private void create() {
        createDisplay();
        createOptions();
        bind();
    }

    private void createDisplay() {
        display.getElement().setPropertyString("placeholder", placeholder);
        display.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                options.show();
            }
        });
    }

    private void createOptions() {
        options = new OrdersPopUpWidget($(getElement()).find(toRule(StyleName.POPUP)).get(0).getInnerHTML());
    }

    private void bind() {
        bindWidgetToElementAndKeepStyles(this, display, $(getElement()).find(toRule(StyleName.DISPLAY)).get(0));
        bindWidgetToElementAndKeepStyles(this, options, $(getElement()).find(toRule(StyleName.POPUP)).get(0));

        onAttach();
        RootPanel.detachOnWindowClose(this);
    }

    private class OrdersPopUpWidget extends FieldPopupWidget<InputKeyFilter, OrdersDialogWidget> {

        public OrdersPopUpWidget(String layout) {
            super(layout, new OrdersDialogWidget(), display);
            init();
        }

        @Override
        protected OrdersDialogWidget createDialog(Element container) {
            bindWidgetToElementAndKeepStyles(this, content, container);
            return content;
        }

        public void clearOrders() {
            content.clearOrders();
        }

        public void addOrder(Order order) {
            content.addOrder(order);
        }
    }

    private class OrdersDialogWidget extends ScrollPanel {

        private final UnorderedListWidget container = new UnorderedListWidget();

        public OrdersDialogWidget() {
            add(container);
        }

        public void clearOrders() {
            container.clear();
        }

        public void addOrder(Order order) {
            OrderItemWidget orderItemWidget = new OrderItemWidget(order);
            orderItemWidget.setOrderSelectHandler(new OrderItemWidget.OrderSelectHandler() {
                @Override
                public void select(Order order) {
                    if (orderSelectHandler != null) orderSelectHandler.select(order);
                }
            });
            container.add(orderItemWidget);
        }
    }

    public interface OrderSelectHandler {
        void select(Order order);
    }

    public interface StyleName {
        String DISPLAY = "display";
        String POPUP = "popup";
    }
}
