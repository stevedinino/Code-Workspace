package stepdefinitions;

import io.cucumber.java.en.*;
import utils.Locator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverFactory;
import utils.LocatorRepository;
import utils.ConfigLoader;
import com.aventstack.extentreports.Status;
import hooks.Hooks;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexSteps {

    private final WebDriver driver = DriverFactory.getDriver();

    @Given("the user launches Catalyst web site")
    public void the_user_launches_catalyst_web_site() {
        String baseUrl = ConfigLoader.get("base.url");
        Hooks.getTest().log(Status.INFO, "Launching Catalyst site at: " + baseUrl);
        driver.get(baseUrl);
    }

    @Given("the XPath repository for {string} is loaded")
    public void the_x_path_repository_for_is_loaded(String pageName) {
        Hooks.getTest().log(Status.INFO, "Loading locator repository for page: " + pageName);
        LocatorRepository.load(pageName);
    }

    @When("the user interacts with {string}")
    public void the_user_interacts_with(String key) {
        Locator locator = LocatorRepository.get(key);
        Hooks.getTest().log(Status.INFO, "Interacting with key: " + key +
            " | type: " + locator.getType() +
            " | action: " + locator.getAction() +
            " | xpath: " + locator.getXpath());

        By by = By.xpath(locator.getXpath());
        int timeout = Integer.parseInt(ConfigLoader.get("wait.timeout.seconds"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

        switch (locator.getAction()) {
            case "click":
                WebElement clickable = wait.until(ExpectedConditions.elementToBeClickable(by));
                clickable.click();
                break;
            case "assert":
                wait.until(ExpectedConditions.presenceOfElementLocated(by));
                break;
            default:
                throw new IllegalArgumentException("Unsupported action: " + locator.getAction());
        }
    }

    @Then("the result of {string} should be validated")
    public void the_result_of_should_be_validated(String key) {
        Locator locator = LocatorRepository.get(key);
        Hooks.getTest().log(Status.INFO, "Validating result for key: " + key +
            " | type: " + locator.getType() +
            " | xpath: " + locator.getXpath());

        By by = By.xpath(locator.getXpath());
        int timeout = Integer.parseInt(ConfigLoader.get("wait.timeout.seconds"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        WebElement element = driver.findElement(by);

        switch (locator.getType()) {
            case "text":
            case "list":
            case "modal":
                assertThat(element.isDisplayed()).isTrue();
                break;
            case "image":
                assertThat(element.getTagName()).isEqualTo("img");
                assertThat(element.isDisplayed()).isTrue();
                break;
            case "link":
                assertThat(element.getTagName()).isEqualTo("a");
                assertThat(element.isDisplayed()).isTrue();
                assertThat(element.getAttribute("href")).isNotEmpty();
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + locator.getType());
        }
    }
}