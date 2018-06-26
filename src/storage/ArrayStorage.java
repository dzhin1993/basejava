package storage;

import model.Resume;


/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String key) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertResume(int index, Resume resume) {
        storage[size] = resume;
    }

    @Override
    protected void removeResume(int index) {
        storage[index] = storage[size - 1];
    }
}
