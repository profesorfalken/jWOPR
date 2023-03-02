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

import java.util.Collections;
import java.util.Map;

/**
 * Client that allows to communicate with the IA API
 *
 * @author Javier Garcia Alonso
 */
public interface ConnectionClient {

    /**
     * Send a payload via a POST request
     *
     * @param endpoint the endpoint to call
     * @param payload  the payload
     * @param mapper   the mapper used to generate WOPR response
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    default WOPRResponse post(String endpoint, String payload, WOPRResponseMapper mapper) {
        return post(endpoint, payload, mapper, Collections.emptyMap());
    }

    /**
     * Send a payload via a POST request
     *
     * @param endpoint the endpoint to call
     * @param payload  the payload
     * @param mapper   the mapper used to generate WOPR response
     * @param headers  {{@link Map}} that contains some HTTP headers to add/override
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    WOPRResponse post(String endpoint, String payload, WOPRResponseMapper mapper, Map<String, String> headers);
}
