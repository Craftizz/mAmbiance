package io.github.craftizz.mambiance.managers;

import io.github.craftizz.mambiance.ambiance.NightclubAmbiance;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NightClubManager {

    private final List<NightclubAmbiance> nightclubAmbiances;
    private final List<UUID> alreadyVoted;

    private NightclubAmbiance currentMusic;
    private String nightclubRegion;
    private int interval;
    private int timeLeft;

    public NightClubManager() {
        this.nightclubAmbiances = new ArrayList<>();
        this.alreadyVoted = new ArrayList<>();
    }

    public void shouldPlayNextMusic() {
        if (timeLeft-- < 0) {
            nextWinningMusic();
            timeLeft = interval;
        }

    }

    public boolean isNightClub(final @NotNull String regionId) {
        return regionId.equals(nightclubRegion);
    }

    public void nextWinningMusic() {

        NightclubAmbiance nightclubAmbiance = null;

        for (final NightclubAmbiance ambiance : nightclubAmbiances) {

            if (nightclubAmbiance == null) {
                nightclubAmbiance = ambiance;
            }

            else if (nightclubAmbiance.getVotes() < ambiance.getVotes()) {
                nightclubAmbiance = ambiance;
            }

            nightclubAmbiance.resetVotes();
        }

        alreadyVoted.clear();
        currentMusic = nightclubAmbiance;
    }

    public void addMusic(final NightclubAmbiance nightclubAmbiance) {
        nightclubAmbiances.add(nightclubAmbiance);
    }

    public void addVote(final @NotNull UUID uniqueId,
                        final @NotNull String musicId) {
        for (final NightclubAmbiance ambiance : nightclubAmbiances) {
            if (ambiance.getSongId().equalsIgnoreCase(musicId)) {
                alreadyVoted.add(uniqueId);
                ambiance.addVote();
                return;
            }
        }
    }

    public int getVote(final @NotNull String musicId) {
        for (final NightclubAmbiance ambiance : nightclubAmbiances) {
            if (ambiance.getSongId().equalsIgnoreCase(musicId)) {
                return ambiance.getVotes();
            }
        }
        return 0;
    }

    public NightclubAmbiance getCurrentMusic() {
        return currentMusic;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getNightclubRegion() {
        return nightclubRegion;
    }

    public void setNightclubRegion(String nightclubRegion) {
        this.nightclubRegion = nightclubRegion;
    }

    public List<NightclubAmbiance> getNightclubAmbiances() {
        return nightclubAmbiances;
    }

    public boolean isMusic(final @NotNull String songId) {
        for (final NightclubAmbiance nightclubAmbiance : nightclubAmbiances) {
            if (nightclubAmbiance.getSongId().equalsIgnoreCase(songId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasVoted(final @NotNull Player player) {
        return alreadyVoted.contains(player.getUniqueId());
    }

    public void clearData() {
        nightclubAmbiances.clear();
    }
}
