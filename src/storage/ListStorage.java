package storage;

import model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    private final ArrayList<Resume> listStorage = new ArrayList<>(STORAGE_LIMIT);

    @Override
    protected int getIndex(String uuid) {
        for (Resume resume : listStorage) {
            if (resume.getUuid().equals(uuid)) {
                return listStorage.indexOf(resume);
            }
        }
        return -1;
    }

    @Override
    protected void updateResume(int index, Resume resume) {
        listStorage.set(index, resume);
    }

    @Override
    protected Resume getResume(int index) {
        return listStorage.get(index);
    }

    @Override
    protected void insertResume(Resume resume, int index) {
        addResume(resume, index);
    }

    @Override
    protected void removeResume(int index) {
        deleteResume(index);
    }

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
    protected void deleteResume(int index) {
        listStorage.remove(index);
    }

    @Override
    protected void addResume(Resume resume, int index) {
        listStorage.add(resume);
    }
}
