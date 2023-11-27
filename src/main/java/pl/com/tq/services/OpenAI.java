package pl.com.tq.services;

import com.fasterxml.jackson.databind.JsonNode;
import pl.com.tq.gpt.completions.Embedding;
import pl.com.tq.gpt.completions.EmbeddingData;

public interface OpenAI {
    JsonNode moderation(String text);

    JsonNode completions(JsonNode body);

    Embedding embeddings(String text);

    String whisper(String pathToMp3File);
}
