import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        Arrays.fill(storage, null);
        size = 0;
    }

    void update(Resume resume) {
        int i = getIndexFromStorage(resume.uuid);
        if (i != -1) {
            storage[i] = resume;
        } else {
            System.out.println("Resume " + resume.uuid + " is not exist in storage");
        }
    }

    void save(Resume r) {
        if (size + 1 > storage.length) {
            System.out.println("the maximum storage size is exceeded");
            return;
        }
        int i = getIndexFromStorage(r.uuid);
        if (i == -1) {
            storage[size++] = r;
        } else {
            System.out.println("Resume " + r.uuid + " is exist in storage");
        }
    }

    Resume get(String uuid) {
        int i = getIndexFromStorage(uuid);
        if (i != -1) {
            return storage[i];
        } else {
            System.out.println("Resume " + uuid + " is not exist in storage");
        }
        return null;
    }

    void delete(String uuid) {
        int i = getIndexFromStorage(uuid);
        if (i != -1) {
            storage[i] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Resume " + uuid + " is not exist in storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] r = new Resume[size];
        System.arraycopy(storage, 0, r, 0, size);
        return r;
    }

    int size() {
        return size;
    }

    private int getIndexFromStorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
