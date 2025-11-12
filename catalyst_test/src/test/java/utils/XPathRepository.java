package utils;

//import utils.Locator;
import java.util.HashMap;
import java.util.Map;

public class XPathRepository {

    private static final Map<String, Map<String, Locator>> pageLocators = new HashMap<>();

    public static void clear() {
        pageLocators.clear();
    }

    public static void add(String page, String key, Locator locator) {
        pageLocators.computeIfAbsent(page, k -> new HashMap<>()).put(key, locator);
    }

    public static Map<String, Locator> get(String page) {
        return pageLocators.getOrDefault(page, new HashMap<>());
    }

    public static void logPage(String page) {
        System.out.println("Loaded XPath repository for page: " + page);
        Map<String, Locator> locators = get(page);
        for (Map.Entry<String, Locator> entry : locators.entrySet()) {
            Locator locator = entry.getValue();
            System.out.println("  " + entry.getKey() + " → " +
                locator.getXpath() + " | " +
                locator.getAction() + " | " +
                locator.getType() + " | " +
                locator.getResult());
        }
    }
}