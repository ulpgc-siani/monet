package client.core.system;

import client.core.model.Order;

public class OrderList extends MonetList<client.core.model.Order> implements client.core.model.OrderList {

    @Override
    public boolean add(Order order) {
        if (containsOrder(order))
            return replaceOrder(order);
        return super.add(order);
    }

    private boolean containsOrder(Order order) {
        for (Order order1 : this)
            if (order.getLabel().equals(order1.getLabel())) return true;
        return false;
    }

    private boolean replaceOrder(Order order) {
        if (order.getMode() == null)
            return remove(getOldOrder(order.getLabel()));
        set(indexOf(getOldOrder(order.getLabel())), order);
        return true;
    }

    private Order getOldOrder(String label) {
        for (Order order : this)
            if (order.getLabel().equals(label)) return order;
        return null;
    }
}
