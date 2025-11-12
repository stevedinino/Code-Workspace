package utils;

public class Locator {
    private String xpath;
    private String action;
    private String type;
    private String result;

    public String getXpath() {
        return xpath;
    }

    public String getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Locator{" +
                "xpath='" + xpath + '\'' +
                ", action='" + action + '\'' +
                ", type='" + type + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}