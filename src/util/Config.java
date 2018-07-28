package util;

import storage.SqlStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final File PROPS = new File("config/resumes.properties");
    private Properties props = new Properties();
    private File storageDir;

    private final SqlStorage sqlStorage;

    public static Config getInstance() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = new FileInputStream(PROPS)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            sqlStorage = new SqlStorage(url, user, password);
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file" + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }
}
