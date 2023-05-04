package pl.com.tq.services.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.com.tq.services.OpenAI;

@Slf4j
@Service
public class OpenAIImpl implements OpenAI {

    @Value("${pl.com.tq.openai.resturl}")
    private String url;

    @Value("${pl.com.tq.openai.apikey}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public OpenAIImpl(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public JsonNode moderation(String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        ObjectNode body = objectMapper.createObjectNode();
        body.put("input", text);
        log.info("moderation: input: " + body.toPrettyString());

        HttpEntity<JsonNode> request = new HttpEntity<>(body, headers);

        JsonNode response = restTemplate.postForObject(url + "v1/moderations", request, JsonNode.class);
        if( response == null ) {
            return null;
        }
        log.info("moderation: result: " + response.toPrettyString());
        return response.get("results").get(0);
    }

    @Override
    public JsonNode completions(JsonNode body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        HttpEntity<JsonNode> request = new HttpEntity<>(body, headers);

        JsonNode response = restTemplate.postForObject(url + "v1/chat/completions", request, JsonNode.class);

        if( response == null ) {
            log.info("completions: null");
            return null;
        }
        log.info("completions: response: " + response.toPrettyString());
        //TODO - add specific
        return response.get("choices").get(0);
    }
}
