package io.github.craftizz.mambiance.ambiance.types.wrapper;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.jetbrains.annotations.NotNull;

public class TickSound extends SoundWrapper {

    private final Sound sound;
    private final SoundStop soundStop;
    private final int duration;
    private final int worldTick;

    public TickSound(final @NotNull Sound sound,
                     final int duration,
                     final int worldTick,
                     final int id) {
        super(id);
        this.sound = sound;
        this.soundStop = sound.asStop();
        this.duration = duration;
        this.worldTick = worldTick;
    }

    @Override
    public Sound getSound() {
        return sound;
    }

    @Override
    public SoundStop getSoundStop() {
        return soundStop;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public int getWorldTick() {
        return worldTick;
    }
}
