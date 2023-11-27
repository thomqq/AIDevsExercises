package pl.com.tq.exersices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.com.tq.services.OpenAI;

import java.util.List;

public class Ex_007_Whisper  implements AIExercise<String>{
    private final ObjectMapper objectMapper;
    private final OpenAI openAI;

    public Ex_007_Whisper(final ObjectMapper objectMapper, final OpenAI openAI) {
        this.objectMapper = objectMapper;
        this.openAI = openAI;
    }

    @Override
    public String run(final JsonNode input, final List<String> logs) {
        String filePath = "F:\\tomek\\AI_Devs2\\c02l04\\mateusz.mp3";
        String whisper = openAI.whisper(filePath);
        logs.add(filePath);
        return whisper;
    }
}
