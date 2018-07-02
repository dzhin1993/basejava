package storage;

import exception.StorageException;
import model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected List<Resume> getResumeList() {
        ArrayList<Resume> resumes = new ArrayList<>();
        File[] files = directory.listFiles();
        for(File currentFile: files){
            try {
                resumes.add(doRead(currentFile));
            } catch (IOException e) {
                throw new StorageException("IO error", currentFile.getName(), e);
            }
        }
        return resumes;
    }

    @Override
    protected void updateResume(File file, Resume resume) {
        try {
            doWrite(file, resume);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void saveResume(File file, Resume resume) {
        try {
            file.createNewFile();
            doWrite(file, resume);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume getResume(File file) {
       File[] files = directory.listFiles();
       for(File currentFile: files){
           if(currentFile.getName().equals(file.getName())){
               try {
                   return doRead(file);
               } catch (IOException e) {
                   throw new StorageException("IO error", file.getName(), e);
               }
           }
       }
        return null;
    }

    @Override
    protected void deleteResume(File file) {
        file.delete();
    }

    @Override
    protected boolean isContains(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String key) {
        return new File(directory, key);
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        for(File currentFile: files){
            currentFile.delete();
        }
    }

    @Override
    public int size() {
        return (int) directory.length();
    }

    protected abstract void doWrite(File file, Resume resume) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;
}
