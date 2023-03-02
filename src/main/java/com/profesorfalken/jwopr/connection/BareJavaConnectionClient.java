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
package com.profesorfalken.jwopr.connection;

import com.profesorfalken.jwopr.mapper.WOPRResponseMapper;
import com.profesorfalken.jwopr.response.WOPRResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Client implementation based in low level Java network libraries
 *
 * @author Javier Garcia Alonso
 */
public class BareJavaConnectionClient implements ConnectionClient {

    /**
     * Send a payload via a POST request
     *
     * @param endpoint the endpoint to call
     * @param payload  the payload
     * @param headers  {{@link Map}} that contains some HTTP headers to add/override
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    @Override
    public WOPRResponse post(String endpoint, String payload, WOPRResponseMapper mapper, Map<String, String> headers) {
        HttpsURLConnection connection = null;

        try {
            connection = createConnection(endpoint);
            setPostHeaders(connection, headers);
            sendPayload(connection, payload);

            return mapper.map(connection);
        } catch (IOException ioe) {
            return mapper.map(ioe);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    //Gets the HttpsURLConnection
    private HttpsURLConnection createConnection(String endpoint) throws IOException {
        URL url = new URL(endpoint);

        return (HttpsURLConnection) url.openConnection();
    }

    //Generate all the HTTP headers to send for the POST request
    private void setPostHeaders(HttpsURLConnection connection, Map<String, String> headers) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        if (headers != null) {
            headers.forEach(connection::setRequestProperty);
        }
    }

    //Send the payload data using the opened connection
    private void sendPayload(HttpsURLConnection connection, String payload) throws IOException {
        connection.setDoOutput(true);
        try (OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
            osw.write(payload);
            osw.flush();
        }
    }
}
