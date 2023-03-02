package com.profesorfalken.jwopr.aiprovider.integration;

import com.profesorfalken.jwopr.aiprovider.AIProvider;
import com.profesorfalken.jwopr.configuration.ConfigurationReader;
import com.profesorfalken.jwopr.connection.BareJavaConnectionClient;
import com.profesorfalken.jwopr.connection.ConnectionClient;
import com.profesorfalken.jwopr.connection.ConnectionUtil;
import com.profesorfalken.jwopr.mapper.ChatGptWOPRResponseMapper;
import com.profesorfalken.jwopr.mapper.WOPRResponseMapper;
import com.profesorfalken.jwopr.response.WOPRResponse;

import java.util.HashMap;
import java.util.Map;

public class BrokenChatGptProvider implements AIProvider {
    private static String OPEN_API_COMPLETION_URL = "https://api.openai.com/v1/completions";
    private ConnectionClient connectionClient = new BareJavaConnectionClient();

    protected Map<String, String> configuration;

    public BrokenChatGptProvider() {
        this.configuration = ConfigurationReader.getChatGptConfiguration();
    }


    @Override
    public WOPRResponse ask(String prompt) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization",
                ConnectionUtil.createBearerAuthHeaderValue(this.configuration.get("authToken")));
        return this.connectionClient.post(OPEN_API_COMPLETION_URL, computeAskPayload(computeAskPayload(prompt)), getMapper(), headers);
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
