package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import exception.StorageException;
import model.Resume;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class AbstractArrayStorageTest {

    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final Resume R1 = new Resume(UUID_1);
    private static final Resume R2 = new Resume(UUID_2);
    private static final Resume R3 = new Resume(UUID_3);
    private static final Resume R4 = new Resume();

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }


    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        storage.update(R1);
        assertEquals(R1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistStorageException() {
        storage.update(R4);
    }

    @Test
    public void save() {
        storage.save(R4);
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistStorageException() {
        storage.save(R3);
    }

    @Test(expected = StorageException.class)
    public void saveStorageOverflow() {
        for (int i = 0; i <= 10000; i++) {
            Resume currentResume = new Resume(String.valueOf(i));
            storage.save(currentResume);
        }
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void get() {
        assertEquals(R1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistStorageException() {
        storage.get("");
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistStorageException() {
        storage.delete("");
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(resumes[0], storage.get(UUID_1));
        assertEquals(resumes[1], storage.get(UUID_2));
        assertEquals(resumes[2], storage.get(UUID_3));
    }
}