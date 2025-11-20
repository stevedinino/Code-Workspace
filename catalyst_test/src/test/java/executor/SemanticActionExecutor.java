package executor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigLoader;
import utils.Locator;
import utils.LocatorRepository;
import utils.ReportLogger;
import validator.SemanticValidator;

import java.time.Duration;

public class SemanticActionExecutor {

    public static void execute(WebDriver driver, String key) {
        Locator locator = LocatorRepository.get(key);
        String xpath = locator.getXpath();
        String action = locator.getAction();

        switch (action) {
            case "click":
                try {
                    int timeout = Integer.parseInt(ConfigLoader.getWaitTimeoutSeconds());
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
                    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

                    ReportLogger.verbose("✅ Found element for key: " + key +
                        " — Tag: <" + element.getTagName() + ">, Outer HTML: " + element.getAttribute("outerHTML"));

                    element.click();
                    ReportLogger.info("🖱️ Clicked element for key: " + key);
                } catch (Exception e) {
                    ReportLogger.error("❌ Failed to click element for key: " + key + " — " + e.getMessage());
                    throw e;
                }
                break;

            case "assert":
            case "assert_title":
            case "assert_visible":
            case "assert_exists":
            case "assert_alt":
                ReportLogger.verbose("🔍 No interaction required for action: " + action + " (key: " + key + ")");
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