package org.example.MODNAME.game;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.plasmid.game.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.example.MODNAME.game.map.MODCLASSMap;
import org.example.MODNAME.game.map.MODCLASSMapGenerator;
import xyz.nucleoid.plasmid.game.common.GameWaitingLobby;
import xyz.nucleoid.plasmid.game.event.GameActivityEvents;
import xyz.nucleoid.plasmid.game.event.GamePlayerEvents;
import xyz.nucleoid.stimuli.event.player.PlayerDeathEvent;

public class MODCLASSWaiting {
    private final GameSpace gameSpace;
    private final MODCLASSMap map;
    private final MODCLASSConfig config;
    private final MODCLASSSpawnLogic spawnLogic;
    private final ServerWorld world;

    private MODCLASSWaiting(GameSpace gameSpace, ServerWorld world, MODCLASSMap map, MODCLASSConfig config) {
        this.gameSpace = gameSpace;
        this.map = map;
        this.config = config;
        this.world = world;
        this.spawnLogic = new MODCLASSSpawnLogic(gameSpace, world, map);
    }

    public static GameOpenProcedure open(GameOpenContext<MODCLASSConfig> context) {
        MODCLASSConfig config = context.config();
        MODCLASSMapGenerator generator = new MODCLASSMapGenerator(config.mapConfig);
        MODCLASSMap map = generator.build();

        RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
                .setGenerator(map.asGenerator(context.server()));

        return context.openWithWorld(worldConfig, (game, world) -> {
            MODCLASSWaiting waiting = new MODCLASSWaiting(game.getGameSpace(), world, map, context.config());

            GameWaitingLobby.addTo(game, config.playerConfig);

            game.listen(GameActivityEvents.REQUEST_START, waiting::requestStart);
            game.listen(GamePlayerEvents.ADD, waiting::addPlayer);
            game.listen(GamePlayerEvents.OFFER, (offer) -> offer.accept(world, Vec3d.ZERO));
            game.listen(PlayerDeathEvent.EVENT, waiting::onPlayerDeath);
        });
    }

    private GameResult requestStart() {
        MODCLASSActive.open(this.gameSpace, this.world, this.map, this.config);
        return GameResult.ok();
    }

    private void addPlayer(ServerPlayerEntity player) {
        this.spawnPlayer(player);
    }

    private ActionResult onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
        player.setHealth(20.0f);
        this.spawnPlayer(player);
        return ActionResult.FAIL;
    }

    private void spawnPlayer(ServerPlayerEntity player) {
        this.spawnLogic.resetPlayer(player, GameMode.ADVENTURE);
        this.spawnLogic.spawnPlayer(player);
    }
}
