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
package com.profesorfalken.jwopr;

import com.profesorfalken.jwopr.aiprovider.AIProvider;
import com.profesorfalken.jwopr.aiprovider.OpenAIProvider;
import com.profesorfalken.jwopr.connection.BareJavaConnectionClient;
import com.profesorfalken.jwopr.response.WOPRResponse;

import java.util.Map;

/**
 * This library allows to interact with an AI provider.<p></p>
 * <p>
 * The current implementation allows to interact with the following AI engines:
 * <ul>
 * <li>OpenAI</li>
 * </ul>
 *
 * @author Javier Garcia Alonso
 */
public class WOPR {
    //The AI provider
    private AIProvider aiProvider;

    //Hide constructor in order to force a static instantiation
    private WOPR() {
    }

    /**
     * Creates an instance of WOPR
     *
     * @return a {{@link WOPR}} instance
     */
    public static WOPR get() {
        return new WOPR();
    }

    /**
     * Sets a specific AI provider
     *
     * @param aiProvider an instance of the implementation of the AI provider
     * @return a {{@link WOPR}} instance
     */
    public WOPR withProvider(AIProvider aiProvider) {
        this.aiProvider = aiProvider;

        return this;
    }

    /**
     * Sets a specific AI configuration
     *
     * @param configuration a {{@link Map}} that contains the configuration keys that we want to set/override
     * @return a {{@link WOPR}} instance
     */
    public WOPR withConfiguration(Map<String, String> configuration) {
        this.getProvider().setConfiguration(configuration);

        return this;
    }

    /**
     * Just ask anything to the AI
     *
     * @param prompt the message to send to the AI
     * @return {{@link WOPRResponse}} object encapsulating the response of the AI
     */
    public WOPRResponse ask(String prompt) {
        return this.getProvider().ask(prompt);
    }

    //Retrieves the AI provider (OpenAI by default)
    private AIProvider getProvider() {
        if (this.aiProvider == null) {
            this.aiProvider = new OpenAIProvider(new BareJavaConnectionClient());
        }
        return this.aiProvider;
    }

}
