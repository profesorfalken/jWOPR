package com.profesorfalken.jwopr.aiprovider.integration;

import com.profesorfalken.jwopr.configuration.ConfigurationReader;

public class BadConfigChatGptProvider extends BrokenChatGptProvider {
    public BadConfigChatGptProvider() {
        this.configuration = ConfigurationReader.getChatGptConfiguration("BadName");
    }
}
