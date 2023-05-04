package pl.com.tq.gpt.completions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Completions
{
    private String model;
    private Integer max_tokens;
    private List<CompletionsItem> messages;

}
