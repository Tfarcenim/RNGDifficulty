package tfar.rngdifficulty;

//import chumbanotz.mutantbeasts.entity.mutant.MutantCreeperEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.world.World;

public class MutantBeastsProxy {

    public static Entity getMutantBeast(World world) {
        return new CreeperEntity(EntityType.CREEPER, world);//MutantCreeperEntity(world);
    }
}
