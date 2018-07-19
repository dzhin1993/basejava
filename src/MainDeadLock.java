public class MainDeadLock {
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> doSomething(LOCK_1, LOCK_2));
        Thread thread2 = new Thread(() -> doSomething(LOCK_2, LOCK_1));

        thread1.start();
        thread2.start();

    }
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static void doSomething(Object lock1, Object lock2) {
        synchronized (lock1) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignore) {
            }
            synchronized (lock2) {
                System.out.println("I can not print this text :(");
            }
        }
    }
}
