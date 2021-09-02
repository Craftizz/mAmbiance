package io.github.craftizz.mambiance.managers;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.ambiance.Ambiance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class AmbianceManager {

    private final HashMap<UUID, AmbiancePlayer> ambiancePlayerMap;
    private final HashMap<String, Ambiance> ambianceMap;

    public AmbianceManager() {
        this.ambianceMap = new HashMap<>();
        this.ambiancePlayerMap = new HashMap<>();
    }

    public void addSound(final @NotNull Ambiance ambiance) {
        ambianceMap.put(ambiance.getRegionId(), ambiance);
    }

    public Ambiance getAmbiance(final @NotNull String regionId) {
        return ambianceMap.get(regionId);
    }

    public AmbiancePlayer getAmbiancePlayer(final @NotNull Player player) {
        return ambiancePlayerMap.get(player.getUniqueId());
    }

    public void addAmbiancePlayer(final @NotNull UUID uniqueId) {
        ambiancePlayerMap.put(uniqueId, new AmbiancePlayer(uniqueId));
    }

    public void removeAmbiancePlayer(final @NotNull UUID uniqueId) {
        ambiancePlayerMap.remove(uniqueId);
    }



}
