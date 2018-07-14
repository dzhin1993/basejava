package storage;

import storage.serialize.DataStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest {
    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.toString(), new DataStreamSerializer()));
    }
}
