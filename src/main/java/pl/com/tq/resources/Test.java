package pl.com.tq.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class Test {

    private final ObjectMapper objectMapper;

    @Autowired
    public Test(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "token/{name}", produces = "application/json")
    JsonNode token(@PathVariable String name) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("token", "xxxx-xxxx-xxx");
        return objectNode;
    }
    @GetMapping(value = "task/{token}", produces = "application/json")
    JsonNode task(@PathVariable String token) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("task", "xxxx-xxxx-xxx");
        return objectNode;
    }

    @PostMapping(value = "answer/{token}", produces = "application/json")
    void answer(@PathVariable String token, @RequestBody JsonNode body) {
        log.info("TEST: answer: " + body.toPrettyString());
    }
}
