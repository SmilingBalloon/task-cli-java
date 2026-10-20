package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class TaskConfig {
    private Properties properties = new Properties();
    private final String propPath = "config/app.properties";

    public Properties getProperties() {
        return properties;
    }

    public void ensureFileExists(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (path.getParent() != null)
            Files.createDirectories(path.getParent());
        if (Files.notExists(path))
            Files.createFile(path);
    }
    
    public void loadProperties() {
        try (FileInputStream in = new FileInputStream(propPath)) {
            ensureFileExists(propPath);
            properties.load(in);
        } catch (FileNotFoundException exception) {
            System.err.println("[CONFIG] File not found: " + exception.getMessage());
        } catch (IOException exception) {
            System.err.println("[CONFIG] Error while reading file: " + exception.getMessage());
        }
    }

    public void saveProperties() {
        try (FileOutputStream out = new FileOutputStream(propPath)) {
            ensureFileExists(propPath);
            properties.store(out, "Current id");
        } catch (FileNotFoundException exception) {
            System.err.println("[CONFIG] File not found: " + exception.getMessage());
        } catch (IOException exception) {
            System.err.println("[CONFIG] Error while writing file: " + exception.getMessage());
        }
    }

    public int getCurrentId() {
        String id = properties.getProperty("id");
        if (id == null) {
            System.err.println("[CONFIG] Missing properties: id");
            return -1;
        }
        try {
            int currentId = Integer.parseInt(id);
            properties.setProperty("id", Integer.valueOf(currentId + 1).toString());
            return currentId;
        } catch (NumberFormatException exception) {
            System.err.println("[CONFIG] Invalid id: " + exception.getMessage());
            return -1;
        }
    }
}
