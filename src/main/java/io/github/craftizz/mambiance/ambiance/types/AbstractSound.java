package io.github.craftizz.mambiance.ambiance.types;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SoundWrapper;

public abstract class AbstractSound {

    public abstract SoundWrapper getSound(final AmbiancePlayer ambiancePlayer);

}
