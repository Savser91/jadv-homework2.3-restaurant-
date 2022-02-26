package com.company;

import java.util.concurrent.locks.*;

public class Visitor extends Thread {
    private final long TIMEOUT_THINKING = 1000;
    private final long TIMEOUT_EATING = 8000;
    private String name;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Restaurant restaurant;

    public Visitor(ThreadGroup tg, String name, Restaurant restaurant) {
        super(name);
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " зашел в ресторан");
        restaurant.newVisitorCame(this);
        lock.lock();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(TIMEOUT_THINKING);
                System.out.println(Thread.currentThread().getName() + " сделал заказ");
                condition.await();
                System.out.println(Thread.currentThread().getName() + " начал есть");
                Thread.sleep(TIMEOUT_EATING);
                System.out.println(Thread.currentThread().getName() + " вышел из ресторана");
                restaurant.setOrderCount();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void takeOrder() {
        lock.lock();
        try {
            condition.signal();
        } catch (Exception e) {
            e.getMessage();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
