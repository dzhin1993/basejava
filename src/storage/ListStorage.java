package storage;

import model.Resume;
import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    @Override
    protected List<Resume> getResumeList() {
        return listStorage;
    }

    @Override
    protected void updateResume(Object searchKey, Resume resume) {
        listStorage.set((Integer) searchKey, resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        listStorage.add(resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return listStorage.get((Integer) searchKey);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        int index = (Integer) searchKey;
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
