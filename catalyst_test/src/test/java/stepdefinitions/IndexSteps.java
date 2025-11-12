package stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.DriverFactory;
import utils.XPathRepository;
import utils.Locator;
import hooks.Hooks;
import utils.ConfigLoader;

public class IndexSteps {

    WebDriver driver;

    @Given("the user launches Catalyst web site")
    public void launchCatalystSite() {
        driver = DriverFactory.getDriver();
        String baseUrl = ConfigLoader.get("base.url");
        assertThat(baseUrl).as("Base URL must be defined").isNotNull();
        Hooks.getTest().info("Launching Catalyst site at: " + baseUrl);
        driver.get(baseUrl);
        Hooks.getTest().pass("Catalyst site loaded successfully");
    }

    @Given("the XPath repository for {string} is loaded")
    public void loadRepository(String pageName) {
        Hooks.getTest().info("Clearing existing XPath repository");
        XPathRepository.clear();
        XPathRepository.loadPage(pageName);
        Hooks.getTest().pass("Loaded XPath repository for page: " + pageName);
    }

    @When("the user interacts with {string}")
    public void interactWithElement(String elementKey) {
        driver = DriverFactory.getDriver();
        Hooks.getTest().info("Retrieving locator for key: " + elementKey);
        Locator locator = XPathRepository.get(elementKey);
        assertThat(locator).as("Locator should exist for key: " + elementKey).isNotNull();
        Hooks.getTest().debug("Locator details: " + locator.toString());

        WebElement element = driver.findElement(By.xpath(locator.xpath));
        Hooks.getTest().info("Found element for key: " + elementKey);

        switch (locator.action.toLowerCase()) {
            case "click":
                element.click();
                Hooks.getTest().pass("Clicked element: " + elementKey);
                break;
            case "type":
                assertThat(locator.data).as("Missing data for typing into: " + elementKey).isNotNull();
                element.sendKeys(locator.data);
                Hooks.getTest().pass("Typed into element: " + elementKey + " with data: " + locator.data);
                break;
            case "assert":
                assertThat(element.isDisplayed()).as("Element should be visible: " + elementKey).isTrue();
                Hooks.getTest().pass("Asserted visibility of element: " + elementKey);
                break;
            default:
                Hooks.getTest().fail("Unsupported action: " + locator.action);
                throw new UnsupportedOperationException("Unsupported action: " + locator.action);
        }
    }

    @Then("the element {string} should be handled as expected")
    public void verifyElementHandled(String elementKey) {
        Hooks.getTest().info("Verifying repository contains key: " + elementKey);
        assertThat(XPathRepository.contains(elementKey))
            .as("Element key should exist in repository: " + elementKey)
            .isTrue();
        Hooks.getTest().pass("Verified repository contains key: " + elementKey);
    }
}