package storage;

import model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        int index = getIndexFromStorage(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("model.Resume " + resume.getUuid() + " is not exist in storage");
        }
    }

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

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
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
