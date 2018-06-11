package storage;

import model.Resume;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public Resume get(String uuid) {
        int index = getIndexFromStorage(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.println("model.Resume " + uuid + " is not exist in storage");
        }
        return null;
    }

    protected abstract int getIndexFromStorage(String uuid);
}
