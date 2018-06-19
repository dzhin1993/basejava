package storage;

import exception.StorageException;
import model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    protected void updateResume(int key, Resume resume) {
        storage[key] = resume;
    }

    @Override
    protected void saveResume(Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, getKey(resume.getUuid()));
        size++;
    }

    @Override
    protected Resume getResume(int key) {
        return storage[key];
    }

    @Override
    protected void deleteResume(int key) {
        removeResume(key);
        storage[size - 1] = null;
        size--;
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void removeResume(int index);
}