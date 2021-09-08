package io.github.craftizz.mambiance.listeners;

import io.github.craftizz.mambiance.MAmbiance;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener {

    private final NamespacedKey namespacedKey;
    private final AmbianceManager ambianceManager;

    public PlayerListener(final @NotNull MAmbiance plugin) {
        this.ambianceManager = plugin.getAmbianceManager();
        this.namespacedKey = plugin.getNamespacedKey();
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        if (dataContainer.getOrDefault(namespacedKey, PersistentDataType.INTEGER, 0) == 0) {
            ambianceManager.addAmbiancePlayer(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event) {
        ambianceManager.removeAmbiancePlayer(event.getPlayer().getUniqueId());
    }

}
