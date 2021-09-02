package io.github.craftizz.mambiance.ambiance.types.wrapper;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;

public abstract class SoundWrapper {

    protected final int id;

    public abstract Sound getSound();
    public abstract SoundStop getSoundStop();
    public abstract int getDuration();

    protected SoundWrapper(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
