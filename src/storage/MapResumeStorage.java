package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapResumeStorage extends AbstractStorage {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected List<Resume> getResumeList() {
        return new ArrayList<>(mapStorage.values());
    }

    @Override
    protected void updateResume(Object searchKey, Resume resume) {
        mapStorage.put(((Resume) searchKey).getUuid(), resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        mapStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return mapStorage.get(((Resume) searchKey).getUuid());
    }

    @Override
    protected void deleteResume(Object searchKey) {
        mapStorage.remove(((Resume) searchKey).getUuid());
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
        return searchKey != null;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return mapStorage.get(uuid);
    }
}
