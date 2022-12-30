package ru.javawebinar.basejava;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Config INSTANCE = new Config();
    private static final File PROPERTIES = new File("config\\resumes.properties");
    private Properties properties = new Properties();
    private File storageDir;

    public static Config getInstance() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    private Config() {
        try (InputStream inputStream = getClass().getResourceAsStream("config\\resumes.properties")){
            properties.load(inputStream);
            storageDir = new File(properties.getProperty("storage.dir"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPERTIES.getAbsolutePath());
        }
    }
}
