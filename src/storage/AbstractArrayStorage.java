package storage;

import model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

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

    @Override
    public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("the maximum storage size is exceeded");
            return;
        }
        int index = getIndexFromStorage(r.getUuid());
        if (index < 0) {
           insertResume(r,index);
        } else {
            System.out.println("model.Resume " + r.getUuid() + " is exist in storage");
        }
    }

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

    @Override
    public void delete(String uuid) {
        int index = getIndexFromStorage(uuid);
        if (index >= 0) {
           removeResume(index);
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

    protected abstract int getIndexFromStorage(String uuid);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void removeResume(int index);
}
