package client.core.model;

public interface AllowAnalyze {
    String getId();
    List<Filter> getFilters(Key view);
    List<Order> getOrders(Key view);
}
