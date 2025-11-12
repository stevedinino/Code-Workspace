package utils;

public class Locator {
    public String xpath;
    public String action;
    public String type;
    public String data; // nullable
    public String result; // 

    @Override
    public String toString() {
        return String.format("Locator[xpath='%s', action='%s', type='%s', result'%s', data='%s']",
                xpath, action, type, result, data);
    }
}
