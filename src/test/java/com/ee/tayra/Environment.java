package com.ee.tayra;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public enum Environment {
    DEV {
        @Override
        NamedParameters get() {
            if (parameters == null) {
                parameters = load("dev.properties");
            }
            return parameters;
        }

    },
    TEST {
        @Override
        NamedParameters get() {
            if (parameters == null) {
                parameters = load("test.properties");
            }
            return parameters;
        }
    };

    abstract NamedParameters get();

    NamedParameters parameters = null;


    NamedParameters load(final String propertiesFile) {
        NamedParameters params = new NamedParameters();
        Properties properties = new Properties();
        FileInputStream file = null;
        try {
            file = new FileInputStream(propertiesFile);
            properties.load(file);
            addTo(params, properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
              try {
                  file.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
            }
        }
        return params;
    }

    private void addTo(
      final NamedParameters params, final Properties props) {
        for (String propertyName : props.stringPropertyNames()) {
          params.add("{" + propertyName + "}", props.getProperty(propertyName));
        }
    }

    public static NamedParameters settings() {
        String env = System.getProperty("env", "dev").toUpperCase();
        return Environment.valueOf(env).get();
    }
}