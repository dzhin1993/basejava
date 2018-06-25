package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    @Override
    public void update(Resume resume) {
        updateResume(getIfExist(resume.getUuid()), resume);
    }

    @Override
    public void save(Resume resume) {
        getIfNotExist(resume.getUuid());
        saveResume(resume);
    }

    @Override
    public Resume get(String uuid) {
        return getResume(getIfExist(uuid));
    }

    @Override
    public void delete(String uuid) {
        deleteResume(getIfExist(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = getResumeList();
        resumeList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumeList;
    }


    private Object getIfExist(String uuid) {
        Object key = getSearchKey(uuid);
        if (!containsSearchKey(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    private void getIfNotExist(String uuid) {
        if (containsSearchKey(getSearchKey(uuid))) {
            throw new ExistStorageException(uuid);
        }
    }

    protected abstract List<Resume> getResumeList();

    protected abstract void updateResume(Object searchKey, Resume resume);

    protected abstract void saveResume(Resume resume);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void deleteResume(Object searchKey);

    protected abstract boolean containsSearchKey(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

}
