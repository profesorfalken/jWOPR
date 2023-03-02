package com.profesorfalken.jwopr.aiprovider.integration;

import com.profesorfalken.jwopr.configuration.ConfigurationReader;

public class MissingPropertiesChatGptProvider extends BrokenChatGptProvider {
    public MissingPropertiesChatGptProvider() {
        this.configuration = ConfigurationReader.getChatGptConfiguration("jwopr_configuration_missing.properties");
    }
}
