package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import model.Resume;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<key> implements Storage {
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

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = getResumeList();
        resumeList.sort(Comparator.comparing(Resume::getFullName));
        return resumeList;
    }


    private key checkExistence(String uuid) {
        key key = getSearchKey(uuid);
        if (!containsSearchKey(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    private void checkNotExistence(String uuid) {
        if (containsSearchKey(getSearchKey(uuid))) {
            throw new ExistStorageException(uuid);
        }
    }

    protected abstract List<Resume> getResumeList();

    protected abstract void updateResume(key searchKey, Resume resume);

    protected abstract void saveResume(Resume resume);

    protected abstract Resume getResume(key searchKey);

    protected abstract void deleteResume(key searchKey);

    protected abstract boolean containsSearchKey(key searchKey);

    protected abstract key getSearchKey(String uuid);

}
