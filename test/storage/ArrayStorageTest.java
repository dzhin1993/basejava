package storage;

import exception.StorageException;
import model.Resume;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ArrayStorageTest extends AbstractStorageTest {
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Test(expected = StorageException.class)
    public void saveStorageOverflow() {
        try {
            for (int i = storage.size(); i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("Storage overflow");
        }
        storage.save(new Resume());
    }
}
