package com.profesorfalken.jwopr.aiprovider.integration;

import com.profesorfalken.jwopr.configuration.ConfigurationReader;

public class BadConfigOpenAIProvider extends BrokenOpenAIProvider {
    public BadConfigOpenAIProvider() {
        this.configuration = ConfigurationReader.getOpenAIConfiguration("BadName");
    }
}
