package storage;

import util.Config;

public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.getInstance().getSqlStorage());
    }
}