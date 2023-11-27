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
import java.util.List;

@Slf4j
public class Ex_003_Inprompt implements AIExercise<JsonNode>{

    private final ObjectMapper objectMapper;
    private final OpenAI openAI;

    public Ex_003_Inprompt(ObjectMapper objectMapper, OpenAI openAI) {
        this.objectMapper = objectMapper;
        this.openAI = openAI;
    }

    @Override
    public JsonNode run(JsonNode input, List<String> logs) {

        List<String> facts = new ArrayList<>();
        String question = input.get("question").asText();
        ArrayNode arrayNode = (ArrayNode) input.get("input");

        arrayNode.forEach( item -> {
            String itemText = item.asText();
            String name = itemText.split(" ")[0];
            if( question.contains(name) ) {
                facts.add(itemText);
            }
        });

        JsonNode completions = compleationsGPT(facts, question);
        log.info("completions: " + completions.toPrettyString());

        String answerText = completions.get("message").get("content").asText();
        ObjectNode answer = objectMapper.createObjectNode().put("answer", answerText);
        log.info("Answer: " + answer.toPrettyString());
        return answer;
    }

    private JsonNode compleationsGPT(List<String> facts, String question) {

        List<CompletionsItem> items = new ArrayList<>();

        facts.stream().forEach( item -> {
            CompletionsItem system = CompletionsItem.builder()
                    .role("system")
                    .content(item).build();
            items.add(system);
        });

        CompletionsItem user = CompletionsItem.builder()
                .role("user")
                .content(question)
                .build();
        items.add(user);

        Completions completions = Completions.builder()
                .model("gpt-4")
                .max_tokens(100)
                .messages(items)
                .build();

        JsonNode body = objectMapper.valueToTree(completions);

        log.info("body: " + body.toPrettyString());

        JsonNode answer = openAI.completions(body);
        return answer;
    }
}

