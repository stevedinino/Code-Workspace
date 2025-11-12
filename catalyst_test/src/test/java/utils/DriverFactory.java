package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Arrays;

public class DriverFactory {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        String browser = System.getProperty("browser", "firefox").toLowerCase(); // default to Firefox

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
                driver = new ChromeDriver(chromeOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-notifications");
                edgeOptions.setAcceptInsecureCerts(true);
                driver = new EdgeDriver(edgeOptions);
                break;

            case "firefox":
            default:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                firefoxOptions.setAcceptInsecureCerts(true);
                firefoxOptions.addArguments("--width=1920");
                firefoxOptions.addArguments("--height=1080");
                driver = new FirefoxDriver(firefoxOptions);
                break;
        }

        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}