package validator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ReportLogger;

public class SemanticValidator {

    public static void validate(WebDriver driver, String locator, String key) {
        WebElement element = driver.findElement(By.xpath(locator));

        // Basic validation logic
        if (key.toLowerCase().contains("visible")) {
            boolean isDisplayed = element.isDisplayed();
            if (isDisplayed) {
                ReportLogger.info("✅ Element is visible for key: " + key);
            } else {
                ReportLogger.error("❌ Element is NOT visible for key: " + key);
            }
        } else if (key.toLowerCase().contains("text")) {
            String text = element.getText();
            if (text != null && !text.isBlank()) {
                ReportLogger.info("✅ Element has text for key: " + key + " → " + text);
            } else {
                ReportLogger.error("❌ Element has no text for key: " + key);
            }
        } else {
            ReportLogger.warn("⚠️ No validation logic defined for key: " + key);
        }
    }
}