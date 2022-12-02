package ru.javawebinar.basejava;

public class MainDeadlock {

    private static final Object OBJECT_1 = new Object();
    private static final Object OBJECT_2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (OBJECT_1) {
                System.out.println("thread1 holds object1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thread1 waits object2");
                synchronized (OBJECT_2) {
                    System.out.println("thread1 holds object2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (OBJECT_2) {
                System.out.println("thread2 holds object2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thread2 waits object1");
                synchronized (OBJECT_1) {
                    System.out.println("thread2 holds object1");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
