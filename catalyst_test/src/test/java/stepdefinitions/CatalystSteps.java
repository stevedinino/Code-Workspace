package stepdefinitions;

import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

//WebDriverManager.chromedriver().setup(); // Automatically resolves and sets up the driver
//driver = new ChromeDriver();


public class CatalystSteps {
    WebDriver driver = new FirefoxDriver();

@Given("I print {string}")
public void i_print(String message) {
    System.out.println(message);
}


   @Given("the user launches the Catalyst Legal Nurse website")
    public void launchCatalystSite() {
        WebDriverManager.firefoxdriver().setup();
        driver.get("https://www.catalystlegalnurse.com");
    }

    @Then("the page title should contain {string}")
    public void verifyTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
        System.out.println("Actual page title: " + actualTitle);
        assertTrue(actualTitle.contains(expectedTitle));
        driver.quit();
    }
}