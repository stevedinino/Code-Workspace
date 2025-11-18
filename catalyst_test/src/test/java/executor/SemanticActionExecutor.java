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
        String xpath = locator.getXpath();
        String action = locator.getAction();

        switch (action) {
            case "click":
                WebElement clickable = driver.findElement(By.xpath(xpath));
                clickable.click();
                ReportLogger.info("🖱️ Clicked element for key: " + key);
                break;

            case "assert":
            case "assert_title":
            case "assert_visible":
            case "assert_exists":
            case "assert_alt":
                // No interaction needed—these are pure validations
                ReportLogger.info("🔍 No interaction required for action: " + action + " (key: " + key + ")");
                break;

            default:
                ReportLogger.warn("⚠️ No execution logic defined for action: " + action + " (key: " + key + ")");
        }
    }

    public static void validate(WebDriver driver, String key) {
        Locator locator = LocatorRepository.get(key);
        SemanticValidator.validate(driver, locator, key);
    }
}