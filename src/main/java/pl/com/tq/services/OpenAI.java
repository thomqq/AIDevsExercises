package pl.com.tq.services;

import com.fasterxml.jackson.databind.JsonNode;

public interface OpenAI {
    JsonNode moderation(String text);

    JsonNode completions(JsonNode body);
}
