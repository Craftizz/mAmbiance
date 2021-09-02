package io.github.craftizz.mambiance.listeners;

import io.github.craftizz.mambiance.MAmbiance;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener {

    private final AmbianceManager ambianceManager;

    public PlayerListener(final @NotNull MAmbiance plugin) {
        this.ambianceManager = plugin.getAmbianceManager();
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        ambianceManager.addAmbiancePlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event) {
        ambianceManager.removeAmbiancePlayer(event.getPlayer().getUniqueId());
    }

}
