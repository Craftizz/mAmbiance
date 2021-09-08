package io.github.craftizz.mambiance.configuration;

import org.jetbrains.annotations.NotNull;

public enum Language {

    ENABLED_AMBIANCE("ambiance-enabled"),
    DISABLED_AMBIANCE("disabled-ambiance"),
    ALREADY_ENABLED("already-enabled"),
    ALREADY_DISABLED("already-disabled"),
    ALREADY_VOTED("already-voted"),
    NOT_SONG("not-song"),
    VOTE_SUCCESS("vote-success");

    private final String configPath;
    private String message;

    Language(final @NotNull String configPath) {
        this.configPath = configPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
