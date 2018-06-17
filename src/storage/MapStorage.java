package storage;

import model.Resume;

import java.util.HashMap;

public class MapStorage extends AbstractStorage {
    private HashMap<String, Resume> mapStorage = new HashMap<>(STORAGE_LIMIT);

    @Override
    protected int getIndex(String uuid) {
        return 0;
    }

    @Override
    protected void updateResume(int index, Resume resume) {

    }

    @Override
    protected Resume getResume(int index) {
        return null;
    }

    @Override
    protected void deleteResume(int index) {

    }

    @Override
    protected void addResume(Resume resume, int index) {

    }

    @Override
    protected void insertResume(Resume resume, int index) {

    }

    @Override
    protected void removeResume(int index) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Resume[] getAll() {
        return new Resume[0];
    }

    @Override
    public int size() {
        return 0;
    }
}
