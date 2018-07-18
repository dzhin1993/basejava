public class MainDeadLock {
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(MainDeadLock::lock_1BeforeLock_2);
        Thread thread2 = new Thread(MainDeadLock::lock_2BeforeLock_1);

        thread1.start();
        thread2.start();

    }

    private static void lock_1BeforeLock_2() {
        synchronized (LOCK_1) {
            System.out.println(Thread.currentThread().getName() + " is capture LOCK_1");
            synchronized (LOCK_2) {
                System.out.println(Thread.currentThread().getName() + " is capture LOCK_2");
            }
        }
    }

    private static void lock_2BeforeLock_1() {
        synchronized (LOCK_2) {
            System.out.println(Thread.currentThread().getName() + " is capture LOCK_2");
            synchronized (LOCK_1) {
                System.out.println(Thread.currentThread().getName() + " is capture LOCK_1");
            }
        }
    }
}
