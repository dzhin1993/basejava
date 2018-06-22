package storage;

import model.Resume;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapUuidStorage extends AbstractStorage {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected void updateResume(Object key, Resume resume) {
        mapStorage.put((String) key, resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        mapStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object key) {
        return mapStorage.get((String) key);
    }

    @Override
    protected void deleteResume(Object key) {
        mapStorage.remove((String) key);
    }

    @Override
    public void clear() {
        mapStorage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        return mapStorage
                .values()
                .stream()
                .sorted(Comparator.comparing(Resume::getFullName))
                .collect(Collectors.toList());
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
    protected Object getSearchKey(String uuid) {
        return uuid;
    }
}
