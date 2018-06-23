package storage;

import exception.StorageException;
import model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
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
    protected void updateResume(Integer searchKey, Resume resume) {
        storage[searchKey] = resume;
    }

    @Override
    protected void saveResume(Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, getSearchKey(resume.getUuid()));
        size++;
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected void deleteResume(Integer searchKey) {
        removeResume(searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean containsSearchKey(Integer searchKey) {
        return searchKey >= 0;
    }

    @Override
    protected List<Resume> getResumeList() {
        Resume[] resumes = new Resume[size];
        System.arraycopy(storage, 0, resumes, 0, size);
        return Arrays.asList(resumes);
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void removeResume(int index);
}