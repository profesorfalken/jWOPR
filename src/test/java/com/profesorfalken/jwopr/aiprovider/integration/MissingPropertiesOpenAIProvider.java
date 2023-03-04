package com.profesorfalken.jwopr.aiprovider.integration;

import com.profesorfalken.jwopr.configuration.ConfigurationReader;

public class MissingPropertiesOpenAIProvider extends BrokenOpenAIProvider {
    public MissingPropertiesOpenAIProvider() {
        this.configuration = ConfigurationReader.getOpenAIConfiguration("jwopr_configuration_missing.properties");
    }
}
