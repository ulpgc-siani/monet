package client.core.model.types;

public interface Number<Type extends java.lang.Number> {
    Type getValue();

    void increment();
    void increment(java.lang.Number number);
    void decrement();
    void decrement(java.lang.Number number);
}
