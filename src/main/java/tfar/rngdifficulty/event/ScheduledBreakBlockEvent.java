package tfar.rngdifficulty.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import tfar.rngdifficulty.MutantBeastsProxy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduledBreakBlockEvent extends ScheduledActionEvent {
    private final IBlockState state;
    private final BlockPos pos;

    public ScheduledBreakBlockEvent(long delay, EntityPlayerMP player, int roll, IBlockState state, BlockPos pos) {
        super(delay, player, roll);
        this.state = state;
        this.pos = pos;
    }

    public static double stoneRollChance = 0.012D;

    @Override
    public void performEvent() {
        if (state.getBlock().getRegistryName().toString().contains("log")) {
            if (roll == 20) {
                player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(Items.DIAMOND, player.getRNG().nextInt(4) + 1)));
            } else if (roll > 16) {
                player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(Items.GOLD_INGOT, roll - 15)));
                player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(Items.GOLD_NUGGET, (roll - 13) * 3)));
            } else if (roll > 8) {
                player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(state.getBlock(), roll - 7)));
            } else if (roll > 1) {
                for (int i = 0; i < (13 - roll) * 2; ++i) {

                    Entity entity;

                    if (Loader.isModLoaded("mutantbeasts")) {
                        entity = MutantBeastsProxy.getMutantBeast(player.world);
                    } else  {
                        entity = new EntityCreeper(player.world);
                    }

                    entity.setPosition(player.posX, player.posY + 5, player.posZ);
                    player.world.spawnEntity(entity);
                }
            } else {
                EntityWither witherBoss = new EntityWither(player.world);
                witherBoss.setPosition(player.posX + player.getRNG().nextInt(5) - 2, player.posY + 1 + player.getRNG().nextInt(5), player.posZ + player.getRNG().nextInt(5) - 2);
                player.world.spawnEntity(witherBoss);
            }
        } else if (state.getBlock() == Blocks.STONE) {
            final List<BlockPos> blockPosList = Arrays.stream(EnumFacing.VALUES).map(pos::offset).collect(Collectors.toList());
            blockPosList.removeIf(pos -> !this.isSurrounded(pos));
            World world = player.world;
            for (BlockPos pos1 : blockPosList) {
                if (roll == 20) {
                    world.setBlockState(pos1, Blocks.DIAMOND_ORE.getDefaultState());
                } else if (roll > 15) {
                    world.setBlockState(pos1, Blocks.GOLD_ORE.getDefaultState());
                } else if (roll > 12) {
                    world.setBlockState(pos1, Blocks.IRON_ORE.getDefaultState());
                } else if (roll > 10) {
                    world.setBlockState(pos1, Blocks.COAL_ORE.getDefaultState());
                } else {
                    if (roll > 7) {
                        world.setBlockToAir(pos1);
                        for (int j = 0; j < 14 - roll; ++j) {
                            EntitySilverfish silverfish = new EntitySilverfish(world);
                            silverfish.setPosition(pos1.getX() + .5, pos1.getY() + .5, pos1.getZ() + .5);
                            world.spawnEntity(silverfish);
                        }
                    } else if (roll > 3) {
                        world.setBlockState(pos1, Blocks.LAVA.getDefaultState());
                    } else {
                        world.setBlockToAir(pos1);
                        EntityTNTPrimed tnt = new EntityTNTPrimed(world);
                        tnt.setPosition(pos1.getX() + .5, pos1.getY() + .5, pos1.getZ() + .5);
                        tnt.setFuse(roll * 12);
                        world.spawnEntity(tnt);
                    }
                }
            }
        }
    }

    private boolean isSurrounded(BlockPos toCheck) {
        World world = player.world;
        return Arrays.stream(EnumFacing.VALUES).map(toCheck::offset).map(world::getBlockState).map(IBlockState::getBlock)
                .anyMatch(block -> block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRAVEL);
    }
}