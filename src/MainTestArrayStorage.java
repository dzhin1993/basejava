import model.Resume;
import storage.ArrayStorage;
import storage.SortedArrayStorage;
import storage.Storage;

/**
 * Test for com.urise.webapp.storage.storage.ArrayStorage
 */
public class MainTestArrayStorage {
    private static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();
    private static final SortedArrayStorage SORTED_ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        System.out.println("test ARRAY_STORAGE:\n");
        testStorage(ARRAY_STORAGE);
        System.out.println("---------");
        System.out.println("test SORTED_ARRAY_STORAGE:\n");
        testStorage(SORTED_ARRAY_STORAGE);
    }

    private static void testStorage(Storage storage) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");

        storage.save(r1);
        storage.save(r2);
        storage.save(r3);

        System.out.println("Get r1: " + storage.get(r1.getUuid()));
        System.out.println("Size: " + storage.size());

        System.out.println("Get dummy: " + storage.get("dummy"));

        Resume r4 = new Resume();
        r4.setUuid("uuid3");
        storage.update(r4);
        System.out.println("Size after update: " + storage.size());

        printAll(storage);
        storage.delete(r1.getUuid());
        printAll(storage);
        storage.clear();
        printAll(storage);

        System.out.println("Size: " + storage.size());

        long timeBefore = System.currentTimeMillis();
        for (int i = 0; i <= 10000; i++) {
            Resume r = new Resume();
            r.setUuid(String.valueOf(i));
            storage.save(r);
        }
        System.out.println(storage.size());
        for (int i = 0; i < 10000; i++) {
            storage.delete(String.valueOf(i));
        }
        long timeAfter = System.currentTimeMillis();
        System.out.println("time:" + (timeAfter - timeBefore) + " ms");
    }

    private static void printAll(Storage storage) {
        System.out.println("\nGet All");
        for (Resume r : storage.getAllSorted()) {
            System.out.println(r);
        }
    }
}
