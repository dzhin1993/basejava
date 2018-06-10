import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void update(Resume resume) {
        int index = getIndexFromStorage(resume.uuid);
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("Resume " + resume.uuid + " is not exist in storage");
        }
    }

    void save(Resume r) {
        if (size >= storage.length) {
            System.out.println("the maximum storage size is exceeded");
            return;
        }
        int index = getIndexFromStorage(r.uuid);
        if (index == -1) {
            storage[size++] = r;
        } else {
            System.out.println("Resume " + r.uuid + " is exist in storage");
        }
    }

    Resume get(String uuid) {
        int index = getIndexFromStorage(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.println("Resume " + uuid + " is not exist in storage");
        }
        return null;
    }

    void delete(String uuid) {
        int index = getIndexFromStorage(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
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
     return Arrays.copyOfRange(storage,0,size);
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
