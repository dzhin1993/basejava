package storage;

import model.Resume;

import java.util.HashMap;

public class MapStorage extends AbstractStorage  {
    private HashMap<String, Resume> mapStorage = new HashMap<>();

    @Override
    protected boolean containsKey(Object key) {
        return false;
    }

    @Override
    protected void updateResume(Object key, Resume resume) {

    }

    @Override
    protected void saveResume(Resume resume) {

    }

    @Override
    protected Resume getResume(Object key) {
        return null;
    }

    @Override
    protected void deleteResume(Object key) {

    }

    @Override
    protected Object getKey(String uuid) {
        return null;
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
