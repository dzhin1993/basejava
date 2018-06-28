package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapUuidStorage extends AbstractStorage<String> {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected List<Resume> getResumeList() {
        return new ArrayList<>(mapStorage.values());
    }

    @Override
    protected void updateResume(String searchKey, Resume resume) {
        mapStorage.put(searchKey, resume);
    }

    @Override
    protected void saveResume(String searchKey, Resume resume) {
        mapStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(String searchKey) {
        return mapStorage.get(searchKey);
    }

    @Override
    protected void deleteResume(String searchKey) {
        mapStorage.remove(searchKey);
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
    protected boolean isContains(String searchKey) {
        return mapStorage.containsKey(searchKey);
    }

    @Override
    protected String getSearchKey(String key) {
        return key;
    }
}
