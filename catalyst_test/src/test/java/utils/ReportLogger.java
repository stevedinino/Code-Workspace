package utils;

import com.aventstack.extentreports.ExtentTest;
import hooks.Hooks;

public class ReportLogger {

    private static ExtentTest test() {
        return Hooks.getTest();
    }

    public static void info(String message) {
        test().info(message);
    }

    public static void pass(String message) {
        test().pass(message);
    }

    public static void fail(String message) {
        test().fail(message);
    }

    public static void warn(String message) {
        test().warning(message);
    }

    public static void skip(String message) {
        test().skip(message);
    }
}