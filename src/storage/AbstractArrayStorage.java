package storage;

import exception.StorageException;
import model.Resume;

import java.util.Arrays;
import java.util.List;

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

    @Override
    protected void updateResume(Object searchKey, Resume resume) {
        storage[(Integer) searchKey] = resume;
    }

    @Override
    protected void saveResume(Object searchKey, Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume((Integer) searchKey, resume);
        size++;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage[(Integer) searchKey];
    }

    @Override
    protected void deleteResume(Object searchKey) {
        removeResume((Integer) searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isContains(Object searchKey) {
        return (Integer) searchKey > -1;
    }

    @Override
    protected List<Resume> getResumeList() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    protected abstract void insertResume(int index, Resume resume);

    protected abstract void removeResume(int index);
}