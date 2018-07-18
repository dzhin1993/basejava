public class MainDeadLock {
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> lock_1BeforeLock_2(Thread.currentThread()));
        Thread thread2 = new Thread(() -> lock_2BeforeLock_1(Thread.currentThread()));

        thread1.start();
        thread2.start();

    }

    private static void lock_1BeforeLock_2(Thread thread) {
        synchronized (LOCK_1) {
            System.out.println(thread.getName() + " is capture LOCK_1");
            synchronized (LOCK_2) {
                System.out.println(thread.getName() + " is capture LOCK_2");
            }
        }
    }

    private static void lock_2BeforeLock_1(Thread thread) {
        synchronized (LOCK_2) {
            System.out.println(thread.getName() + " is capture LOCK_2");
            synchronized (LOCK_1) {
                System.out.println(thread.getName() + " is capture LOCK_1");
            }
        }
    }
}
