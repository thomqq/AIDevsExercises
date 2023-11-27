package pl.com.tq.exersices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pl.com.tq.gpt.completions.Embedding;
import pl.com.tq.services.OpenAI;

import java.util.List;

public class Ex_006_Embedding implements AIExercise<JsonNode> {


    private final ObjectMapper objectMapper;
    private final OpenAI openAI;

    public Ex_006_Embedding(final ObjectMapper objectMapper, final OpenAI openAI) {
        this.objectMapper = objectMapper;
        this.openAI = openAI;
    }

    @Override
    public JsonNode run(final JsonNode input, final List<String> logs) {

        Embedding hawaiianPizza = openAI.embeddings("Hawaiian pizza");
        List<Float> embedding = hawaiianPizza.getData().get(0).getEmbedding();
        JsonNode result = objectMapper.valueToTree(embedding);
        ObjectNode answer = objectMapper.createObjectNode().set("answer", result);
        return answer;
    }
}
