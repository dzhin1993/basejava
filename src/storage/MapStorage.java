package storage;

import model.Resume;

import java.util.HashMap;

public class MapStorage extends AbstractStorage {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected void updateResume(int key, Resume resume) {

    }

    @Override
    protected void saveResume(Resume resume) {

    }

    @Override
    protected Resume getResume(int key) {
        return null;
    }

    @Override
    protected void deleteResume(int key) {

    }

    @Override
    protected int getKey(String uuid) {
        return 0;
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
