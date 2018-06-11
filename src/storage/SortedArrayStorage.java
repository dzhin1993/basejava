package storage;

import model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("the maximum storage size is exceeded");
            return;
        }
        int index = getIndexFromStorage(r.getUuid());
        if (index < 0) {
            index = -index - 1;
            System.arraycopy(storage, index, storage, index + 1, size - index);
            storage[index] = r;
            size++;
        } else {
            System.out.println("model.Resume " + r.getUuid() + " is exist in storage");
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndexFromStorage(uuid);
        if (index >= 0) {
            System.arraycopy(storage, index + 1, storage, index, storage.length - index - 1);
            size--;
        } else {
            System.out.println("model.Resume " + uuid + " is not exist in storage");
        }
    }

    @Override
    protected int getIndexFromStorage(String uuid) {
        Resume resume = new Resume();
        resume.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, resume);
    }
}
