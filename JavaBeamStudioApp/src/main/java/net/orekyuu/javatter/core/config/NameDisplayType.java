package net.orekyuu.javatter.core.config;

public enum NameDisplayType {
    NAME("Name"),
    ID("@ScreenName"),
    NAME_ID("Name / @ScreenName"),
    ID_NAME("@ScreenName / Name");

    private String configName;

    NameDisplayType(String configName) {
        this.configName = configName;
    }

    public String configName() {
        return configName;
    }
}
