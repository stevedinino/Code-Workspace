package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigLoader;
import utils.DriverFactory;
import utils.LocatorRepository;
import utils.ReportLogger;
import executor.SemanticActionExecutor;

import java.time.Duration;

public class CatalystSteps {

    private WebDriver driver;

    @Given("the user launches Catalyst {string} page")
    public void launchCatalystWebsite(String page) {
        driver = DriverFactory.getDriver();
        String normalizedPage = page.toLowerCase();
        String url = ConfigLoader.getBaseUrl() + "/" + normalizedPage + ".html";
        driver.get(url);
        ReportLogger.info("🌐 Navigated to: " + url);

        try {
            int timeout = Integer.parseInt(ConfigLoader.getWaitTimeoutSeconds());
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

            // Wait for relaxed heading match on the target page
            String headingXpath = "//h1[contains(text(),'" + page + "')]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(headingXpath)));

            ReportLogger.info("✅ Page heading loaded: " + headingXpath);
        } catch (Exception e) {
            ReportLogger.error("❌ Failed to confirm page load for: " + page + " — " + e.getMessage());
            throw e;
        }
    }

    @And("the XPath repository for {string} is loaded")
    public void loadXPathRepository(String pageName) {
        String folder = ConfigLoader.getDataFolder();
        LocatorRepository.loadRepository(folder, pageName);
        ReportLogger.info("📦 Loaded XPath repository for page: " + pageName);
    }

    @When("the user interacts with {string}")
    public void userInteractsWith(String key) {
        SemanticActionExecutor.execute(driver, key);
    }

    @Then("the result of {string} should be validated")
    public void validateResult(String key) {
        SemanticActionExecutor.validate(driver, key);
    }
}