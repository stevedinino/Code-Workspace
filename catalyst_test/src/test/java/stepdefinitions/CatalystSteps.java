package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import utils.ConfigLoader;
import utils.DriverFactory;
import utils.LocatorRepository;
import utils.ReportLogger;
import executor.SemanticActionExecutor;

public class CatalystSteps {

    private WebDriver driver;


    @Given("the user launches Catalyst web site")
    public void launchCatalystWebsite() {
        driver = DriverFactory.getDriver();
        String url = ConfigLoader.getBaseUrl();
        driver.get(url);
        ReportLogger.info("🌐 Navigated to: " + url);
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