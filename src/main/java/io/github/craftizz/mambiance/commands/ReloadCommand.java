package io.github.craftizz.mambiance.commands;

import io.github.craftizz.mambiance.MAmbiance;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command("ambiance")
public class ReloadCommand extends CommandBase {

    private final MAmbiance plugin;

    public ReloadCommand(final @NotNull MAmbiance plugin) {
        this.plugin = plugin;
    }

    @SubCommand("reload")
    @Permission("mambiance.relaod")
    public void onReloadCommand(final CommandSender commandSender) {
        commandSender.sendMessage("Plugin Reloaded");
        plugin.reload();
    }

}
