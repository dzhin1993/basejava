package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    @Override
    public final void update(Resume resume) {
        updateResume(getIfExist(resume.getUuid()), resume);
    }

    @Override
    public final void save(Resume resume) {
        Object key = getIfNotExist(resume.getUuid());
        saveResume(key, resume);
    }

    @Override
    public final Resume get(String uuid) {
        return getResume(getIfExist(uuid));
    }

    @Override
    public final void delete(String uuid) {
        deleteResume(getIfExist(uuid));
    }

    @Override
    public final List<Resume> getAllSorted() {
        List<Resume> resumeList = getResumeList();
        resumeList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumeList;
    }


    private Object getIfExist(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isContains(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getIfNotExist(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isContains(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract List<Resume> getResumeList();

    protected abstract void updateResume(Object searchKey, Resume resume);

    protected abstract void saveResume(Object searchKey, Resume resume);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void deleteResume(Object searchKey);

    protected abstract boolean isContains(Object searchKey);

    protected abstract Object getSearchKey(String key);

}
