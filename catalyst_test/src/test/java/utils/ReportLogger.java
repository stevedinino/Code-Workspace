package utils;

import com.aventstack.extentreports.Status;
import hooks.Hooks;

public class ReportLogger {

    public static void info(String message) {
        Hooks.getTest().log(Status.INFO, message);
    }

    public static void warn(String message) {
        Hooks.getTest().log(Status.WARNING, message);
    }

    public static void error(String message) {
        Hooks.getTest().log(Status.FAIL, message);
    }

    public static void pass(String message) {
        Hooks.getTest().log(Status.PASS, message);
    }
}