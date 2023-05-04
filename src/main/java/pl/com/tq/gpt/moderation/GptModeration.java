package pl.com.tq.gpt.moderation;

public interface GptModeration {
    GptModerationCategories checkPrommt(String text);
}
