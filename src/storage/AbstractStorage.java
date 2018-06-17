package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import exception.StorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            updateResume(index, resume);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public void save(Resume resume) {
        if (size() == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            addResume(resume, index);
        } else {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return getResume(index);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            deleteResume(index);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }


    protected abstract int getIndex(String uuid);

    protected abstract void updateResume(int index, Resume resume);

    protected abstract Resume getResume(int index);

    protected abstract void deleteResume(int index);

    protected abstract void addResume(Resume resume, int index);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void removeResume(int index);
}
