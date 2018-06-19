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
    protected void updateResume(int key, Resume resume) {
        listStorage.set(key, resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        listStorage.add(resume);
    }

    @Override
    protected Resume getResume(int key) {
        return listStorage.get(key);
    }

    @Override
    protected void deleteResume(int key) {
        listStorage.remove(key);
    }

    @Override
    protected int getKey(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (listStorage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
