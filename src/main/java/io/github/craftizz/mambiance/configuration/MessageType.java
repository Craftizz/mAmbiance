package io.github.craftizz.mambiance.configuration;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public enum MessageType {

    SUCCESS_SOUND("success-sound"),
    DENY_SOUND("deny-sound");

    private final String configPath;
    private Sound sound;

    MessageType(final @NotNull String configPath) {
        this.configPath = configPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

}
