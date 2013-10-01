package sourcetalk.continuousdelivery;

import java.util.Properties;

public class Configuration {
    private final Properties properties;

    public Configuration(Properties properties) {
        this.properties = properties;
    }

    public String getTitleColor() {
        return properties.getProperty("title.color");
    }

    public int getPortHttp() {
        return Integer.valueOf(properties.getProperty("port.http"));
    }

    public int getPortShutdown() {
        return Integer.valueOf(properties.getProperty("port.shutdown"));
    }

    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public boolean isDbLoadTestData() {
        return Boolean.parseBoolean(properties.getProperty("db.loadTestData"));
    }

    public boolean isFeatureFriendCound() {
        return Boolean.parseBoolean(properties.getProperty("feature.friendCount"));
    }
}
