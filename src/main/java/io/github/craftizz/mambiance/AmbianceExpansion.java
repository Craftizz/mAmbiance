package io.github.craftizz.mambiance;

import io.github.craftizz.mambiance.managers.AmbianceManager;
import io.github.craftizz.mambiance.managers.NightClubManager;
import io.github.craftizz.mambiance.utils.NumberUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmbianceExpansion extends PlaceholderExpansion {

    private MAmbiance plugin;
    private NamespacedKey namespacedKey;
    private NightClubManager nightClubManager;
    private AmbianceManager ambianceManager;

    public AmbianceExpansion(final @NotNull MAmbiance plugin) {
        this.plugin = plugin;
        this.namespacedKey = plugin.getNamespacedKey();
        this.nightClubManager = plugin.getNightClubManager();
        this.ambianceManager = plugin.getAmbianceManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ambiance";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(",", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return List.of(
                "%ambiance_isenabled%",
                "%ambiance_nightclub_music%",
                "%ambiance_nightclub_timeleft%",
                "%ambiance_nightclub_timeleft%_formatted",
                "%ambiance_nightclub_votes_<song>%"
        );
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String placeholder) {
        return retrieveValue(offlinePlayer, placeholder.toLowerCase().split("_"));
    }

    private String retrieveValue(final @NotNull OfflinePlayer offlinePlayer,
                                 final @NotNull String... arguments) {

        switch (arguments[0]) {

            case "isenabled":

                final Player player = offlinePlayer.getPlayer();

                if (player == null) {
                    return null;
                }

                final PersistentDataContainer dataContainer = player.getPersistentDataContainer();
                final int status = dataContainer.getOrDefault(namespacedKey, PersistentDataType.INTEGER, 0);

                if (status == 0) {
                    return "true";
                } else if (status == 1) {
                    return "false";
                }

            case "nightclub":

                switch (arguments[1]) {

                    case "music":
                        return nightClubManager.getCurrentMusic().getSongId();

                    case "timeleft":

                        if (arguments.length != 3) {
                            return String.valueOf(nightClubManager.getTimeLeft());
                        }

                        else if (arguments[2].equals("formatted")) {
                            return NumberUtils.convertSeconds(nightClubManager.getTimeLeft());
                        }

                    case "votes":
                        return String.valueOf(nightClubManager.getVote(arguments[2]));
                }
        }

        return null;
    }

}
