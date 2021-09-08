package io.github.craftizz.mambiance.commands;

import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.MAmbiance;
import io.github.craftizz.mambiance.configuration.Language;
import io.github.craftizz.mambiance.configuration.MessageType;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import io.github.craftizz.mambiance.utils.MessageUtils;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@Command("ambiance")
public class EnableDisableCommand extends CommandBase {

    private final AmbianceManager ambianceManager;
    private final NamespacedKey namespacedKey;

    public EnableDisableCommand(final @NotNull MAmbiance plugin) {
        this.ambianceManager = plugin.getAmbianceManager();
        this.namespacedKey = plugin.getNamespacedKey();
    }

    @SubCommand("disable")
    @Permission("mambiance.disable")
    public void onDisableCommand(final @NotNull Player player) {

        final PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        final Integer status = dataContainer.get(namespacedKey, PersistentDataType.INTEGER);

        if (status == null || status == 0) {

            dataContainer.set(namespacedKey, PersistentDataType.INTEGER, 1);

            final AmbiancePlayer ambiancePlayer = ambianceManager.getAmbiancePlayer(player);

            ambiancePlayer.stopSound(player);
            ambianceManager.removeAmbiancePlayer(player.getUniqueId());

            MessageUtils.sendMessage(player,
                    Language.DISABLED_AMBIANCE,
                    MessageType.SUCCESS_SOUND);

        } else if (status == 1) {

            MessageUtils.sendMessage(player,
                    Language.ALREADY_DISABLED,
                    MessageType.DENY_SOUND);
        }
    }

    @SubCommand("enable")
    @Permission("mambiance.enable")
    public void onEnableCommand(final @NotNull Player player) {

        final PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        final Integer status = dataContainer.get(namespacedKey, PersistentDataType.INTEGER);

        if (status == null || status == 0) {
            MessageUtils.sendMessage(player,
                    Language.ALREADY_ENABLED,
                    MessageType.DENY_SOUND);
        } else if (status == 1) {

            dataContainer.set(namespacedKey, PersistentDataType.INTEGER, 0);
            ambianceManager.addAmbiancePlayer(player.getUniqueId());

            MessageUtils.sendMessage(player,
                    Language.ENABLED_AMBIANCE,
                    MessageType.SUCCESS_SOUND);
        }
    }

}
