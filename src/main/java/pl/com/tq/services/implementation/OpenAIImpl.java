package pl.com.tq.services.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.com.tq.gpt.completions.Embedding;
import pl.com.tq.gpt.completions.EmbeddingData;
import pl.com.tq.services.OpenAI;

import java.nio.charset.Charset;
import java.util.Arrays;

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
        //add content code Utf-8
        headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));


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

    @Override
    public Embedding embeddings(String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        //add content code Utf-8
        headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));


        ObjectNode body = objectMapper.createObjectNode();
        body.put("input", text);
        body.put("model", "text-embedding-ada-002");
        log.info("embeddings: input: " + body.toPrettyString());

        HttpEntity<JsonNode> request = new HttpEntity<>(body, headers);

        JsonNode response = restTemplate.postForObject(url + "v1/embeddings", request, JsonNode.class);
        log.info("embeddings: result: " + response.toPrettyString());
        JsonNode data = response.get("data");


        Embedding embedding = objectMapper.convertValue(response, Embedding.class);

        return embedding;
    }

    @Override
    public String whisper(final String pathToMp3File) {
        log.info("whisper: pathToMp3File: " + pathToMp3File);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(apiKey);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", getTestFile(pathToMp3File));
        body.add("model", "whisper-1");
        body.add("response_format", "text");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<JsonNode> response = restTemplate
//                .postForEntity(url + "v1/audio/transcriptions", requestEntity, JsonNode.class);


        String response = restTemplate.postForObject(url + "v1/audio/transcriptions", requestEntity, String.class);
        log.info("whisper: result: " + response);
        //JsonNode data = response.get("data");

        return response;
    }

    private FileSystemResource getTestFile(String pathToMp3File) {
        return new FileSystemResource(pathToMp3File);
    }
}
