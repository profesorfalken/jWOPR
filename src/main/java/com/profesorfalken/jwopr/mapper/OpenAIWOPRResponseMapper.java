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
package com.profesorfalken.jwopr.mapper;

import com.profesorfalken.jwopr.response.WOPRResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Component that perform the mapping needed to build a {{@link WOPRResponse}} object from different sources
 *
 * @author Javier Garcia Alonso
 */
public class OpenAIWOPRResponseMapper implements WOPRResponseMapper {
    //Pattern to retrieve the text value from OpenAI provider
    private static final Pattern EXTRACT_TEXT_PATTERN = Pattern.compile("\"text\":\"(.*?)\",\"index\":");

    //Pattern to retrieve the error message value from OpenAI provider
    private static final Pattern EXTRACT_ERROR_PATTERN = Pattern.compile("\"message\":.\"(.*?)\"");

    /**
     * Builds a {{@link WOPRResponse}} object from the API response
     *
     * @param connection the HTTP connection
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     * @throws IOException I/O exception
     */
    @Override
    public WOPRResponse map(HttpsURLConnection connection) throws IOException {
        WOPRResponse woprResponse = new WOPRResponse();
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getConnectionInputStream(connection)))) {
            br.lines().forEach(content::append);
        }

        if (isConnectionOK(connection)) {
            woprResponse.setText(parseTextFromJsonContent(content.toString()));
        } else {
            woprResponse.setError(parseErrorFromJsonContent(content.toString()));
        }

        return woprResponse;
    }


    /**
     * Builds a {{@link WOPRResponse}} object from an IOException
     *
     * @param ioException I/O exception
     * @return {{@link WOPRResponse}} object encapsulating the response
     */
    @Override
    public WOPRResponse map(IOException ioException) {
        WOPRResponse woprResponse = new WOPRResponse();

        woprResponse.setError(ioException.toString());

        return woprResponse;
    }

    //Retrieve text data from JSON using regex
    private static String parseTextFromJsonContent(String jsonContent) {
        return parseFromJsonContent(jsonContent, EXTRACT_TEXT_PATTERN);
    }

    //Retrieve error data from JSON using regex
    private static String parseErrorFromJsonContent(String jsonContent) {
        return parseFromJsonContent(jsonContent, EXTRACT_ERROR_PATTERN);
    }

    //Get a JSON element value from a given JSON and using a provided pattern
    private static String parseFromJsonContent(String jsonContent, Pattern pattern) {
        final Matcher matcher = pattern.matcher(jsonContent);
        String result = "";
        if (matcher.find()) {
            result = sanitize(matcher.group(1));
        }
        return result;
    }

    //Remove line breaks and slashes
    private static String sanitize(String content) {
        return content.replace("\\n", "").replace("\\", "");
    }

    //Get the stream of data sent by the IA
    private static InputStream getConnectionInputStream(HttpsURLConnection connection) throws IOException {
        return isConnectionOK(connection) ? connection.getInputStream() : connection.getErrorStream();
    }

    //Indicates if the call to the API was OK
    private static boolean isConnectionOK(HttpsURLConnection connection) throws IOException {
        return connection.getResponseCode() == 200;
    }
}
