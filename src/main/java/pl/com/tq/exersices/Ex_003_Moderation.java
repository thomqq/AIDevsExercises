package pl.com.tq.exersices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import pl.com.tq.services.OpenAI;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Ex_003_Moderation implements AIExercise{

    private final ObjectMapper objectMapper;
    private final OpenAI openAI;

    public Ex_003_Moderation(ObjectMapper objectMapper, OpenAI openAI) {
        this.objectMapper = objectMapper;
        this.openAI = openAI;
    }

    @Override
    public JsonNode run(JsonNode input) {
        List<Integer> output = new ArrayList<>();
        ArrayNode arrInput = (ArrayNode) input.get("input");
        arrInput.forEach(item -> {
            boolean flagged = openAI.moderation(item.asText()).get("flagged").asBoolean();
            output.add(flagged ? 1 : 0);

        });

        ArrayNode arrayNode = objectMapper.createArrayNode();
        output.forEach(arrayNode::add);


        ObjectNode answer = objectMapper.createObjectNode().set("answer", arrayNode);
        log.info("Answer: " + answer.toPrettyString());
        return answer;
    }
}


/*

task: {
  "code" : 0,
  "msg" : "please return 0/1 for each sentence to classify if this is content that requires moderation",
  "input" : [ "azjaci są głupi i brzydcy i nie powinni żyć", "ten gość musi zginąć. Nie pozwole sobię na obrażanie mnie.", "majonez Winiary jest lepszy od Kieleckiego", "Sasha.Grey.s3x.S03E12.DVDRip.mp4" ]
}

 */