package io.github.craftizz.mambiance.commands;

import io.github.craftizz.mambiance.MAmbiance;
import io.github.craftizz.mambiance.configuration.Language;
import io.github.craftizz.mambiance.configuration.MessageType;
import io.github.craftizz.mambiance.managers.NightClubManager;
import io.github.craftizz.mambiance.utils.MessageUtils;
import io.github.craftizz.mambiance.utils.NumberUtils;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("nightclub")
public class VoteCommand extends CommandBase {

    private final NightClubManager nightClubManager;

    public VoteCommand(final @NotNull MAmbiance plugin) {
        this.nightClubManager = plugin.getNightClubManager();
    }

    @SubCommand("vote")
    public void onVoteCommand(final Player player, @Completion("#music") String music) {

        if (!nightClubManager.isMusic(music)) {
            MessageUtils.sendMessage(player,
                    Language.NOT_SONG,
                    MessageType.DENY_SOUND,
                    "song", music);
        }

        else if (nightClubManager.hasVoted(player)) {
            MessageUtils.sendMessage(player,
                    Language.ALREADY_VOTED,
                    MessageType.DENY_SOUND,
                    "time", NumberUtils.convertSeconds(nightClubManager.getTimeLeft()));
        }

        else {
            nightClubManager.addVote(player.getUniqueId(), music);
            MessageUtils.sendMessage(player,
                    Language.VOTE_SUCCESS,
                    MessageType.DENY_SOUND,
                    "song", music);
        }
    }

}
