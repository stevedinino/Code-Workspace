package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class LocatorRepository {

    private static Map<String, Locator> repository;

    public static void loadRepository(String folder, String pageName) {
        try {
            String path = folder + "/" + pageName + ".json";
            InputStream inputStream = LocatorRepository.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                throw new RuntimeException("❌ Locator file not found: " + path);
            }

            ObjectMapper mapper = new ObjectMapper();
            repository = mapper.readValue(inputStream, new TypeReference<Map<String, Locator>>() {});
            ReportLogger.verbose("📦 Locator JSON loaded successfully for page: " + pageName);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load locator JSON: " + folder + "/" + pageName + ".json", e);
        }
    }

    public static Locator get(String key) {
        if (repository == null) {
            throw new IllegalStateException("❌ Locator repository not loaded.");
        }
        Locator locator = repository.get(key);
        if (locator == null) {
            throw new RuntimeException("❌ Locator key not found: " + key);
        }
        return locator;
    }
}