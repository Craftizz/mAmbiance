package io.github.craftizz.mambiance;

import com.sk89q.worldguard.WorldGuard;
import io.github.craftizz.mambiance.ambiance.NightclubAmbiance;
import io.github.craftizz.mambiance.commands.EnableDisableCommand;
import io.github.craftizz.mambiance.commands.ReloadCommand;
import io.github.craftizz.mambiance.commands.VoteCommand;
import io.github.craftizz.mambiance.configuration.ConfigurationHandler;
import io.github.craftizz.mambiance.listeners.PlayerListener;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import io.github.craftizz.mambiance.managers.NightClubManager;
import io.github.craftizz.mambiance.task.PlaySoundTask;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.CompletionHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public final class MAmbiance extends JavaPlugin {

    private NamespacedKey namespacedKey;
    private ConfigurationHandler configurationHandler;
    private WorldGuard worldGuard;

    private NightClubManager nightClubManager;
    private AmbianceManager ambianceManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {

        hookWorldguard();
        registerNameSpace();

        this.configurationHandler = new ConfigurationHandler(this);

        // Register Managers
        this.nightClubManager = new NightClubManager();
        this.ambianceManager = new AmbianceManager();
        this.commandManager = new CommandManager(this);

        // Register Commands
        registerTabCompletions();
        commandManager.register(
                new EnableDisableCommand(this),
                new ReloadCommand(this),
                new VoteCommand(this));

        // Register Listeners
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);

        // Load Files
        configurationHandler.loadAmbiances();
        configurationHandler.loadNightclub();
        configurationHandler.setupLanguage();
        configurationHandler.setupSounds();
        nightClubManager.nextWinningMusic();

        // Start Runnable
        new PlaySoundTask(this).runTaskTimer(this, 20, 20);

        // Register Placeholder
        new AmbianceExpansion(this).register();
    }

    public void reload() {

        ambianceManager.clearData();
        nightClubManager.clearData();

        configurationHandler.loadAmbiances();
        configurationHandler.loadNightclub();
        configurationHandler.setupLanguage();
        configurationHandler.setupSounds();

    }

    public void registerNameSpace() {
        this.namespacedKey = new NamespacedKey(this, "mAmbiance");
    }

    public void hookWorldguard() {
        try {
            this.worldGuard = WorldGuard.getInstance();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }

    public void registerTabCompletions() {

        final CompletionHandler completionHandler = commandManager.getCompletionHandler();

        completionHandler.register("#music", input -> nightClubManager
                .getNightclubAmbiances()
                .stream()
                .map(NightclubAmbiance::getSongId)
                .collect(Collectors.toList()));

    }

    @Override
    public void onDisable() {
        configurationHandler.saveVotes();
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

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public NightClubManager getNightClubManager() {
        return nightClubManager;
    }
}
