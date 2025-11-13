package hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigLoader;
import utils.DriverFactory;

public class Hooks {

    public static ExtentTest test;
    public static ExtentReports extent;
    public static ExtentSparkReporter spark;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    @Before(order = 0)
    public void setupReport() {
        if (extent == null) {
            String reportPath = "test-output/ExtentReport.html";
            spark = new ExtentSparkReporter(reportPath);

            Theme theme = ConfigLoader.getTheme();
            spark.config().setTheme(theme);
            spark.config().setDocumentTitle("Catalyst Test Report");
            spark.config().setReportName("Catalyst UI Automation");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Theme", theme.name());
        }
    }

    @Before(order = 1)
    public void createTest(Scenario scenario) {
        ExtentTest test = extent.createTest(scenario.getName());
        testThread.set(test);
    }

    @After(order = 98)
    public void tearDownDriver() {
        DriverFactory.quitDriver();
    }

    @After(order = 99)
    public void tearDownReport(Scenario scenario) {
        ExtentTest test = getTest();
        if (test != null) {
            if (scenario.isFailed()) {
                test.fail("Scenario failed: " + scenario.getName());
            } else {
                test.pass("Scenario passed: " + scenario.getName());
            }
        }
        if (extent != null) {
            extent.flush();
        }
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }
}