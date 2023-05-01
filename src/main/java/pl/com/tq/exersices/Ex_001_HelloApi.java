package pl.com.tq.exersices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Ex_001_HelloApi implements AIExercise{

    private final ObjectMapper objectMapper;

    public Ex_001_HelloApi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public JsonNode run(JsonNode input) {
        return objectMapper.createObjectNode().put("answer", input.get("cookie").asText());
    }
}
