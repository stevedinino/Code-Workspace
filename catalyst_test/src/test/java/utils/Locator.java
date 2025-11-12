package utils;

public class Locator {
    public String xpath;
    public String action;
    public String type;
    public String data; // nullable

    @Override
    public String toString() {
        return String.format("Locator[xpath='%s', action='%s', type='%s', data='%s']",
                xpath, action, type, data);
    }
}
