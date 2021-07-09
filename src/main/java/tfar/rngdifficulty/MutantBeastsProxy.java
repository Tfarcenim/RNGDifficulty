package tfar.rngdifficulty;

import chumbanotz.mutantbeasts.entity.mutant.MutantCreeperEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class MutantBeastsProxy {

    public static Entity getMutantBeast(World world) {
        return new MutantCreeperEntity(world);
    }
}
