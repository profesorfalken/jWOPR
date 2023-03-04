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
package com.profesorfalken.jwopr.aiprovider;

import com.profesorfalken.jwopr.configuration.ConfigurationReader;
import com.profesorfalken.jwopr.connection.ConnectionClient;
import com.profesorfalken.jwopr.connection.ConnectionUtil;
import com.profesorfalken.jwopr.mapper.OpenAIWOPRResponseMapper;
import com.profesorfalken.jwopr.mapper.WOPRResponseMapper;
import com.profesorfalken.jwopr.response.WOPRResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * AI provider based in OpenAI API.
 *
 * @author Javier Garcia Alonso
 */
public class OpenAIProvider implements AIProvider {

    //The URL to the OpenAI completions API
    private static final String OPEN_API_COMPLETION_URL = "https://api.openai.com/v1/completions";

    //Template for a completion payload
    private static final String ASK_PAYLOAD = "{\"model\": \"{model}\",\"prompt\": \"{prompt}\",\"temperature\": {temperature},\"max_tokens\": {maxTokens}}";

    //Reference to the client used to send the payload
    protected ConnectionClient connectionClient;

    //The configuration of the provider
    protected Map<String, String> configuration;

    //Constructor
    public OpenAIProvider(ConnectionClient connectionClient) {
        this.connectionClient = connectionClient;

        this.configuration = ConfigurationReader.getOpenAIConfiguration();
    }

    /**
     * Ask something to the AI
     *
     * @param prompt the message to send to the AI
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    @Override
    public WOPRResponse ask(String prompt) {
        //Set auth key in Authorization header
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", ConnectionUtil.createBearerAuthHeaderValue(getAuthToken()));

        return this.connectionClient.post(OPEN_API_COMPLETION_URL, computeAskPayload(prompt), getMapper(), headers);
    }

    /**
     * Computes the payload that will be sent to AI provider
     *
     * @param prompt the message to send to the AI
     * @return the computed payload
     */
    @Override
    public String computeAskPayload(String prompt) {
        return ASK_PAYLOAD.replaceAll("\\{prompt\\}", prompt)
                .replaceAll("\\{model\\}", this.configuration.get("model"))
                .replaceAll("\\{temperature\\}", this.configuration.get("temperature"))
                .replaceAll("\\{maxTokens\\}", this.configuration.get("maxTokens"));
    }

    /**
     * Sets a custom configuration for the current provider
     *
     * @param configuration {{@link Map}} with the configuration field to override
     */
    @Override
    public void setConfiguration(Map<String, String> configuration) {
        this.configuration.putAll(configuration);
    }

    /**
     * Get the mapper used to produce the WOPR response
     *
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    @Override
    public WOPRResponseMapper getMapper() {
        return new OpenAIWOPRResponseMapper();
    }

    //Get the authorization token for Chat GPT API session
    private String getAuthToken() {
        String authToken = System.getenv("OPENAI_API_KEY");

        //Override by configuration
        if (this.configuration.get("authToken") != null) {
            authToken = this.configuration.get("authToken");
        }

        return authToken;
    }
}
