package io.github.craftizz.mambiance.task;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import io.github.craftizz.mambiance.AmbiancePlayer;
import io.github.craftizz.mambiance.MAmbiance;
import io.github.craftizz.mambiance.ambiance.Ambiance;
import io.github.craftizz.mambiance.ambiance.NightclubAmbiance;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import io.github.craftizz.mambiance.managers.NightClubManager;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlaySoundTask extends BukkitRunnable {

    private final AmbianceManager ambianceManager;
    private final NightClubManager nightClubManager;
    private final RegionContainer container;

    public PlaySoundTask(final @NotNull MAmbiance plugin) {
        this.ambianceManager = plugin.getAmbianceManager();
        this.nightClubManager = plugin.getNightClubManager();
        this.container = plugin.getWorldGuard().getPlatform().getRegionContainer();
    }

    public void execute() {

        nightClubManager.shouldPlayNextMusic();

        final RegionQuery query = container.createQuery();

        for (final Player player : Bukkit.getOnlinePlayers()) {

            final AmbiancePlayer ambiancePlayer = ambianceManager.getAmbiancePlayer(player);

            if (ambiancePlayer == null) {
                continue;
            }

            final Location location = BukkitAdapter.adapt(player.getLocation());
            final ApplicableRegionSet regions = query.getApplicableRegions(location);

            ProtectedRegion region = null;

            for (final ProtectedRegion protectedRegion : regions) {

                if (region == null) {
                    region = protectedRegion;
                }

                else if (protectedRegion.getPriority() > region.getPriority()) {
                    region = protectedRegion;
                }
            }

            if (region == null) {

                final SoundStop soundStop = ambiancePlayer.getSoundStop();

                if (soundStop != null) {
                    ambiancePlayer.stopSound(player);
                    ambiancePlayer.setSoundId(0);
                }

                continue;
            }

            if (nightClubManager.isNightClub(region.getId())) {
                nightClubManager.getCurrentMusic().playSound(player, ambiancePlayer);
                continue;

            }

            ambiancePlayer.setTick(player.getWorld().getTime());

            final Ambiance ambiance = ambianceManager.getAmbiance(region.getId());

            if (ambiance == null) {
                continue;
            }

            ambiance.playSound(player, ambiancePlayer);

        }

    }

    @Override
    public void run() {
        execute();
    }
}
