package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
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
        return new ArrayList<>(listStorage);
    }

    @Override
    protected void updateResume(Integer searchKey, Resume resume) {
        listStorage.set(searchKey, resume);
    }

    @Override
    protected void saveResume(Integer searchKey, Resume resume) {
        listStorage.add(resume);
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return listStorage.get(searchKey);
    }

    @Override
    protected void deleteResume(Integer searchKey) {
        listStorage.remove(searchKey.intValue());
    }

    @Override
    protected boolean isContains(Integer searchKey) {
        return searchKey != null;
    }

    @Override
    protected Integer getSearchKey(String key) {
        for (int i = 0; i < size(); i++) {
            if (listStorage.get(i).getUuid().equals(key)) {
                return i;
            }
        }
        return null;
    }
}
