package org.example.MODNAME.game;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.example.MODNAME.game.map.MODCLASSMapConfig;
import xyz.nucleoid.plasmid.api.game.common.config.WaitingLobbyConfig;

public record MODCLASSConfig(WaitingLobbyConfig players, MODCLASSMapConfig mapConfig, int timeLimitSecs) {
    public static final MapCodec<MODCLASSConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            WaitingLobbyConfig.CODEC.fieldOf("players").forGetter(MODCLASSConfig::players),
            MODCLASSMapConfig.CODEC.fieldOf("map").forGetter(MODCLASSConfig::mapConfig),
            Codec.INT.fieldOf("time_limit_secs").forGetter(MODCLASSConfig::timeLimitSecs)
    ).apply(instance, MODCLASSConfig::new));
}
