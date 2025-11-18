package executor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Locator;
import utils.LocatorRepository;
import utils.ReportLogger;
import validator.SemanticValidator;

public class SemanticActionExecutor {

    public static void execute(WebDriver driver, String key) {
        Locator locator = LocatorRepository.get(key);
        WebElement element = driver.findElement(By.xpath(locator.getXpath()));

        switch (locator.getAction()) {
            case "click":
                element.click();
                ReportLogger.info("🖱️ Clicked element for key: " + key);
                break;

            case "assert":
                String actualText = element.getText().trim();
                if (actualText.equals(locator.getResult())) {
                    ReportLogger.info("✅ Assertion passed for key: " + key);
                } else {
                    ReportLogger.error("❌ Assertion failed for key: " + key +
                        ". Expected: \"" + locator.getResult() + "\", Found: \"" + actualText + "\"");
                }
                break;

            case "assert_visible":
                if (element.isDisplayed()) {
                    ReportLogger.info("👁️ Element is visible for key: " + key);
                } else {
                    ReportLogger.error("❌ Element not visible for key: " + key);
                }
                break;

            case "assert_exists":
                ReportLogger.info("📌 Element exists for key: " + key);
                break;

            default:
                ReportLogger.warn("⚠️ No interaction logic defined for action: " + locator.getAction());
        }
    }

    public static void validate(WebDriver driver, String key) {
        Locator locator = LocatorRepository.get(key);
        SemanticValidator.validate(driver, locator.getXpath(), key);
    }
}