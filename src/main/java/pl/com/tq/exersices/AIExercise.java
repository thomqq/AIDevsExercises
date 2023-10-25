package pl.com.tq.exersices;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface AIExercise {
    JsonNode run(JsonNode input, List<String> logs);
}
