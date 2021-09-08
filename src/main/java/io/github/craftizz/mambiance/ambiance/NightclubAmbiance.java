package io.github.craftizz.mambiance.ambiance;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.types.AbstractSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SoundWrapper;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NightclubAmbiance {

    private final String songId;
    private final AbstractSound sound;

    private final int x;
    private final int y;
    private final int z;

    private int votes;

    public NightclubAmbiance(final @NotNull String songId,
                             final @NotNull AbstractSound sound,
                             int x,
                             int y,
                             int z) {
        this.songId = songId;
        this.sound = sound;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void addVote() {
        votes++;
    }

    public int getVotes() {
        return votes;
    }

    public void playSound(final @NotNull Player player,
                          final @NotNull AmbiancePlayer ambiancePlayer) {

        final SoundWrapper soundWrapper = sound.getSound(ambiancePlayer);

        if (ambiancePlayer.isTheSame(soundWrapper.getId())) {
            if (ambiancePlayer.shouldRepeat()) {
                ambiancePlayer.setRepeatIn(soundWrapper.getDuration());
                player.playSound(soundWrapper.getSound(), x, y, z);
            }
        }

        else {

            if (ambiancePlayer.getSoundStop() != null) {
                ambiancePlayer.stopSound(player);
            }

            player.playSound(soundWrapper.getSound(), x, y, z);
            ambiancePlayer.setSoundId(soundWrapper.getId());
            ambiancePlayer.setRepeatIn(soundWrapper.getDuration());
            ambiancePlayer.setSoundStop(soundWrapper.getSoundStop());

        }

    }

    public void resetVotes() {
        this.votes = 0;
    }

    public String getSongId() {
        return songId;
    }

    public AbstractSound getSound() {
        return sound;
    }
}
