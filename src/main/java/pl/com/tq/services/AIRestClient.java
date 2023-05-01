package pl.com.tq.services;

import com.fasterxml.jackson.databind.JsonNode;

public interface AIRestClient {
    String getToken(String taskName);
    public JsonNode getTask(String token);
    void sendAnswer(JsonNode result, String token);
}
