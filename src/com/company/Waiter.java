package com.company;

import java.util.concurrent.locks.*;

public class Waiter extends Thread {
    private final long TIMEOUT_WAITER = 100;
    private String name;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Restaurant restaurant;

    public Waiter (ThreadGroup tg, String name, Restaurant restaurant) {
        super(name);
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " вышел на работу");
        lock.lock();
        try {
            Visitor visitor = restaurant.getVisitor();
            System.out.println(Thread.currentThread().getName() + " взял заказ у " + visitor.getName());
            restaurant.getChef().addOrder(this, visitor);

        } catch (Exception e) {
            e.getMessage();
        } finally {
            lock.unlock();
        }
    }

    public void getOrder(Visitor visitor) {
        System.out.println(this.getName() + " доставил заказ " + visitor.getName());
        visitor.takeOrder();
    }
}
