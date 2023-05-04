package pl.com.tq.gpt.moderation;

import java.util.HashMap;
import java.util.Map;

public enum GptModerationCategories {
    none("none"),
    hate("hate"),
    hate_threatening("hate/threatening"),
    self_harm("elf-harm"),
    sexual("sexual"),
    sexual_minors("sexual/minors"),
    violence("violence"),
    violence_graphic("violence/graphic")
    ;

    final Map<String, GptModerationCategories> testToEnum = new HashMap<>();
    final Map<GptModerationCategories, String> enumToText = new HashMap<>();

    GptModerationCategories(String key) {
        testToEnum.put(key, this);
        enumToText.put(this, key);
    }

    public GptModerationCategories getCategories(String key) {
        return testToEnum.get(key);
    }
    public String getCategories( GptModerationCategories categoriesEnum) {
        return enumToText.get(categoriesEnum);
    }

}
