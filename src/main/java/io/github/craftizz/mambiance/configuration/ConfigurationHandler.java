package io.github.craftizz.mambiance.configuration;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import io.github.craftizz.mambiance.MAmbiance;
import io.github.craftizz.mambiance.ambiance.AmbianceType;
import io.github.craftizz.mambiance.ambiance.CoordinateAmbiance;
import io.github.craftizz.mambiance.ambiance.FollowAmbiance;
import io.github.craftizz.mambiance.ambiance.SoundType;
import io.github.craftizz.mambiance.ambiance.types.AbstractSound;
import io.github.craftizz.mambiance.ambiance.types.DynamicSound;
import io.github.craftizz.mambiance.ambiance.types.StaticSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SingleSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.TickSound;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationHandler {

    private final static Sound.Source source = Sound.Source.MASTER;

    private final MAmbiance plugin;
    private final Yaml ambiance;

    private int currentId = 1;

    public ConfigurationHandler(final @NotNull MAmbiance plugin) {
        this.plugin = plugin;
        this.ambiance = LightningBuilder
                .fromPath("Ambiance", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("ambiance.yml")
                .createYaml();
    }

    public void loadAmbiances() {

        plugin.getLogger().warning("Loading Ambiances...");

        final AmbianceManager ambianceManager = plugin.getAmbianceManager();

        for (final String regionId : ambiance.singleLayerKeySet()) {

            plugin.getLogger().warning("Loading " + regionId);

            final AmbianceType ambianceType = ambiance.getEnum(regionId + ".ambiance-type", AmbianceType.class);

            switch (ambianceType) {
                case FOLLOW -> ambianceManager.addSound(followAmbianceFrom(regionId));
                case LOCATION -> ambianceManager.addSound(coordinateAmbianceFrom(regionId));
                default -> throw new IllegalArgumentException();
            }
        }
    }

    public CoordinateAmbiance coordinateAmbianceFrom(final @NotNull String regionId) {

        final String[] locationData = ambiance
                .getString(regionId + ".location")
                .split(",");

        return new CoordinateAmbiance(regionId, processSoundType(regionId),
                Integer.parseInt(locationData[0]),
                Integer.parseInt(locationData[1]),
                Integer.parseInt(locationData[2]));
    }

    public FollowAmbiance followAmbianceFrom(final @NotNull String regionId) {
        return new FollowAmbiance(regionId, processSoundType(regionId));
    }

    public AbstractSound processSoundType(final @NotNull String regionId) {

        final SoundType soundType = ambiance.getEnum(regionId + ".sound-setting.sound-type", SoundType.class);

        if (soundType == null) {
            throw new IllegalArgumentException();
        }

        return switch (soundType) {
            case DYNAMIC -> processDynamicAmbiance(regionId);
            case STATIC -> processStaticAmbiance(regionId);
        };
    }

    public DynamicSound processDynamicAmbiance(final @NotNull String regionId) {

        final List<TickSound> tickSounds = new ArrayList<>();

        final float volume = ambiance.getFloat(regionId + ".sound-setting.volume");
        final float pitch = ambiance.getFloat(regionId + ".sound-setting.pitch");

        for (String soundKey : ambiance.getStringList(regionId + ".sound-setting.sound-key")) {

            final String[] soundData = soundKey.split(":");

            if (soundData.length != 4) {
                throw new IllegalArgumentException(soundKey);
            }

            @Subst("minecraft") String nameSpaceKey = soundData[0];
            @Subst("empty") String fontKey = soundData[1];

            final Key key = Key.key(nameSpaceKey, fontKey);
            final int duration = Integer.parseInt(soundData[2]);
            final int tick = Integer.parseInt(soundData[3]);


            tickSounds.add(new TickSound(Sound.sound(key, source, volume, pitch), duration, tick, currentId++));
        }

        return new DynamicSound(tickSounds);
    }

    public @NotNull StaticSound processStaticAmbiance(final @NotNull String regionId) {

        final String soundKey = ambiance.getString(regionId + ".sound-setting.sound-key");
        final String[] soundData = soundKey.split(":");

        if (soundData.length != 2) {
            throw new IllegalArgumentException(soundKey);
        }

        @Subst("minecraft") String nameSpaceKey = soundData[0];
        @Subst("empty") String fontKey = soundData[1];

        final Key key = Key.key(nameSpaceKey, fontKey);
        final float volume = ambiance.getFloat(regionId + ".sound-setting.volume");
        final float pitch = ambiance.getFloat(regionId + ".sound-setting.pitch");
        final int duration = ambiance.getInt(regionId + ".sound-setting.duration");

        final SingleSound singleSound = new SingleSound(Sound.sound(key, source, volume, pitch), duration, currentId++);

        return new StaticSound(singleSound);
    }


}
