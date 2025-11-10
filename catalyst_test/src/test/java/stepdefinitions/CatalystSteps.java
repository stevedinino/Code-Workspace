package stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.IndexPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import utils.DriverFactory;

public class CatalystSteps {

    WebDriver driver;
    IndexPage indexpage;

@Before
public void setUp() {
    driver = DriverFactory.getDriver();
    indexpage = new IndexPage(driver);
}

@After
public void tearDown() {
    DriverFactory.quitDriver();
}


    @Given("the user launches the Catalyst Legal Nurse website")
    public void launchCatalystSite() {
        driver.get("https://www.catalystlegalnurse.com");
    }

    @Then("the page title should contain {string}")
    public void verifyTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
         assertThat(actualTitle)
            .as("Check that the page title contains expected text")
            .contains(expectedTitle);
    }

    @And("the Explore Services button should be visible")
    public void verifyButtonVisible() {
        assertThat(indexpage.exploreServicesCTA().isDisplayed())
            .as("Check that the Explore Services button is visible")
            .isTrue();
    }
    @Then("the About link should navigate to the About page")
    public void verifyAboutLink() {
        indexpage.aboutLink().click();
        assertThat(driver.getCurrentUrl())
            .as("Verify About page URL")
            .contains("/about");
    }

    @Then("the Services link should navigate to the Services page")
    public void verifyServicesLink() {
        indexpage.servicesLink().click();
        assertThat(driver.getCurrentUrl())
            .as("Verify Services page URL")
            .contains("/services");
    }

@Then("the Testimonials link should navigate to the Testimonials page")
    public void verifyTestimonials() {
        indexpage.testimonialsLink().click();
        assertThat(driver.getCurrentUrl())
            .as("Verify Testimonials page URL")
            .contains("/testimonials");
    }

    @Then("the Upload link should navigate to the Upload page")
    public void verifyUpload() {
        indexpage.uploadLink().click();
        assertThat(driver.getCurrentUrl())
            .as("Verify Upload page URL")
            .contains("/upload");
    }

    @Then("the Contact link should navigate to the Contact page")
    public void verifyContactLink() {
        indexpage.contactLink().click();
        assertThat(driver.getCurrentUrl())
            .as("Verify Contact page URL")
            .contains("/contact");
    }

    @Then("the Privacy link should navigate to the Privacy page")
    public void verifyPrivacy() {
        indexpage.privacyLink().click();
        assertThat(driver.getCurrentUrl())
            .as("Verify Privacy page URL")
            .contains("/privacy");
    }

        @Then("the FAQ link should navigate to the FAQ page")
    public void verifyFAQ() {
        indexpage.faqLink().click();
        assertThat(driver.getCurrentUrl())
            .as("Verify FAQ page URL")
            .contains("/faq");
    }
}