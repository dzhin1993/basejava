package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListStorage extends AbstractStorage {
    private final List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        return listStorage.stream().sorted(Comparator.comparing(Resume::getFullName)).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    @Override
    protected void updateResume(Object key, Resume resume) {
        listStorage.set((Integer) key, resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        listStorage.add(resume);
    }

    @Override
    protected Resume getResume(Object key) {
        return listStorage.get((Integer) key);
    }

    @Override
    protected void deleteResume(Object key) {
        int index = (Integer) key;
        listStorage.remove(index);
    }

    @Override
    protected boolean containsSearchKey(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (listStorage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }
}
