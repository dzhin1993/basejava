package storage;

import model.Resume;


/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("the maximum storage size is exceeded");
            return;
        }
        int index = getIndexFromStorage(r.getUuid());
        if (index == -1) {
            storage[size++] = r;
        } else {
            System.out.println("model.Resume " + r.getUuid() + " is exist in storage");
        }
    }


    public void delete(String uuid) {
        int index = getIndexFromStorage(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("model.Resume " + uuid + " is not exist in storage");
        }
    }

    protected int getIndexFromStorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