/*

task: {
  "code" : 0,
  "msg" : "remember information about each person and then answer the question",
  "input" : [ "Abdon ma czarne oczy, średniej długości włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść owsiankę", "Abel ma czarne oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Abelard ma zielone oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Abraham ma zielone oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Achilles ma czarne oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść kurczaka", "Adam ma szare oczy, średniej długości włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kurczaka", "Adelard ma szare oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Adnan ma czarne oczy, średniej długości włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kurczaka", "Adrian ma czarne oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Agapit ma czarne oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść parówki", "Agaton ma niebieskie oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Agrypin ma szare oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Ajdin ma czarne oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kurczaka", "Albert ma szare oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Alan ma brązowe oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kurczaka", "Albin ma niebieskie oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Albrecht ma zielone oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kurczaka", "Aleks ma szare oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Aleksander ma brązowe oczy, długie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść owsiankę", "Aleksy ma szare oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść owsiankę", "Alfons ma brązowe oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Alfred ma szare oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść parówki", "Alojzy ma czarne oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść owsiankę", "Alwin ma zielone oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść owsiankę", "Amadeusz ma zielone oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kurczaka", "Ambroży ma niebieskie oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść parówki", "Anastazy ma szare oczy, długie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kanapki z serem", "Ananiasz ma brązowe oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść parówki", "Anatol ma brązowe oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Andrzej ma czarne oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść owsiankę", "Anioł ma brązowe oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść parówki", "Annasz ma brązowe oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść kurczaka", "Antoni ma czarne oczy, średniej długości włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kurczaka", "Antonin ma szare oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść parówki", "Antonius ma brązowe oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Anzelm ma szare oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść kanapki z serem", "Apollo ma czarne oczy, średniej długości włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kanapki z serem", "Apoloniusz ma niebieskie oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kurczaka", "Ariel ma niebieskie oczy, średniej długości włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kanapki z serem", "Arkadiusz ma niebieskie oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kurczaka", "Arkady ma zielone oczy, średniej długości włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Arnold ma brązowe oczy, średniej długości włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść owsiankę", "Artur ma zielone oczy, krótkie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść owsiankę", "August ma zielone oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść owsiankę", "Augustyn ma szare oczy, długie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kurczaka", "Aurelian ma czarne oczy, długie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść kurczaka", "Baldwin ma czarne oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść owsiankę", "Baltazar ma brązowe oczy, długie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kurczaka", "Barabasz ma brązowe oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść parówki", "Barnim ma brązowe oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Bartłomiej ma czarne oczy, krótkie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kanapki z serem", "Bartosz ma czarne oczy, długie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kanapki z serem", "Bazyli ma niebieskie oczy, krótkie włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść owsiankę", "Beat ma czarne oczy, średniej długości włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Benedykt ma szare oczy, średniej długości włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść kurczaka", "Beniamin ma brązowe oczy, długie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Benon ma czarne oczy, średniej długości włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść parówki", "Bernard ma czarne oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść owsiankę", "Bert ma brązowe oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Błażej ma niebieskie oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kurczaka", "Bodosław ma czarne oczy, średniej długości włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Bogdał ma niebieskie oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kanapki z serem", "Bogdan ma czarne oczy, długie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Boguchwał ma zielone oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść parówki", "Bogumił ma czarne oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść owsiankę", "Bogumir ma brązowe oczy, średniej długości włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kurczaka", "Bogusław ma brązowe oczy, średniej długości włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Bogusz ma czarne oczy, długie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść kurczaka", "Bolebor ma niebieskie oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść kurczaka", "Bolelut ma brązowe oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Bolesław ma niebieskie oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść owsiankę", "Bonawentura ma brązowe oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Bonifacy ma szare oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Borys ma szare oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Borysław ma niebieskie oczy, długie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść kurczaka", "Borzywoj ma czarne oczy, krótkie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść owsiankę", "Bożan ma brązowe oczy, krótkie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść parówki", "Bożidar ma szare oczy, średniej długości włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść owsiankę", "Bożydar ma brązowe oczy, średniej długości włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Bożimir ma zielone oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść owsiankę", "Bromir ma szare oczy, krótkie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kurczaka", "Bronisław ma zielone oczy, długie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść kanapki z serem", "Bruno ma szare oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Brunon ma zielone oczy, krótkie włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść kurczaka", "Budzisław ma czarne oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kanapki z serem", "Cecyl ma niebieskie oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść kanapki z serem", "Cecylian ma niebieskie oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Celestyn ma szare oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść owsiankę", "Cezar ma szare oczy, krótkie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Cezary ma niebieskie oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Chociemir ma zielone oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść parówki", "Chrystian ma brązowe oczy, krótkie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Chwalibóg ma szare oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść parówki", "Chwalimir ma niebieskie oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść kurczaka", "Chwalisław ma szare oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść kanapki z serem", "Cichosław ma brązowe oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Ciechosław ma brązowe oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść parówki", "Cyprian ma szare oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść owsiankę", "Cyryl ma zielone oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść parówki", "Czesław ma zielone oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść owsiankę", "Dajmir ma czarne oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Dal ma zielone oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść parówki", "Dalbor ma czarne oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Damazy ma szare oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść kurczaka", "Damian ma niebieskie oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kanapki z serem", "Daniel ma brązowe oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kanapki z serem", "Danisław ma zielone oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść parówki", "Danko ma zielone oczy, krótkie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść owsiankę", "Dargomir ma zielone oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść parówki", "Dargosław ma czarne oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Dariusz ma niebieskie oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Darwit ma zielone oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Dawid ma brązowe oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Denis ma brązowe oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Derwit ma zielone oczy, długie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kurczaka", "Dionizy ma brązowe oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Dobiesław ma niebieskie oczy, długie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść owsiankę", "Dobrogost ma zielone oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść parówki", "Dobrosław ma brązowe oczy, krótkie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Domasław ma czarne oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Dominik ma szare oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kurczaka", "Donald ma niebieskie oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kanapki z serem", "Donat ma zielone oczy, krótkie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść owsiankę", "Dorian ma szare oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść parówki", "Duszan ma brązowe oczy, długie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść owsiankę", "Dymitr ma szare oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść kurczaka", "Dyter ma niebieskie oczy, krótkie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Dzwonimierz ma szare oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kanapki z serem", "Dżamil ma brązowe oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Dżan ma czarne oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść owsiankę", "Dżem ma szare oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Dżemil ma szare oczy, krótkie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść parówki", "Edgar ma szare oczy, długie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kurczaka", "Edmund ma czarne oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść parówki", "Edward ma zielone oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść parówki", "Edwin ma czarne oczy, średniej długości włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Efraim ma czarne oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Efrem ma niebieskie oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Eliasz ma brązowe oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Eligiusz ma zielone oczy, długie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kanapki z serem", "Eliot ma zielone oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kanapki z serem", "Emanuel ma czarne oczy, średniej długości włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Emil ma czarne oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Emir ma zielone oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść parówki", "Erazm ma szare oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść owsiankę", "Ernest ma brązowe oczy, krótkie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Erwin ma zielone oczy, krótkie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kurczaka", "Eugeniusz ma niebieskie oczy, długie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść parówki", "Eryk ma brązowe oczy, długie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść owsiankę", "Ewald ma czarne oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Ewaryst ma brązowe oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Ezaw ma niebieskie oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść owsiankę", "Ezechiel ma szare oczy, długie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść owsiankę", "Fabian ma niebieskie oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść parówki", "Farid ma szare oczy, długie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kurczaka", "Faris ma brązowe oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść parówki", "Faustyn ma brązowe oczy, krótkie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Felicjan ma brązowe oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kurczaka", "Feliks ma szare oczy, średniej długości włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Ferdynand ma niebieskie oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść parówki", "Filip ma szare oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Florentyn ma szare oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść parówki", "Florian ma czarne oczy, krótkie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść parówki", "Fortunat ma zielone oczy, średniej długości włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść owsiankę", "Franciszek ma zielone oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Fryc ma niebieskie oczy, krótkie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Fryderyk ma szare oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Gabriel ma czarne oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść owsiankę", "Gabor ma niebieskie oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść parówki", "Gaj ma niebieskie oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Gardomir ma niebieskie oczy, krótkie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Gaweł ma niebieskie oczy, krótkie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Gerard ma czarne oczy, średniej długości włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kanapki z serem", "Gerwazy ma czarne oczy, krótkie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kurczaka", "Gilbert ma szare oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Ginter ma brązowe oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kurczaka", "Gniewomir ma szare oczy, krótkie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kanapki z serem", "Gniewosz ma niebieskie oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Godfryg ma niebieskie oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Godfryd ma czarne oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść owsiankę", "Godzisław ma brązowe oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Gościsław ma czarne oczy, długie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść owsiankę", "Gracjan ma czarne oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść parówki", "Grodzisław ma szare oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść owsiankę", "Grzegorz ma brązowe oczy, długie włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść kanapki z serem", "Grzymisław ma czarne oczy, krótkie włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Gustaw ma niebieskie oczy, krótkie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść parówki", "Gwalbert ma niebieskie oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Gwido ma zielone oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kanapki z serem", "Gwidon ma niebieskie oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Hadrian ma brązowe oczy, długie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść parówki", "Hamza ma czarne oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść owsiankę", "Hanusz ma niebieskie oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść parówki", "Hasan ma brązowe oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kurczaka", "Hektor ma czarne oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść owsiankę", "Heliodor ma szare oczy, długie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść owsiankę", "Henryk ma zielone oczy, średniej długości włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kurczaka", "Herakles ma szare oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść owsiankę", "Herbert ma czarne oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść owsiankę", "Herman ma zielone oczy, długie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść parówki", "Hermes ma czarne oczy, krótkie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kurczaka", "Hiacynt ma niebieskie oczy, krótkie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Hieronim ma zielone oczy, krótkie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kurczaka", "Hilary ma niebieskie oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść owsiankę", "Hipolit ma czarne oczy, krótkie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść parówki", "Honorat ma brązowe oczy, średniej długości włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść kurczaka", "Horacy ma szare oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść owsiankę", "Hubert ma szare oczy, długie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść parówki", "Hugo ma brązowe oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kurczaka", "Hugon ma brązowe oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść owsiankę", "Husajn ma niebieskie oczy, długie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kanapki z serem", "Idzi ma szare oczy, średniej długości włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Ignacy ma czarne oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Igor ma szare oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Ildefons ma szare oczy, krótkie włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Inocenty ma zielone oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Ireneusz ma niebieskie oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść parówki", "Iwan ma brązowe oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Iwo ma brązowe oczy, średniej długości włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kurczaka", "Iwon ma szare oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść owsiankę", "Izajasz ma zielone oczy, długie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kurczaka", "Izydor ma niebieskie oczy, krótkie włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Jacek ma zielone oczy, średniej długości włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Jacenty ma niebieskie oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Jakub ma czarne oczy, krótkie włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść parówki", "Jan ma zielone oczy, długie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść owsiankę", "January ma szare oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Janusz ma czarne oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Jarad ma niebieskie oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść owsiankę", "Jaromir ma niebieskie oczy, długie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść parówki", "Jaropełk ma szare oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Jarosław ma brązowe oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Jarowit ma czarne oczy, długie włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Jeremiasz ma niebieskie oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kurczaka", "Jerzy ma brązowe oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść owsiankę", "Jędrzej ma czarne oczy, długie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kurczaka", "Joachim ma brązowe oczy, średniej długości włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść kurczaka", "Jona ma brązowe oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść owsiankę", "Jonasz ma czarne oczy, krótkie włosy i pracuje jako prawnik, a na śniadanie najbardziej lubi jeść kanapki z serem", "Jonatan ma zielone oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Jozafat ma niebieskie oczy, krótkie włosy i pracuje jako murarz, a na śniadanie najbardziej lubi jeść kurczaka", "Józef ma zielone oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Józefat ma szare oczy, średniej długości włosy i pracuje jako programista, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Julian ma szare oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Juliusz ma brązowe oczy, długie włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kurczaka", "Jur ma czarne oczy, krótkie włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść owsiankę", "Juri ma czarne oczy, średniej długości włosy i pracuje jako nauczyciel, a na śniadanie najbardziej lubi jeść kanapki z serem", "Justyn ma niebieskie oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kurczaka", "Justynian ma szare oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Jasuf ma zielone oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść kurczaka", "Kacper ma brązowe oczy, średniej długości włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść kurczaka", "Kain ma zielone oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść parówki", "Kajetan ma zielone oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Kajfasz ma czarne oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść parówki", "Kajusz ma brązowe oczy, długie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kanapki z serem", "Kamil ma brązowe oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść kanapki z serem", "Kanimir ma szare oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść parówki", "Karol ma brązowe oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść parówki", "Kasjusz ma zielone oczy, średniej długości włosy i pracuje jako grafik, a na śniadanie najbardziej lubi jeść owsiankę", "Kasper ma szare oczy, średniej długości włosy i pracuje jako muzyk, a na śniadanie najbardziej lubi jeść kanapki z serem", "Kastor ma niebieskie oczy, średniej długości włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść parówki", "Kazimierz ma niebieskie oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść owsiankę", "Kemal ma czarne oczy, długie włosy i pracuje jako lekarz, a na śniadanie najbardziej lubi jeść owsiankę", "Kilian ma niebieskie oczy, długie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kurczaka", "Klaudiusz ma niebieskie oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść owsiankę", "Klemens ma brązowe oczy, średniej długości włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść kanapki z serem", "Kochan ma szare oczy, krótkie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść parówki", "Kondrat ma czarne oczy, średniej długości włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść owsiankę", "Konrad ma niebieskie oczy, długie włosy i pracuje jako fryzjer, a na śniadanie najbardziej lubi jeść kanapki z serem", "Konradyn ma czarne oczy, krótkie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Konstancjusz ma brązowe oczy, krótkie włosy i pracuje jako kucharz, a na śniadanie najbardziej lubi jeść parówki", "Konstanty ma brązowe oczy, długie włosy i pracuje jako dziennikarz, a na śniadanie najbardziej lubi jeść bułkę z bananem", "Konstantyn ma szare oczy, długie włosy i pracuje jako kierowca, a na śniadanie najbardziej lubi jeść jogurt z owocami", "Kordian ma brązowe oczy, krótkie włosy i pracuje jako architekt, a na śniadanie najbardziej lubi jeść parówki" ],
  "question" : "co lubi jeść na śniadanie Alojzy?"
}


 */
