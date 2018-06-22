package storage;

import model.Resume;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapResumeStorage extends AbstractStorage {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected void updateResume(Object key, Resume resume) {
        mapStorage.put(((Resume) key).getUuid(), resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        mapStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object key) {
        return mapStorage.get(((Resume) key).getUuid());
    }

    @Override
    protected void deleteResume(Object key) {
        mapStorage.remove(((Resume) key).getUuid());
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
        return searchKey != null;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return mapStorage.get(uuid);
    }
}
