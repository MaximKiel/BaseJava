package ru.javawebinar.basejava;

public class MainDeadlock {

    private static final Object OBJECT_1 = new Object();
    private static final Object OBJECT_2 = new Object();

    public static void main(String[] args) {
        deadlock(OBJECT_1, OBJECT_2);
        deadlock(OBJECT_2, OBJECT_1);
    }

    private static void deadlock(Object object1, Object object2) {
        new Thread(() -> {
            synchronized (object1) {
                System.out.println("thread holds first object");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thread waits second object");
                synchronized (object2) {
                    System.out.println("thread holds second object");
                }
            }
        }).start();
    }
}
