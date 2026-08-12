package com.swapi.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private ConfigManager() {}

    //Lee el archivo swapi.properties
    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigManager.class.getResourceAsStream("/" + CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found on the classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read " + CONFIG_FILE, e);
        }
        return props;
    }

    /**
     * @return valor de system property
     */
    public static String get(String key) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override;
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing configuration key: " + key);
        }
        return value;
    }

    public static String getBaseUri() {
        return get("base.uri");
    }

    public static String getBasePath() {
        return get("base.path");
    }
}
