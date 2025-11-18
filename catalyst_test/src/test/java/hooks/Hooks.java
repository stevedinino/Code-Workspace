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
import utils.ReportLogger;

public class Hooks {

    private static ExtentReports extent;
    private static ExtentSparkReporter spark;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    @Before(order = 0)
    public void setupReport() {
        if (extent == null) {
            String reportPath = "test-output/ExtentReport.html";
            spark = new ExtentSparkReporter(reportPath);

            Theme theme = Theme.valueOf(ConfigLoader.getReportTheme().toUpperCase());

            spark.config().setTheme(theme);
            spark.config().setDocumentTitle("Catalyst Test Report");
            spark.config().setReportName("Catalyst UI Automation");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Theme", theme.name());
        }
    }

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {
        ExtentTest test = extent.createTest(scenario.getName());
        testThread.set(test);
        ReportLogger.info("🎨 Report theme loaded from config: " + ConfigLoader.getReportTheme());
    }

    @After(order = 1)
    public void afterScenario() {
        DriverFactory.quitDriver();
        ReportLogger.info("🧹 WebDriver session closed after scenario.");
        extent.flush();
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }
}