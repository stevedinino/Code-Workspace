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

    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (threadDriver.get() == null) {
            threadDriver.set(createDriver());
        }
        return threadDriver.get();
    }

    private static WebDriver createDriver() {
        String browser = ConfigLoader.get("browser").toLowerCase();
        System.out.println("[INFO] Launching browser: " + browser);

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
                return new ChromeDriver(chromeOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (Boolean.parseBoolean(ConfigLoader.get("edge.start.maximized")))
                    edgeOptions.addArguments("--start-maximized");
                if (Boolean.parseBoolean(ConfigLoader.get("edge.disable.notifications")))
                    edgeOptions.addArguments("--disable-notifications");
                if (Boolean.parseBoolean(ConfigLoader.get("accept.insecure.certs")))
                    edgeOptions.setAcceptInsecureCerts(true);
                return new EdgeDriver(edgeOptions);

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
                return new FirefoxDriver(firefoxOptions);
        }
    }

    public static void quitDriver() {
        if (threadDriver.get() != null) {
            System.out.println("[INFO] Quitting browser instance");
            threadDriver.get().quit();
            threadDriver.remove();
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            quitDriver();
        }));
    }
}