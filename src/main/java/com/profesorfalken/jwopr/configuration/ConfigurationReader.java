/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.profesorfalken.jwopr.configuration;

import com.profesorfalken.jwopr.exception.WOPRException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Default provider configuration reader
 *
 * @author Javier Garcia Alonso
 */
public class ConfigurationReader {
    //Path of default configuration file
    private static final String JWOPR_CONFIG_FILE = "jwopr_configuration.properties";

    //Error message if file cannot be loaded
    private static final String LOAD_ERROR_MSG = String.format("Cannot load jWOPR base configuration file: [%s]", JWOPR_CONFIG_FILE);

    /**
     * Reads the configuration parameters for the OpenAI provider
     *
     * @return {{@link Map}} with the default parameters for OpenAI API
     */
    public static Map<String, String> getOpenAIConfiguration() {
        return getOpenAIConfiguration(null);
    }

    /**
     * Reads the configuration parameters for the OpenAI provider
     *
     * @param configFileName custom name of the configuration file
     * @return {{@link Map}} with the default parameters for OpenAI API
     */
    public static Map<String, String> getOpenAIConfiguration(String configFileName) {
        Map<String, String> baseConfiguration = new HashMap<>();
        String configFile = configFileName != null ? configFileName : JWOPR_CONFIG_FILE;

        try (InputStream input = ConfigurationReader.class.getClassLoader().getResourceAsStream(configFile)) {
            Properties prop = new Properties();

            if (input == null) {
                throw new WOPRException(LOAD_ERROR_MSG);
            }

            prop.load(input);

            baseConfiguration.put("authToken", prop.getProperty("openai.authToken"));
            baseConfiguration.put("model", prop.getProperty("openai.model"));
            baseConfiguration.put("temperature", prop.getProperty("openai.temperature"));
            baseConfiguration.put("maxTokens", prop.getProperty("openai.maxTokens"));

        } catch (IOException e) {
            throw new WOPRException(LOAD_ERROR_MSG, e);
        }

        return baseConfiguration;
    }
}
