package storage;

import model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    private final ArrayList<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    public Resume[] getAll() {
        return listStorage.toArray(new Resume[0]);
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
    protected Integer getKey(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (listStorage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean containsKey(Object key) {
        return (Integer) key >= 0;
    }
}
