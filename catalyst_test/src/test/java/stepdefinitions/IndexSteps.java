package stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.DriverFactory;
import utils.XPathRepository;
import utils.Locator;
import utils.ReportLogger;
import utils.ConfigLoader;

public class IndexSteps {

    WebDriver driver;

    @Given("the user launches Catalyst web site")
    public void launchCatalystSite() {
        driver = DriverFactory.getDriver();
        String baseUrl = ConfigLoader.get("base.url");
        assertThat(baseUrl).as("Base URL must be defined").isNotNull();
        ReportLogger.info("Launching Catalyst site at: " + baseUrl);
        driver.get(baseUrl);
        ReportLogger.pass("Catalyst site loaded successfully");
    }

    @Given("the XPath repository for {string} is loaded")
    public void loadRepository(String pageName) {
        ReportLogger.info("Clearing existing XPath repository");
        XPathRepository.clear();
        XPathRepository.loadPage(pageName);
        ReportLogger.pass("Loaded XPath repository for page: " + pageName);
    }

    @When("the user interacts with {string}")
    public void interactWithElement(String elementKey) {
        driver = DriverFactory.getDriver();
        Locator locator = XPathRepository.get(elementKey);
        assertThat(locator).as("Locator should exist for key: " + elementKey).isNotNull();
        ReportLogger.info("Interacting with element: " + elementKey + " (type: " + locator.type + ", action: " + locator.action + ")");

        WebElement element = driver.findElement(By.xpath(locator.xpath));
        assertThat(element).as("Element must be found for key: " + elementKey).isNotNull();

        switch (locator.type.toLowerCase()) {
            case "text":
            case "image":
            case "list":
                if (locator.action.equalsIgnoreCase("assert")) {
                    assertThat(element.isDisplayed()).isTrue();
                    ReportLogger.pass("Asserted visibility of " + locator.type + ": " + elementKey);
                } else {
                    throw new UnsupportedOperationException("Cannot '" + locator.action + "' on type '" + locator.type + "'");
                }
                break;

            case "link":
            case "button":
                if (locator.action.equalsIgnoreCase("click")) {
                    element.click();
                    ReportLogger.pass("Clicked " + locator.type + ": " + elementKey);
                } else {
                    throw new UnsupportedOperationException("Unsupported action '" + locator.action + "' for type '" + locator.type + "'");
                }
                break;

            case "modal":
                if (locator.action.equalsIgnoreCase("assert")) {
                    assertThat(element.isDisplayed()).isTrue();
                    ReportLogger.pass("Modal is visible: " + elementKey);
                } else if (locator.action.equalsIgnoreCase("click")) {
                    element.click();
                    ReportLogger.pass("Clicked modal trigger: " + elementKey);
                } else {
                    throw new UnsupportedOperationException("Unsupported action '" + locator.action + "' for modal");
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown element type: " + locator.type);
        }
    }

    @Then("the result of {string} should be validated")
    public void validateResult(String elementKey) {
        driver = DriverFactory.getDriver();
        Locator locator = XPathRepository.get(elementKey);
        assertThat(locator).as("Locator should exist for key: " + elementKey).isNotNull();
        assertThat(locator.result).as("Result must be defined for key: " + elementKey).isNotEmpty();

        String result = locator.result.trim();
        ReportLogger.info("Validating result for key: " + elementKey + " → " + result);

        try {
            if (result.startsWith("url contains")) {
                String expectedFragment = result.replace("url contains", "").replace("'", "").trim();
                assertThat(driver.getCurrentUrl()).contains(expectedFragment);
                ReportLogger.pass("URL contains expected fragment: " + expectedFragment);
            } else if (result.equals("element is visible")) {
                WebElement element = driver.findElement(By.xpath(locator.xpath));
                assertThat(element.isDisplayed()).isTrue();
                ReportLogger.pass("Element is visible: " + elementKey);
            } else if (result.equals("image is visible")) {
                WebElement image = driver.findElement(By.xpath(locator.xpath));
                assertThat(image.isDisplayed()).isTrue();
                assertThat(image.getTagName()).isEqualTo("img");
                ReportLogger.pass("Image is visible: " + elementKey);
            } else if (result.equals("modal is visible")) {
                WebElement modal = driver.findElement(By.xpath(locator.xpath));
                assertThat(modal.isDisplayed()).isTrue();
                ReportLogger.pass("Modal is visible: " + elementKey);
            } else if (result.equals("modal is dismissed")) {
                WebElement modal = driver.findElement(By.xpath(locator.xpath));
                assertThat(modal.isDisplayed()).isFalse();
                ReportLogger.pass("Modal is dismissed: " + elementKey);
            } else if (result.equals("element is visible with list items")) {
                WebElement list = driver.findElement(By.xpath(locator.xpath));
                assertThat(list.isDisplayed()).isTrue();
                assertThat(list.findElements(By.tagName("li"))).isNotEmpty();
                ReportLogger.pass("List is visible with items: " + elementKey);
            } else if (result.startsWith("element with class")) {
                String className = result.replace("element with class", "").replace("'", "").trim();
                WebElement target = driver.findElement(By.className(className));
                assertThat(target.isDisplayed()).isTrue();
                ReportLogger.pass("Element with class '" + className + "' is visible");
            } else {
                ReportLogger.warn("No validator implemented for result: " + result);
            }
        } catch (Exception e) {
            ReportLogger.fail("Validation failed for key: " + elementKey + " → " + result);
            throw e;
        }
    }
}