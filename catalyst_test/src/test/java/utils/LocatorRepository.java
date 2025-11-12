package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocatorRepository {
    private static final String JSON_PATH = "locators.json";
    private static Map<String, Locator> locatorMap = new HashMap<>();

    public static void load() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            locatorMap = mapper.readValue(new File(JSON_PATH), new TypeReference<Map<String, Locator>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to load locators.json", e);
        }
    }

    public static Locator get(String key) {
        if (!locatorMap.containsKey(key)) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }
        return locatorMap.get(key);
    }

    public static void clear() {
        locatorMap.clear();
    }
}