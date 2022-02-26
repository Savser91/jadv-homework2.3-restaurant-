package com.company;

import java.util.*;
import java.util.concurrent.locks.*;

public class Chef extends Thread {
    private final long TIMEOUT_COOKING = 6000;
    private String name;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Queue<Order> orderQueue;

    public Chef(String name) {
        super(name);
        orderQueue = new LinkedList<Order>();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " на работе");
        lock.lock();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                while (orderQueue.isEmpty())
                    condition.await();
                Order order = orderQueue.poll();
                if (order != null) {
                    System.out.println(Thread.currentThread().getName() + " готовит еду для" + order.getVisitor().getName());
                    Thread.sleep(TIMEOUT_COOKING);
                    System.out.println(Thread.currentThread().getName() + " приготовил еду для " + order.getVisitor().getName());
                    order.getWaiter().getOrder(order.getVisitor());
                }
            }
        } catch (InterruptedException e) {
            e.getMessage();
        } finally {
            lock.unlock();
        }
    }

    public void addOrder(Waiter waiter, Visitor visitor) {
        lock.lock();
        try {
            orderQueue.add(new Order(waiter, visitor));
            condition.signal();
        } catch (Exception e) {
            e.getMessage();
        } finally {
            lock.unlock();
        }
    }
}
