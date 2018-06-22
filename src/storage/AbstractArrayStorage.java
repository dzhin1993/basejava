package storage;

import exception.StorageException;
import model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Resume> getAllSorted() {
        return Arrays.stream(Arrays.copyOfRange(storage, 0, size))
                .sorted(Comparator.comparing(Resume::getFullName))
                .collect(Collectors.toList());
    }

    @Override
    protected void updateResume(Object key, Resume resume) {
        storage[(Integer) key] = resume;
    }

    @Override
    protected void saveResume(Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, (Integer) getSearchKey(resume.getUuid()));
        size++;
    }

    @Override
    protected Resume getResume(Object key) {
        return storage[(Integer) key];
    }

    @Override
    protected void deleteResume(Object key) {
        removeResume((Integer) key);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean containsSearchKey(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void removeResume(int index);
}