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

import com.profesorfalken.jwopr.mapper.WOPRResponseMapper;
import com.profesorfalken.jwopr.response.WOPRResponse;

import java.util.Map;

/**
 * Interface for the AI provider
 *
 * @author Javier Garcia Alonso
 */
public interface AIProvider {

    /**
     * Ask something to the AI
     *
     * @param prompt the message to send to the AI
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    WOPRResponse ask(String prompt);

    /**
     * Computes the payload that will be sent to AI provider
     *
     * @param prompt the message to send to the AI
     * @return the computed payload
     */
    String computeAskPayload(String prompt);

    /**
     * Sets a custom configuration for the current provider
     *
     * @param configuration {{@link Map}} with the configuration field to override
     */
    void setConfiguration(Map<String, String> configuration);

    /**
     * Get the mapper used to produce the WOPR response
     *
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    WOPRResponseMapper getMapper();

}
