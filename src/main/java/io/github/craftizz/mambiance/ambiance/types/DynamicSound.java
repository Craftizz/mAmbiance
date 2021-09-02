package io.github.craftizz.mambiance.ambiance.types;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SoundWrapper;
import io.github.craftizz.mambiance.ambiance.types.wrapper.TickSound;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DynamicSound extends AbstractSound {

    private final TickSound[] sounds;

    public DynamicSound(final @NotNull List<TickSound> sounds) {
        this.sounds = sounds.toArray(TickSound[]::new);
    }

    @Override
    public SoundWrapper getSound(@NotNull AmbiancePlayer ambiancePlayer) {
        return getSoundByTime(ambiancePlayer.getTick());
    }

    public SoundWrapper getSoundByTime(final long tick) {

        if (tick <= sounds[0].getWorldTick()) {
            return sounds[0];
        }

        if (tick >= sounds[sounds.length - 1].getWorldTick()) {
            return sounds[sounds.length - 1];
        }

        int low = 0;
        int high = sounds.length - 1;

        while (low <= high) {

            int middle = (high + low) / 2;

            if (tick < sounds[middle].getWorldTick()) {
                high = middle - 1;
            } else if (tick > sounds[middle].getWorldTick()) {
                low = middle + 1;
            } else {
                return sounds[middle];
            }
        }

        return (sounds[low].getWorldTick() - tick) < (tick - sounds[high].getWorldTick()) ? sounds[low] : sounds[high];
    }
}
