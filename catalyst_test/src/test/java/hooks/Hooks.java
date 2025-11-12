package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import utils.ConfigLoader;
import utils.DriverFactory;

public class Hooks {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> scenarioThread = new ThreadLocal<>();

    @BeforeAll
    public static void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
        spark.config().setDocumentTitle(ConfigLoader.get("report.title"));
        spark.config().setReportName(ConfigLoader.get("report.name"));
        spark.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Author", "Steve");
        extent.setSystemInfo("Suite", "Catalyst Legal Nurse UI Tests");
        extent.setSystemInfo("Browser", "Firefox");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Base URL", ConfigLoader.get("base.url"));
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        DriverFactory.getDriver(); // Initializes WebDriver
        ExtentTest test = extent.createTest(scenario.getName());
        scenarioThread.set(test);
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            scenarioThread.get().fail("Scenario failed: " + scenario.getName());
        } else {
            scenarioThread.get().pass("Scenario passed");
        }
        DriverFactory.quitDriver(); // Clean up WebDriver
    }

    @AfterAll
    public static void tearDownReport() {
        extent.flush();
    }

    public static ExtentTest getTest() {
        return scenarioThread.get();
    }
}