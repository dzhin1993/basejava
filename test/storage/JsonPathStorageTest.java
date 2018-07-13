package storage;

import storage.serialize.JsonStreamSerialization;

public class JsonPathStorageTest extends AbstractStorageTest {
    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.toString(), new JsonStreamSerialization()));
    }
}
