package utils;

import java.io.InputStream;
import java.util.Properties;

import com.aventstack.extentreports.reporter.configuration.Theme;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static Theme get(String key) {
        return props.getProperty(key);
    }
}
