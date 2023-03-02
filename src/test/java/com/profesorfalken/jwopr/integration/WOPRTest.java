package com.profesorfalken.jwopr.integration;

import com.profesorfalken.jwopr.WOPR;
import com.profesorfalken.jwopr.aiprovider.integration.BadConfigChatGptProvider;
import com.profesorfalken.jwopr.aiprovider.integration.BrokenChatGptProvider;
import com.profesorfalken.jwopr.aiprovider.integration.MissingPropertiesChatGptProvider;
import com.profesorfalken.jwopr.exception.WOPRException;
import com.profesorfalken.jwopr.response.WOPRResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WOPRTest {
    WOPR wopr = WOPR.get();

    @Test
    public void testAsk() {
        Assertions.assertEquals("This is a test.", wopr.ask("Say 'This is a test.'").getText());
    }

    @Test
    public void testBadRequest() {
        WOPR brokenWopr = WOPR.get().withProvider(new BrokenChatGptProvider());

        WOPRResponse woprResponse = brokenWopr.ask("Say 'This is a test.'");

        String expectedErrorMessage = "We could not parse the JSON body of your request. (HINT: This likely means you aren't using your HTTP library correctly. The OpenAI API expects a JSON payload, but what was sent was not valid JSON. If you have trouble figuring out how to fix this, please send an email to support@openai.com and include any relevant code you'd like help with.)";
        Assertions.assertNull(woprResponse.getText());
        Assertions.assertEquals(expectedErrorMessage, brokenWopr.ask("Say 'This is a test.'").getError());
    }

    @Test
    public void testNoApiKey() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("authToken", "");
        WOPR brokenWopr = WOPR.get().withProvider(new BrokenChatGptProvider()).withConfiguration(configuration);

        WOPRResponse woprResponse = brokenWopr.ask("Say 'This is a test.'");

        String expectedErrorMessage = "You didn't provide an API key. You need to provide your API key in an Authorization header using Bearer auth (i.e. Authorization: Bearer YOUR_KEY), or as the password field (with blank username) if you're accessing the API from your browser and are prompted for a username and password. You can obtain an API key from https://platform.openai.com/account/api-keys.";
        Assertions.assertNull(woprResponse.getText());
        Assertions.assertEquals(expectedErrorMessage, brokenWopr.ask("Say 'This is a test.'").getError());
    }

    @Test
    public void testConfigFileNotFound() {
        WOPR brokenWopr = WOPR.get();

        Exception e = assertThrows(WOPRException.class, () -> {
            brokenWopr.withProvider(new BadConfigChatGptProvider()).ask("Say 'This is a test.'");
        });

        assertEquals("Cannot load jWOPR base configuration file: [jwopr_configuration.properties]", e.getMessage());
    }

    @Test
    public void testMissingConfigProperty() {
        WOPRResponse woprResponse = WOPR.get().withProvider(new MissingPropertiesChatGptProvider()).ask("Say 'This is a test.'");

        String expectedErrorMessage = "Incorrect API key provided: null. You can find your API key at https://platform.openai.com/account/api-keys.";
        Assertions.assertNull(woprResponse.getText());
        Assertions.assertEquals(expectedErrorMessage, woprResponse.getError());
    }

    @Test
    public void testExtractJson() {
        final Pattern p = Pattern.compile("\"text\":\"(.*?)\",\"index\":");
        final Matcher m = p.matcher("{\"id\":\"cmpl-6nl03zkN83bljL2NcWzRtx5tIrhPa\",\"object\":\"text_completion\",\"created\":1677316759,\"model\":\"text-davinci-003\",\"choices\":[{\"text\":\"\\n\\nThis is a test.\",\"index\":0,\"logprobs\":null,\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":7,\"completion_tokens\":7,\"total_tokens\":14}}");
        String result = "";
        if (m.find()) {
            result = m.group(1);
        }
        Assertions.assertEquals("This is a test.", result.replace("\\n", ""));
    }

    @Test
    public void testExtractErrorJson() {
        final Pattern p = Pattern.compile("\"message\":.\"(.*?)\"");
        final Matcher m = p.matcher("{    \"error\": {        \"message\": \"We could not parse the JSON body of your request. (HINT: This likely means you aren't using your HTTP library correctly. The OpenAI API expects a JSON payload, but what was sent was not valid JSON. If you have trouble figuring out how to fix this, please send an email to support@openai.com and include any relevant code you'd like help with.)\",        \"type\": \"invalid_request_error\",        \"param\": null,        \"code\": null    }}");
        String result = "";
        if (m.find()) {
            result = m.group(1);
        }
        String expectedErrorMessage = "We could not parse the JSON body of your request. (HINT: This likely means you aren't using your HTTP library correctly. The OpenAI API expects a JSON payload, but what was sent was not valid JSON. If you have trouble figuring out how to fix this, please send an email to support@openai.com and include any relevant code you'd like help with.)";
        Assertions.assertEquals(expectedErrorMessage, result);
    }
}