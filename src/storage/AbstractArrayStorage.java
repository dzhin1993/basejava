package storage;

import model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("Resume " + resume.getUuid() + " is not exist in storage");
        }
    }

    @Override
    public void save(Resume resume) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("the maximum storage size is exceeded");
            return;
        }
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            insertResume(resume, index);
            size++;
        } else {
            System.out.println("Resume " + resume.getUuid() + " is exist in storage");
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("Resume " + uuid + " is not exist in storage");
        return null;
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            removeResume(index);
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Resume " + uuid + " is not exist in storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void removeResume(int index);
}
