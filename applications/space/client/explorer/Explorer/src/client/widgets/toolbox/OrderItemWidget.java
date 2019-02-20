package client.widgets.toolbox;

import client.core.model.Order;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class OrderItemWidget extends ListItemWidget {

    private final Order.Mode[] orders = new Order.Mode[]{null, Order.Mode.ASC, Order.Mode.DESC};
    private final String[] orderStyle = new String[]{StyleName.ORDER, StyleName.ASC, StyleName.DESC};
    private int currentOrder = 0;
    private OrderSelectHandler orderSelectHandler;

    public OrderItemWidget(final Order order) {
        super(order.getLabel());
        addStyleName(orderStyle[currentOrder]);
        addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                updateOrder(order);
            }
        }, ClickEvent.getType());
    }

    public void setOrderSelectHandler(OrderSelectHandler orderSelectHandler) {
        this.orderSelectHandler = orderSelectHandler;
    }

    private void updateOrder(Order order) {
        currentOrder = (currentOrder + 1) % orders.length;
        setStyleName(orderStyle[currentOrder]);
        order.setMode(orders[currentOrder]);
        if (orderSelectHandler != null) orderSelectHandler.select(order);
    }

    public interface StyleName {
        String ASC = "order asc";
        String DESC = "order desc";
        String ORDER = "order";
    }

    public interface OrderSelectHandler {
        void select(Order order);
    }
}
