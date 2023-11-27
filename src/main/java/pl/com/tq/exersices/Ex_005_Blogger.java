package pl.com.tq.exersices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import pl.com.tq.gpt.completions.Completions;
import pl.com.tq.gpt.completions.CompletionsItem;
import pl.com.tq.services.OpenAI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Ex_005_Blogger implements AIExercise<JsonNode> {
    //properties objectmapper and openai
    private ObjectMapper objectMapper;
    private OpenAI openAI;

    //constructor
    public Ex_005_Blogger(ObjectMapper objectMapper, OpenAI openAI) {
        this.objectMapper = objectMapper;
        this.openAI = openAI;
    }

    @Override
    public JsonNode run(JsonNode input, List<String> logs) {
        //get blog arraynode from input
        JsonNode blog = input.get("blog");
        logs.add("blog: " + blog.toPrettyString());

        //prepare arraynode for blogs
        JsonNode blogs = objectMapper.createArrayNode();
        //add arrraylist for string
        ArrayList<String> blogList = new ArrayList<>();

        //iterate over blog arraynode
        for (JsonNode blogItem : blog) {
            //get prompt from blogItem
            String prompt = blogItem.asText();
            //get completion from openAI
            JsonNode completion = openAI.completions(createBody(prompt));
            //add completion to blogs
            String answerText = completion.get("message").get("content").asText();
            blogList.add(answerText);
        }



        //prepade jsonnode as blogs
        //add properties answer
        //put blogs there
        ArrayNode arrayNode = objectMapper.createArrayNode();
        blogList.forEach(arrayNode::add);
        JsonNode answer = objectMapper.createObjectNode();
        ((ObjectNode) answer).put("answer", arrayNode);
        //add log using lombok
        log.info("answer: {}", answer.toPrettyString());
        logs.add("answer: " + answer.toPrettyString());
        //return answer
        return answer;
    }

    private JsonNode createBody(String prompt) {
        Completions user = Completions.builder()
                .model("gpt-4")
                .max_tokens(100)
                .messages(
                        Arrays.asList(
                                CompletionsItem.builder()
                                        .role("user")
                                        .content(prompt)
                                        .build(),
                                CompletionsItem.builder()
                                        .role("system")
                                        .content("as cook prepare blod , return only one sentence with 20 tokens, add prompt at the beginning, use Polish language model")
                                        .build()
                        )
                )
                .build();

        return objectMapper.valueToTree(user);
    }
}

/*
task: {
  "code" : 0,
  "msg" : "please write blog post for the provided outline",
  "blog" : [ "Wstęp: kilka słów na temat historii pizzy", "Niezbędne składniki na pizzę", "Robienie pizzy", "Pieczenie pizzy w piekarniku" ]
}
 */