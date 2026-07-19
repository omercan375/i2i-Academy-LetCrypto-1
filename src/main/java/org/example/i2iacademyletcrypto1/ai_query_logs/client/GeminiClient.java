package org.example.i2iacademyletcrypto1.ai_query_logs.client;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.ai_query_logs.LlmClient;
import org.example.i2iacademyletcrypto1.config.GeminiProperties;
import org.example.i2iacademyletcrypto1.zcommon.exception.AiServiceException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiClient implements LlmClient {

    private final Client client;
    private final GeminiProperties properties;

    @Override
    public String generateResponse(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            throw new AiServiceException("Prompt cannot be empty");
        }

        try {
            GenerateContentResponse response =
                    client.models.generateContent(
                            properties.model(),
                            prompt,
                            null
                    );

            if (response == null) {
                throw new AiServiceException(
                        "Gemini returned no response"
                );
            }

            String responseText = response.text();

            if (responseText == null || responseText.isBlank()) {
                throw new AiServiceException(
                        "Gemini returned an empty response"
                );
            }

            return responseText;

        } catch (AiServiceException exception) {
            throw exception;

        } catch (Exception exception) {
            exception.printStackTrace();

            throw new AiServiceException(
                    "Gemini request failed: "
                            + exception.getClass().getSimpleName()
                            + " - "
                            + exception.getMessage()
            );
        }
    }
}