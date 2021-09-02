package io.github.craftizz.mambiance;

import com.sk89q.worldguard.WorldGuard;
import io.github.craftizz.mambiance.configuration.ConfigurationHandler;
import io.github.craftizz.mambiance.listeners.PlayerListener;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import io.github.craftizz.mambiance.task.PlaySoundTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MAmbiance extends JavaPlugin {

    private ConfigurationHandler configurationHandler;
    private WorldGuard worldGuard;

    private AmbianceManager ambianceManager;

    @Override
    public void onEnable() {

        hookWorldguard();

        this.configurationHandler = new ConfigurationHandler(this);

        // Register Managers
        this.ambianceManager = new AmbianceManager();

        // Register Commands

        // Register Listeners
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);

        configurationHandler.loadAmbiances();

        new PlaySoundTask(ambianceManager, worldGuard.getPlatform().getRegionContainer()).runTaskTimer(this, 20, 20);
    }

    public void hookWorldguard() {
        try {
            this.worldGuard = WorldGuard.getInstance();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public WorldGuard getWorldGuard() {
        return worldGuard;
    }

    public AmbianceManager getAmbianceManager() {
        return ambianceManager;
    }

    public ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }
}
