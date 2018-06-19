package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public void update(Resume resume) {
        updateResume(checkExistence(resume.getUuid()), resume);
    }

    @Override
    public void save(Resume resume) {
        checkNotExistence(resume.getUuid());
        saveResume(resume);
    }

    @Override
    public Resume get(String uuid) {
        return getResume(checkExistence(uuid));
    }

    @Override
    public void delete(String uuid) {
        deleteResume(checkExistence(uuid));
    }


    private int checkExistence(String uuid) {
        int key = getKey(uuid);
        if (key < 0) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    private void checkNotExistence(String uuid) {
        int key = getKey(uuid);
        if (key > 0) {
            throw new ExistStorageException(uuid);
        }
    }

    protected abstract void updateResume(int key, Resume resume);

    protected abstract void saveResume(Resume resume);

    protected abstract Resume getResume(int key);

    protected abstract void deleteResume(int key);

    protected abstract int getKey(String uuid);

}
