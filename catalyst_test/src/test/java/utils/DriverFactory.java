package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = ConfigLoader.getBrowser();
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if ("true".equalsIgnoreCase(ConfigLoader.get("chrome.headless"))) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    driver.set(new ChromeDriver(chromeOptions));
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    if ("true".equalsIgnoreCase(ConfigLoader.get("firefox.headless"))) {
                        firefoxOptions.addArguments("--headless");
                    }

                    String width = ConfigLoader.get("firefox.window.width");
                    String height = ConfigLoader.get("firefox.window.height");
                    if (width != null && height != null) {
                        firefoxOptions.addArguments("--width=" + width);
                        firefoxOptions.addArguments("--height=" + height);
                    }

                    driver.set(new FirefoxDriver(firefoxOptions));
                    break;

                default:
                    throw new RuntimeException("❌ Unsupported browser: " + browser);
            }
        }
        return driver.get();
    }

    public static void quitDriver() {
        try {
            WebDriver currentDriver = driver.get();
            if (currentDriver != null) {
                currentDriver.quit();
                ReportLogger.info("🧹 WebDriver quit successfully.");
            }
        } catch (Exception e) {
            ReportLogger.error("❌ Error during WebDriver quit: " + e.getMessage());
        } finally {
            driver.remove();
        }
    }
}