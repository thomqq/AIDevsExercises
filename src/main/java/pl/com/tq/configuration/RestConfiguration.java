package pl.com.tq.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.com.tq.common.ExerciseList;
import pl.com.tq.exersices.*;
import pl.com.tq.services.AIRestClient;
import pl.com.tq.services.OpenAI;
import pl.com.tq.services.implementation.AIRestClientImpl;
import pl.com.tq.services.implementation.OpenAIImpl;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "pl.com.tq")
public class RestConfiguration {
    @Bean
    RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    AIRestClient getAIRestClient() {
        AIRestClient aiRestClient = new AIRestClientImpl(getRestTemplate(), getObjectMapper());
        return aiRestClient;
    }

    @Bean
    OpenAI getOpenAI() {
        OpenAI openAI = new OpenAIImpl(getObjectMapper(), getRestTemplate());
        return openAI;
    }

    @Bean
    ExerciseList getExercisesList() {

        return () -> {
            Map<String, AIExercise> result = new LinkedHashMap<>();
            result.put("helloapi", new Ex_001_HelloApi(getObjectMapper()));
            result.put("moderation", new Ex_003_Moderation(getObjectMapper(), getOpenAI()));
            result.put("inprompt", new Ex_003_Inprompt(getObjectMapper(), getOpenAI()));
            result.put("blogger", new Ex_005_Blogger(getObjectMapper(), getOpenAI()));


            return result;
        };
    }
}
