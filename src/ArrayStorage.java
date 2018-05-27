
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        for (Resume resume : storage)
            if (resume != null)
                resume = null;
        size = 0;
    }

    void save(Resume r) {
        for (Resume resume : storage) {
            if (resume == null)
                break;
            if (resume.uuid.equals(r.uuid))
                return;
        }
        if (size < storage.length) {
            storage[size] = r;
            size++;
        }
    }

    Resume get(String uuid) {
        Resume r = null;
        for (Resume resume : storage) {
            if (resume == null)
                break;
            if (resume.uuid.equals(uuid)) {
                r = resume;
                break;
            }
        }
        return r;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                size--;
                System.arraycopy(storage, i + 1, storage, i, storage.length - i - 1);
                break;
            }
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

}
