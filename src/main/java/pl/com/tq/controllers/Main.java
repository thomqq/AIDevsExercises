package pl.com.tq.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.com.tq.common.ExerciseList;
import pl.com.tq.exersices.AIExercise;
import pl.com.tq.services.AIRestClient;

import java.util.*;

@Slf4j
@Controller
public class Main {

    private final AIRestClient aiRestClient;

    private final ExerciseList exercises;// = new LinkedHashMap<>();

    public Main(AIRestClient aiRestClient, ExerciseList exercises) {
        this.aiRestClient = aiRestClient;

        this.exercises = exercises;
    }

//    public void addExercise(String name, AIExercise exercise ) {
//        exercises.put(name, exercise);
//    }

    public List<String> getTasksList() {
        List<String> result = new ArrayList<>();
        exercises.getExercises().forEach( (name, exercise) ->  result.add(name) );
        return result;
    }


    @RequestMapping("/")
    public String main() {
        return "main";
    }

    @RequestMapping(value = "action", method = RequestMethod.POST)
    public String performAction(@RequestParam Map<String,String> allRequestParams) {
        String taskName = allRequestParams.get("task");
        if( taskName == null ) {
            throw new RuntimeException("No action");
        }

        AIExercise exercise = exercises.getExercises().get(taskName);
        if( exercise == null ) {
            throw new RuntimeException("No exercise for task: " + taskName);
        }

        String token = aiRestClient.getToken(taskName);
        log.info("token: " + token);
        JsonNode task = aiRestClient.getTask(token);
        log.info("task: " + task.toPrettyString());

        JsonNode result = exercise.run(task);

        aiRestClient.sendAnswer(result, token);
        return "main";
    }
}
