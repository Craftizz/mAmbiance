package io.github.craftizz.mambiance;

import net.kyori.adventure.sound.SoundStop;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AmbiancePlayer {

    private final UUID uniqueId;

    private SoundStop soundStop;
    private long tick;
    private int soundId;
    private int repeatIn;

    public AmbiancePlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean shouldRepeat() {
        return repeatIn-- < 0;
    }

    public boolean isTheSame(final int soundId) {
        return this.soundId == soundId;
    }

    public void setSoundStop(SoundStop soundStop) {
        this.soundStop = soundStop;
    }

    public void stopSound(final @NotNull Player player) {
        if (soundStop != null) {

            player.stopSound(soundStop);

            this.soundStop = null;
        }
    }

    public SoundStop getSoundStop() {
        return soundStop;
    }

    public int getRepeatIn() {
        return repeatIn;
    }

    public void setRepeatIn(int repeatIn) {
        this.repeatIn = repeatIn;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }
}
