package utils;

import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_PATH = "/config.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getResourceAsStream(CONFIG_PATH)) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static Theme getTheme() {
        String value = props.getProperty("report.theme", "standard").toUpperCase();
        try {
            return Theme.valueOf(value);
        } catch (IllegalArgumentException e) {
            return Theme.STANDARD;
        }
    }

    public static Properties getAll() {
        return props;
    }
}