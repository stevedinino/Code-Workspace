package validator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Locator;
import utils.ReportLogger;

public class SemanticValidator {

    public static void validate(WebDriver driver, Locator locator, String key) {
        String xpath = locator.getXpath();
        String action = locator.getAction();
        String expected = locator.getResult();

        switch (action) {
            case "assert":
                validateText(driver, xpath, expected, key);
                break;

            case "assert_visible":
                validateVisibility(driver, xpath, key);
                break;

            case "assert_exists":
                validatePresence(driver, xpath, key);
                break;

            case "assert_alt":
                validateAltText(driver, xpath, expected, key);
                break;

            case "click":
                validateNavigation(driver, expected, key);
                break;
            case "assert_title":
                validatePageTitle(driver, expected, key);
                break;
            default:
                ReportLogger.warn("⚠️ No validation logic defined for action: " + action + " (key: " + key + ")");
        }
    }

    private static void validateText(WebDriver driver, String xpath, String expected, String key) {
        WebElement element = driver.findElement(By.xpath(xpath));
        ReportLogger.info("Validating result for key: " + key);
        String actual = element.getText().trim();
        if (actual.equals(expected)) {
            ReportLogger.pass("✅ Text matches for key: " + key);
        } else {
            ReportLogger.error("❌ Text mismatch for key: " + key +
                ". Expected: \"" + expected + "\", Found: \"" + actual + "\"");
        }
    }

    private static void validateVisibility(WebDriver driver, String xpath, String key) {
        WebElement element = driver.findElement(By.xpath(xpath));
        ReportLogger.info("Validating result for key: " + key);
        if (element.isDisplayed()) {
            ReportLogger.pass("✅ Element is visible for key: " + key);
        } else {
            ReportLogger.error("❌ Element is NOT visible for key: " + key);
        }
    }

    private static void validatePresence(WebDriver driver, String xpath, String key) {
        boolean exists = driver.findElements(By.xpath(xpath)).size() > 0;
        ReportLogger.info("Validating result for key: " + key);
        if (exists) {
            ReportLogger.pass("✅ Element exists for key: " + key);
        } else {
            ReportLogger.error("❌ Element does NOT exist for key: " + key);
        }
    }

    private static void validatePageTitle(WebDriver driver, String expected, String key) {
        String actualTitle = driver.getTitle().trim();
        ReportLogger.info("Validating result for key: " + key);
        if (actualTitle.equals(expected)) {
            ReportLogger.pass("✅ Page title matches for key: " + key);
        } else {
            ReportLogger.error("❌ Page title mismatch for key: " + key +
                ". Expected: \"" + expected + "\", Found: \"" + actualTitle + "\"");
        }
    }
    private static void validateAltText(WebDriver driver, String xpath, String expected, String key) {
        WebElement element = driver.findElement(By.xpath(xpath));
        ReportLogger.info("Validating result for key: " + key);
        String actualAlt = element.getAttribute("alt");
        if (expected.equals(actualAlt)) {
            ReportLogger.pass("✅ Alt text matches for key: " + key);
        } else {
            ReportLogger.error("❌ Alt text mismatch for key: " + key +
                ". Expected: \"" + expected + "\", Found: \"" + actualAlt + "\"");
        }
    }

    private static void validateNavigation(WebDriver driver, String expected, String key) {
        String currentUrl = driver.getCurrentUrl();
        ReportLogger.info("Validating navigation for key: " + key);
        if (currentUrl.contains(expected)) {
            ReportLogger.pass("✅ Navigation validated for key: " + key + " → URL contains: " + expected);
        } else {
            ReportLogger.error("❌ Navigation failed for key: " + key +
                ". Expected URL to contain: \"" + expected + "\", but got: \"" + currentUrl + "\"");
        }
    }
}