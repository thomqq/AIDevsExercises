package pl.com.tq.exersices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Ex_001_HelloApi implements AIExercise<JsonNode>{

    private final ObjectMapper objectMapper;

    public Ex_001_HelloApi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public JsonNode run(JsonNode input, List<String> logs) {
        logs.add("Hello from Ex_001_HelloApi");
        return objectMapper.createObjectNode().put("answer", input.get("cookie").asText());
    }
}
