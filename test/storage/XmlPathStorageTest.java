package storage;

import storage.serialize.XmlStreamSerialization;

public class XmlPathStorageTest extends AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.toString(), new XmlStreamSerialization()));
    }
}
