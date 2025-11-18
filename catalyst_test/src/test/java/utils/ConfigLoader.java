package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("❌ config.properties not found in classpath");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load config.properties", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getBrowser() {
        return properties.getProperty("browser");
    }

    public static String getWaitTimeoutSeconds() {
        return properties.getProperty("wait.timeout.seconds");
    }

    public static String getReportTheme() {
        return properties.getProperty("report.theme");
    }

    public static String getDataFolder() {
        String folder = properties.getProperty("data.folder");
        if (folder == null || folder.isBlank()) {
            throw new RuntimeException("❌ Missing config key: data.folder");
        }
        return folder;
    }

    // ✅ Generic accessor for dynamic config keys
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException("❌ Missing config key: " + key);
        }
        return value;
    }
}