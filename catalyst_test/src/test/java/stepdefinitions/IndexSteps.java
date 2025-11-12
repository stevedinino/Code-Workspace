package stepdefinitions;

import utils.Locator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LocatorRepository;

import java.time.Duration;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexSteps {

    private final WebDriver driver;
    private final Properties config;

    public IndexSteps(WebDriver driver, Properties config) {
        this.driver = driver;
        this.config = config;
    }

    public void interactWithElement(String key) {
        Locator locator = LocatorRepository.get(key);
        By by = By.xpath(locator.getXpath());

        int timeoutSeconds = Integer.parseInt(config.getProperty("wait.timeout.seconds", "10"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        switch (locator.getAction()) {
            case "click":
                WebElement clickable = wait.until(ExpectedConditions.elementToBeClickable(by));
                clickable.click();
                break;

            case "assert":
                WebElement visible = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                switch (locator.getType()) {
                    case "text":
                    case "list":
                        assertThat(visible.isDisplayed()).isTrue();
                        break;
                    case "image":
                        assertThat(visible.getTagName()).isEqualTo("img");
                        assertThat(visible.isDisplayed()).isTrue();
                        break;
                    case "modal":
                        assertThat(visible.isDisplayed()).isTrue();
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported type: " + locator.getType());
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported action: " + locator.getAction());
        }
    }
}
