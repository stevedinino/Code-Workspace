package utils;

import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_PATH = "config.properties";
    private static Properties props = new Properties();

    static {
        try (FileInputStream input = new FileInputStream(CONFIG_PATH)) {
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