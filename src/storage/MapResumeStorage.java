package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapResumeStorage extends AbstractStorage<Resume> {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected List<Resume> getResumeList() {
        return new ArrayList<>(mapStorage.values());
    }

    @Override
    protected void updateResume(Resume searchKey, Resume resume) {
        mapStorage.put(searchKey.getUuid(), resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        mapStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Resume searchKey) {
        return mapStorage.get(searchKey.getUuid());
    }

    @Override
    protected void deleteResume(Resume searchKey) {
        mapStorage.remove(searchKey.getUuid());
    }

    @Override
    public void clear() {
        mapStorage.clear();
    }

    @Override
    public int size() {
        return mapStorage.size();
    }

    @Override
    protected boolean containsSearchKey(Resume searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return mapStorage.get(uuid);
    }
}
