package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public final void update(Resume resume) {
        LOG.info("Update " + resume);
        updateResume(getIfExist(resume.getUuid()), resume);
    }

    @Override
    public final void save(Resume resume) {
        LOG.info("Save " + resume);
        SK key = getIfNotExist(resume.getUuid());
        saveResume(key, resume);
    }

    @Override
    public final Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return getResume(getIfExist(uuid));
    }

    @Override
    public final void delete(String uuid) {
        LOG.info("Delete " + uuid);
        deleteResume(getIfExist(uuid));
    }

    @Override
    public final List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> resumeList = getResumeList();
        resumeList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumeList;
    }


    private SK getIfExist(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isContains(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getIfNotExist(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isContains(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract List<Resume> getResumeList();

    protected abstract void updateResume(SK searchKey, Resume resume);

    protected abstract void saveResume(SK searchKey, Resume resume);

    protected abstract Resume getResume(SK searchKey);

    protected abstract void deleteResume(SK searchKey);

    protected abstract boolean isContains(SK searchKey);

    protected abstract SK getSearchKey(String key);

}
