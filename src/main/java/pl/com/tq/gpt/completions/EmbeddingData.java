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
public class EmbeddingData {
    List<Float> embedding;
    Integer index;
    String object;
}
