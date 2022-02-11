package com.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppProperties {

    public static String getVersion() throws IOException {
        Properties properties = new Properties();
        InputStream resourceAsStream = AppProperties.class.getResourceAsStream("/app.properties");
        properties.load(resourceAsStream);
        return properties.getProperty("version");
    }

}
