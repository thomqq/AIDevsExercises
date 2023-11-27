package pl.com.tq.gpt.completions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmbeddingUsage {
    Integer prompt_tokens;
    Integer total_tokens;
}
