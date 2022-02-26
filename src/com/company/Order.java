package com.company;

public class Order {
    private Waiter waiter;
    private Visitor visitor;

    public Order(Waiter waiter, Visitor visitor) {
        this.waiter = waiter;
        this.visitor = visitor;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Visitor getVisitor() {
        return visitor;
    }
}
