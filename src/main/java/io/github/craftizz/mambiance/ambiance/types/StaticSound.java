package io.github.craftizz.mambiance.ambiance.types;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SingleSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SoundWrapper;
import org.jetbrains.annotations.NotNull;

public class StaticSound extends AbstractSound {

    private final SingleSound singleSound;

    public StaticSound(final @NotNull SingleSound singleSound) {
        this.singleSound = singleSound;
    }

    @Override
    public SoundWrapper getSound(@NotNull AmbiancePlayer ambiancePlayer) {
        return singleSound;
    }


}
