package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapUuidStorage extends AbstractStorage {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected List<Resume> getResumeList() {
        return new ArrayList<>(mapStorage.values());
    }

    @Override
    protected void updateResume(Object searchKey, Resume resume) {
        mapStorage.put((String) searchKey, resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        mapStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return mapStorage.get((String) searchKey);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        mapStorage.remove((String) searchKey);
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
    protected boolean containsSearchKey(Object searchKey) {
        return mapStorage.containsKey((String) searchKey);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }
}
