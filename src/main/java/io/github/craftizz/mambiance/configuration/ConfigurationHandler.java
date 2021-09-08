package io.github.craftizz.mambiance.configuration;

import de.leonhard.storage.Json;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.craftizz.mambiance.MAmbiance;
import io.github.craftizz.mambiance.ambiance.*;
import io.github.craftizz.mambiance.ambiance.types.AbstractSound;
import io.github.craftizz.mambiance.ambiance.types.DynamicSound;
import io.github.craftizz.mambiance.ambiance.types.StaticSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.SingleSound;
import io.github.craftizz.mambiance.ambiance.types.wrapper.TickSound;
import io.github.craftizz.mambiance.managers.AmbianceManager;
import io.github.craftizz.mambiance.managers.NightClubManager;
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
    private final Yaml nightclub;
    private final Yaml language;

    private final Json data;

    private int currentId = 1;

    public ConfigurationHandler(final @NotNull MAmbiance plugin) {
        this.plugin = plugin;

        this.ambiance = LightningBuilder
                .fromPath("ambiance", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("ambiance.yml")
                .createYaml();

        this.nightclub = LightningBuilder
                .fromPath("nightclub", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("nightclub.yml")
                .createYaml();

        this.language = LightningBuilder
                .fromPath("language", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("lang.yml")
                .createYaml();

        this.data = LightningBuilder
                .fromPath("data", plugin.getDataFolder().getAbsolutePath())
                .createJson();
    }

    public void saveVotes() {
        final NightClubManager nightClubManager = plugin.getNightClubManager();
        for (final NightclubAmbiance ambiance : nightClubManager.getNightclubAmbiances()) {
            data.set(ambiance.getSongId() + ".votes", ambiance.getVotes());
        }
    }

    public void setupLanguage() {
        for (Language languageEnum : Language.values()) {
            languageEnum.setMessage(language.getString(languageEnum.getConfigPath()));
        }
    }

    public void setupSounds() {
        for (MessageType soundType : MessageType.values()) {
            soundType.setSound(language.getEnum(soundType.getConfigPath(), org.bukkit.Sound.class));
        }
    }


    public void loadNightclub() {

        final NightClubManager nightClubManager = plugin.getNightClubManager();

        nightClubManager.setNightclubRegion(nightclub.getString("nightclub.region-name"));
        nightClubManager.setInterval(nightclub.getInt("nightclub.interval"));
        nightClubManager.setTimeLeft(nightclub.getInt("nightclub.interval"));

        final String[] locationData = nightclub.getString("nightclub.location").split(",");

        final FlatFileSection section = nightclub.getSection("music");

        for (final String music : section.singleLayerKeySet()) {

            final String soundKey = section.getString(music + ".sound-key");
            final String[] soundData = soundKey.split(":");

            if (soundData.length != 2) {
                throw new IllegalArgumentException("Invalid Sound key " + soundKey);
            }

            @Subst("minecraft") String nameSpaceKey = soundData[0];
            @Subst("empty") String fontKey = soundData[1];

            final Key key = Key.key(nameSpaceKey, fontKey);
            final float volume = section.getFloat(music + ".volume");
            final float pitch = section.getFloat(music + ".pitch");
            final int duration = section.getInt(music + ".duration");

            final StaticSound staticSound = new StaticSound(new SingleSound(Sound.sound(key, source, volume, pitch), duration, currentId++));

            nightClubManager.addMusic(new NightclubAmbiance(section.getString(music + ".song-name"), staticSound,
                    Integer.parseInt(locationData[0]),
                    Integer.parseInt(locationData[1]),
                    Integer.parseInt(locationData[2])));
        }
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

        final FlatFileSection section = ambiance.getSection(regionId + ".sound-setting.sound-list");

        for (String soundName : section.singleLayerKeySet()) {

            final float volume = section.getFloat(soundName + ".volume");
            final float pitch = section.getFloat(soundName + ".pitch");
            final int duration = section.getInt(soundName + ".duration");
            final int tick = section.getInt(soundName + ".tick");

            final String soundKey = section.getString(soundName + ".sound-key");
            final String[] soundData = soundKey.split(":");

            if (soundData.length != 2) {
                throw new IllegalArgumentException(soundKey);
            }

            @Subst("minecraft") String nameSpaceKey = soundData[0];
            @Subst("empty") String fontKey = soundData[1];
            final Key key = Key.key(nameSpaceKey, fontKey);

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
