package io.github.craftizz.mambiance.ambiance;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.types.AbstractSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SoundWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoordinateAmbiance extends Ambiance {

    private final int x;
    private final int y;
    private final int z;

    public CoordinateAmbiance(final @NotNull String regionId,
                              final @NotNull AbstractSound sound,
                              final int x,
                              final int y,
                              final int z) {
        super(regionId, sound);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void playSound(final @NotNull Player player,
                          final @NotNull AmbiancePlayer ambiancePlayer) {

        final SoundWrapper soundWrapper = sound.getSound(ambiancePlayer);
        Bukkit.getLogger().severe("(" + soundWrapper.getId() + ") Sound Name: " + soundWrapper.getSound().name());

        if (ambiancePlayer.isTheSame(soundWrapper.getId())) {

            Bukkit.getLogger().warning("The same sound");

            if (ambiancePlayer.shouldRepeat()) {

                Bukkit.getLogger().severe("Repeating Sound");

                ambiancePlayer.setRepeatIn(soundWrapper.getDuration());
                player.playSound(soundWrapper.getSound(), x, y, z);
            }

        }

        else {

            Bukkit.getLogger().info("Different Sound Playing");

            if (ambiancePlayer.getSoundStop() != null) {
                player.stopSound(ambiancePlayer.getSoundStop());
            }

            player.playSound(soundWrapper.getSound(), x, y, z);
            ambiancePlayer.setSoundId(soundWrapper.getId());
            ambiancePlayer.setRepeatIn(soundWrapper.getDuration());
            ambiancePlayer.setSoundStop(soundWrapper.getSoundStop());

        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
