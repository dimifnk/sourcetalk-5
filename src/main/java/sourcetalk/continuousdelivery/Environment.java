package sourcetalk.continuousdelivery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Environment {
    private final String environment;
    private final Configuration configuration;

    private static final Environment INSTANCE = new Environment();

    private Environment() {
        environment = detectEnvironment();

        String configFile = "config/" + environment + ".properties";

        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(configFile)));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration for environment: " + environment);
        }

        configuration = new Configuration(properties);
    }

    public static String getName() {
        return INSTANCE.environment;
    }

    public static Configuration getConfiguration() {
        return INSTANCE.configuration;
    }

    private String detectEnvironment() {
        if (System.getenv("BUILD_NUMBER") != null) {
            return "ci";
        }

        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            if ("10.0.2.15".equals(ipAddress)) {
                return "prod";
            }
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unable to detect environment!", e);
        }

        if (classExists("org.junit.Assert")) {
            return "unittest";
        }

        return "local";
    }

    private static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
