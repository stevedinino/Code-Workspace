package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import utils.Locator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LocatorRepository {

    private static final String BASE_PATH = "/test-data/";
    private static final Map<String, Map<String, Locator>> pageLocators = new HashMap<>();

    public static void load(String pageName) {
        String normalized = pageName.toLowerCase();
        if (pageLocators.containsKey(normalized)) return;

        String resourcePath = BASE_PATH + normalized + ".json";
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream input = LocatorRepository.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new RuntimeException("Locator file not found in classpath: " + resourcePath);
            }
            Map<String, Locator> locators = mapper.readValue(input, new TypeReference<Map<String, Locator>>() {});
            pageLocators.put(normalized, locators);
            System.out.println("Loaded locators for page: " + normalized);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load locator file: " + resourcePath, e);
        }
    }

    public static Locator get(String key) {
        Map<String, Locator> locators = pageLocators.get("index"); // hardcoded for IndexSteps
        if (locators == null) {
            throw new IllegalStateException("Locator file not loaded for page: index");
        }
        if (!locators.containsKey(key)) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }
        return locators.get(key);
    }

    public static void clear() {
        pageLocators.clear();
    }
}