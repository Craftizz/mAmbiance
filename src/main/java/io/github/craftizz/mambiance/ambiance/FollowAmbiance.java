package io.github.craftizz.mambiance.ambiance;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.types.AbstractSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SoundWrapper;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FollowAmbiance extends Ambiance {


    public FollowAmbiance(final @NotNull String regionId,
                          final @NotNull AbstractSound sound) {
        super(regionId, sound);
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
                player.playSound(soundWrapper.getSound(), Sound.Emitter.self());
            }

        }

        else {

            Bukkit.getLogger().info("Different Sound Playing");

            if (ambiancePlayer.getSoundStop() != null) {
                player.stopSound(ambiancePlayer.getSoundStop());
            }

            player.playSound(soundWrapper.getSound(), Sound.Emitter.self());
            ambiancePlayer.setSoundId(soundWrapper.getId());
            ambiancePlayer.setRepeatIn(soundWrapper.getDuration());
            ambiancePlayer.setSoundStop(soundWrapper.getSoundStop());

        }
    }

}
