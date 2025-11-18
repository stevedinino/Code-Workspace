package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import utils.ConfigLoader;
//import utils.DriverFactory;
//import utils.Locator;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
//import java.util.Objects;

public class XPathValidator {

    private static final String TEST_DATA_DIR = "src/test/resources/test-data";
    private static final WebDriver driver = DriverFactory.getDriver();
    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(
        Integer.parseInt(ConfigLoader.get("wait.timeout.seconds"))));
    private static final JavascriptExecutor js = (JavascriptExecutor) driver;

    public static void main(String[] args) throws IOException {
        String baseUrl = ConfigLoader.get("base.url");
        driver.get(baseUrl);
        System.out.println("🔍 Validating XPaths against: " + baseUrl);

        File folder = new File(TEST_DATA_DIR);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("⚠️ No JSON files found in: " + TEST_DATA_DIR);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        for (File file : jsonFiles) {
            System.out.println("\n📄 Validating file: " + file.getName());
            Map<String, Locator> locators = mapper.readValue(file, new TypeReference<>() {});

            for (Map.Entry<String, Locator> entry : locators.entrySet()) {
                String key = entry.getKey();
                Locator locator = entry.getValue();
                String xpath = locator.getXpath();

                try {
                    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
                    js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element);
                    System.out.println("✅ Found: " + key + " → " + xpath);
                } catch (TimeoutException | NoSuchElementException e) {
                    System.out.println("❌ Missing: " + key + " → " + xpath);
                } catch (Exception e) {
                    System.out.println("⚠️ Error: " + key + " → " + e.getMessage());
                }
            }
        }

        driver.quit();
        System.out.println("\n🏁 XPath validation complete.");
    }
}