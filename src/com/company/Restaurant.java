package com.company;

import java.sql.ClientInfoStatus;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;

public class Restaurant {
    Chef chef = new Chef("Повар");
    ThreadGroup waiters = new ThreadGroup("Официанты");
    ThreadGroup visitors = new ThreadGroup("Посетители");
    ArrayDeque<Visitor> visitorList = new ArrayDeque<>();
    private final int ORDERS_TO_TAKE = 5;
    private final int NUMBER_OF_WAITERS = 3;
    private final int NUMBER_OF_VISITORS = 5;
    private int orderCount = 0;
    Lock lock = new ReentrantLock();
    Condition condition;

    public Restaurant () {

    }

    public void openRestaurant() {
        System.out.println("Ресторан открыт");
        chef.start();

        for (int i = 0; i < NUMBER_OF_VISITORS; i++) {
            Thread visitor = new Visitor(visitors, "Посетитель" + (i + 1), this);
            visitor.start();
        }

        for (int i = 0; i < NUMBER_OF_WAITERS; i++) {
            Waiter waiter = new Waiter(waiters, "Официант " + (i + 1), this);
            waiter.start();
        }

        while (true) {
            if (orderCount == ORDERS_TO_TAKE) {
                closeRestaurant();
                break;
            }
        }
    }

    private void closeRestaurant() {
        chef.interrupt();
        waiters.interrupt();
        visitors.interrupt();
        System.out.println("Ресторан закрыт");
    }

    public void newVisitorCame(Visitor visitor) {
        visitorList.add(visitor);
    }

    public Visitor getVisitor() {
        Visitor visitor = visitorList.removeFirst();
        return visitor;
    }

    public Chef getChef() {
        return chef;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public int getORDERS_TO_TAKE() {
        return ORDERS_TO_TAKE;
    }

    public void setOrderCount() {
        orderCount += 1;
    }
}
