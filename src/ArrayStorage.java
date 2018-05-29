
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        for (int i=0; i < size; i++){
                storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        for (int i=0; i < size; i++) {
            if (storage[i].uuid.equals(r.uuid)){
                return;
            }
        }
        if (size < storage.length) {
            storage[size] = r;
            size++;
        }
    }

    Resume get(String uuid) {
        for (int i=0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
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
