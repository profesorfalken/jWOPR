package com.profesorfalken.jwopr.aiprovider.integration;

import com.profesorfalken.jwopr.aiprovider.ChatGptProvider;
import com.profesorfalken.jwopr.connection.BareJavaConnectionClient;
import com.profesorfalken.jwopr.mapper.ChatGptWOPRResponseMapper;
import com.profesorfalken.jwopr.mapper.WOPRResponseMapper;

import java.util.Map;

public class BrokenChatGptProvider extends ChatGptProvider {

    public BrokenChatGptProvider() {
        super(new BareJavaConnectionClient());
    }

    @Override
    public String computeAskPayload(String prompt) {
        return prompt;
    }

    @Override
    public void setConfiguration(Map<String, String> configuration) {
        this.configuration.putAll(configuration);
    }

    @Override
    public WOPRResponseMapper getMapper() {
        return new ChatGptWOPRResponseMapper();
    }
}
