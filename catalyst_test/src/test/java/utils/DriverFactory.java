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
        String browser = ConfigLoader.get("browser").toLowerCase(); // from config.properties

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (Boolean.parseBoolean(ConfigLoader.get("chrome.start.maximized")))
                    chromeOptions.addArguments("--start-maximized");
                if (Boolean.parseBoolean(ConfigLoader.get("chrome.disable.infobars")))
                    chromeOptions.addArguments("--disable-infobars");
                if (Boolean.parseBoolean(ConfigLoader.get("chrome.disable.notifications")))
                    chromeOptions.addArguments("--disable-notifications");
                if (Boolean.parseBoolean(ConfigLoader.get("accept.insecure.certs")))
                    chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.setExperimentalOption("excludeSwitches",
                        Arrays.asList(ConfigLoader.get("chrome.exclude.switches")));
                driver = new ChromeDriver(chromeOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (Boolean.parseBoolean(ConfigLoader.get("edge.start.maximized")))
                    edgeOptions.addArguments("--start-maximized");
                if (Boolean.parseBoolean(ConfigLoader.get("edge.disable.notifications")))
                    edgeOptions.addArguments("--disable-notifications");
                if (Boolean.parseBoolean(ConfigLoader.get("accept.insecure.certs")))
                    edgeOptions.setAcceptInsecureCerts(true);
                driver = new EdgeDriver(edgeOptions);
                break;

            case "firefox":
            default:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (Boolean.parseBoolean(ConfigLoader.get("firefox.headless")))
                    firefoxOptions.addArguments("--headless");
                if (Boolean.parseBoolean(ConfigLoader.get("accept.insecure.certs")))
                    firefoxOptions.setAcceptInsecureCerts(true);
                firefoxOptions.addArguments("--width=" + ConfigLoader.get("firefox.window.width"));
                firefoxOptions.addArguments("--height=" + ConfigLoader.get("firefox.window.height"));
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