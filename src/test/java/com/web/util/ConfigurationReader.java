package com.web.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * reads the properties file configuration.properties
 */
public class ConfigurationReader {
    private static Logger LOG = LogManager.getLogger(ConfigurationReader.class);
    public static Config config;

    static {
        try {
            config = ConfigFactory.load("Configuration.properties");
            String env = config.getString("env");

            Config local = ConfigFactory.load(env + ".properties");

            config = ConfigFactory.systemProperties().withFallback(config).withFallback(local);
            LOG.debug("Running tests with the environment " + env);
        } catch (Exception e) {
            LOG.error("Failure to get configs: " + e);
            e.printStackTrace();
        }
    }

    public static String get(String keyName) {
        return config.getString(keyName);
    }

}