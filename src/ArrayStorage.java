
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void update(Resume resume) {
        if (resumeIsExistInStrorage(resume.uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(resume.uuid)) {
                    storage[i] = resume;
                    break;
                }
            }
        } else {
            System.out.println("Resume " + resume.uuid + " is not exist in storage");
        }
    }

    void save(Resume r) {
        if (!resumeIsExistInStrorage(r.uuid)) {
            if (size < storage.length) {
                storage[size] = r;
                size++;
            }
        } else {
            System.out.println("Resume " + r.uuid + " is exist in storage");
        }
    }

    Resume get(String uuid) {
        if (resumeIsExistInStrorage(uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    return storage[i];
                }
            }
        } else {
            System.out.println("Resume " + uuid + " is not exist in storage");
        }
        return null;
    }

    void delete(String uuid) {
        if (resumeIsExistInStrorage(uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    size--;
                    System.arraycopy(storage, i + 1, storage, i, storage.length - i - 1);
                    break;
                }
            }
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

    private boolean resumeIsExistInStrorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return true;
            }
        }
        return false;
    }

}
