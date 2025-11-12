package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XPathRepository {

    private static final Map<String, Locator> locatorMap = new HashMap<>();

    public static void loadPage(String pageName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String filePath = "test-data/" + pageName + ".json";
            InputStream input = XPathRepository.class.getClassLoader().getResourceAsStream(filePath);
            if (input == null) {
                throw new RuntimeException("File not found: " + filePath);
            }

            JsonNode root = mapper.readTree(input);

            Iterator<Map.Entry<String, JsonNode>> sections = root.fields();
            while (sections.hasNext()) {
                Map.Entry<String, JsonNode> section = sections.next();
                JsonNode group = section.getValue();

                Iterator<Map.Entry<String, JsonNode>> entries = group.fields();
                while (entries.hasNext()) {
                    Map.Entry<String, JsonNode> entry = entries.next();
                    JsonNode node = entry.getValue();

                    Locator locator = new Locator();
                    locator.xpath = node.has("xpath") ? node.get("xpath").asText() : null;
                    locator.action = node.has("action") ? node.get("action").asText() : null;
                    locator.type = node.has("type") ? node.get("type").asText() : null;
                    locator.data = node.has("data") ? node.get("data").asText() : null;

                    locatorMap.put(entry.getKey(), locator);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load page: " + pageName, e);
        }
    }

    public static Locator get(String key) {
        return locatorMap.getOrDefault(key, null);
    }

    public static boolean contains(String key) {
        return locatorMap.containsKey(key);
    }

    public static void clear() {
        locatorMap.clear();
    }
}