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


    private Object checkExistence(String uuid) {
        Object key = getKey(uuid);
        if (!containsKey(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    private void checkNotExistence(String uuid) {
        if (containsKey(getKey(uuid))) {
            throw new ExistStorageException(uuid);
        }
    }

    protected abstract boolean containsKey(Object key);

    protected abstract void updateResume(Object key, Resume resume);

    protected abstract void saveResume(Resume resume);

    protected abstract Resume getResume(Object key);

    protected abstract void deleteResume(Object key);

    protected abstract Object getKey(String uuid);

}
