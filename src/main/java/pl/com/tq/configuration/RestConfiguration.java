package pl.com.tq.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.com.tq.common.ExerciseList;
import pl.com.tq.exersices.AIExercise;
import pl.com.tq.exersices.Ex_001_HelloApi;
import pl.com.tq.services.AIRestClient;
import pl.com.tq.services.implementation.AIRestClientImpl;

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
    ExerciseList getExercisesList() {

        return new ExerciseList() {
            @Override
            public Map<String, AIExercise> getExercises() {
                Map<String, AIExercise> result = new LinkedHashMap<>();
                result.put("helloapi", new Ex_001_HelloApi(getObjectMapper()));
                return result;
            }
        };
    }
}
