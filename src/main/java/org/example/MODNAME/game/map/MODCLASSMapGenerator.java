package org.example.MODNAME.game.map;

import xyz.nucleoid.map_templates.MapTemplate;
import net.minecraft.util.math.BlockPos;

public class MODCLASSMapGenerator {

    private final MODCLASSMapConfig config;

    public MODCLASSMapGenerator(MODCLASSMapConfig config) {
        this.config = config;
    }

    public MODCLASSMap build() {
        MapTemplate template = MapTemplate.createEmpty();
        MODCLASSMap map = new MODCLASSMap(template, this.config);

        this.buildSpawn(template);
        map.spawn = new BlockPos(0,65,0);

        return map;
    }

    private void buildSpawn(MapTemplate builder) {
        BlockPos min = new BlockPos(-5, 64, -5);
        BlockPos max = new BlockPos(5, 64, 5);

        for (BlockPos pos : BlockPos.iterate(min, max)) {
            builder.setBlockState(pos, this.config.spawnBlock);
        }
    }
}
