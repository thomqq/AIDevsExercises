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
public class Embedding {
    List<EmbeddingData> data;
    String model;
    String object;
    EmbeddingUsage usage;
}
