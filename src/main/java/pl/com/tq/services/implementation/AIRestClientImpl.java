package pl.com.tq.services.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.com.tq.services.AIRestClient;

public class AIRestClientImpl implements AIRestClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${pl.com.tq.aidev.resturl}")
    private String url;

    @Value("${pl.com.tq.aidev.apikey}")
    private String apiKey;

    @Autowired
    public AIRestClientImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getToken(String taskName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectNode body = objectMapper.createObjectNode();
        body.put("apikey", apiKey);

        HttpEntity<JsonNode> request = new HttpEntity<JsonNode>(body, headers);

        JsonNode response = restTemplate.postForObject(url + "token/" + taskName, request, JsonNode.class);
        return response.get("token").asText();
    }

    @Override
    public JsonNode getTask(String token) {

        JsonNode response = restTemplate.getForObject(url + "task/" + token, JsonNode.class);
        return response;
    }

    @Override
    public void sendAnswer(JsonNode result, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JsonNode> request = new HttpEntity<JsonNode>(result, headers);

        JsonNode response = restTemplate.postForObject(url + "answer/" + token, request, JsonNode.class);
    }
}
