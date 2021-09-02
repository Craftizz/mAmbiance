package io.github.craftizz.mambiance.ambiance;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.types.AbstractSound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Ambiance {

    protected final String regionId;
    protected final AbstractSound sound;

    protected Ambiance(final @NotNull String regionId,
                       final @NotNull AbstractSound sound) {
        this.regionId = regionId;
        this.sound = sound;
    }

    public abstract void playSound(final Player player,
                                   final AmbiancePlayer ambiancePlayer);

    public String getRegionId() {
        return regionId;
    }

    public @NotNull AbstractSound getSound() {
        return sound;
    }
}
