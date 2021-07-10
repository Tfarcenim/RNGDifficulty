package tfar.rngdifficulty;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Perform {

    public static void doEvent(EventType type, PlayerEntity player, int roll,Object... objects) {

        if (roll == 20) {
            player.addPotionEffect(new EffectInstance(Effects.SPEED, 1200, 2));
            player.addPotionEffect(new EffectInstance(Effects.HASTE, 1200, 2));
            player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1200, 2));
        }

        if (roll > 16) {
            player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 1200, 1));
        }

        if (roll > 13) {
            player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1200, 1));
        }

        if (roll < 10) {
            player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 1200, 2));
        }

        if (roll < 8) {
            player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 1200, 3));
        }

        if (roll < 6) {
            player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 1200, 2));
        }

        if (roll < 4) {
            player.addPotionEffect(new EffectInstance(Effects.POISON, 1200, 2));
        }

        if (roll == 1) {
            player.addPotionEffect(new EffectInstance(Effects.WITHER, 1200, 2));
        }


        if (type == EventType.MINE) {
            BlockPos pos = (BlockPos) objects[0];
            BlockState state = (BlockState) objects[1];
            if (state.getBlock().getRegistryName().toString().contains("log")) {
                if (roll == 20) {
                    player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(Items.DIAMOND, player.getRNG().nextInt(4) + 1)));
                } else if (roll > 16) {
                    player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(Items.GOLD_INGOT, roll - 15)));
                    player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(Items.GOLD_NUGGET, (roll - 13) * 3)));
                } else if (roll > 8) {
                    player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(state.getBlock(), roll - 7)));
                } else if (roll > 1) {
                    for (int i = 0; i < (13 - roll) * 2; ++i) {

                        Entity entity;

                        if (ModList.get().isLoaded("mutantbeasts")) {
                            entity = MutantBeastsProxy.getMutantBeast(player.world);
                        } else  {
                            entity = EntityType.CREEPER.create(player.world);
                        }

                        entity.setPosition(player.getPosX(), player.getPosY() + 5, player.getPosZ());
                        player.world.addEntity(entity);
                    }
                } else {
                    WitherEntity witherBoss = EntityType.WITHER.create(player.world);
                    witherBoss.setPosition(player.getPosX() + player.getRNG().nextInt(5) - 2, player.getPosY() + 1 + player.getRNG().nextInt(5), player.getPosZ() + player.getRNG().nextInt(5) - 2);
                    player.world.addEntity(witherBoss);
                }
            } else if (state.getBlock() == Blocks.STONE) {
                final List<BlockPos> blockPosList = Arrays.stream(Direction.values()).map(pos::offset).collect(Collectors.toList());
                blockPosList.removeIf(pos1 -> !isSurrounded(player,pos1));
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
                            world.removeBlock(pos1,false);
                            for (int j = 0; j < 14 - roll; ++j) {
                                SilverfishEntity silverfish = EntityType.SILVERFISH.create(world);
                                silverfish.setPosition(pos1.getX() + .5, pos1.getY() + .5, pos1.getZ() + .5);
                                world.addEntity(silverfish);
                            }
                        } else if (roll > 3) {
                            world.setBlockState(pos1, Blocks.LAVA.getDefaultState());
                        } else {
                            world.removeBlock(pos1,false);
                            TNTEntity tnt = EntityType.TNT.create(world);
                            tnt.setPosition(pos1.getX() + .5, pos1.getY() + .5, pos1.getZ() + .5);
                            tnt.setFuse(roll * 12);
                            world.addEntity(tnt);
                        }
                    }
                }
            }
        }

        else if (type == EventType.CRAFT) {
            ItemStack crafted = (ItemStack) objects[0];
            switch (roll) {
                case 1: {
                    crafted.setDamage(crafted.getMaxDamage());
                    crafted.addEnchantment(Enchantments.BINDING_CURSE,1);
                    crafted.addEnchantment(Enchantments.VANISHING_CURSE,1);
                    break;
                }

                case 20: {
                    for (int i = 0; i < 4; i++) {
                        List<EnchantmentData> enchantmentData = EnchantmentHelper.buildEnchantmentList(player.getRNG(),crafted,90,true);
                        for (EnchantmentData data : enchantmentData) {
                            if (EnchantmentHelper.getEnchantmentLevel(data.enchantment,crafted) == 0)
                                crafted.addEnchantment(data.enchantment,data.enchantmentLevel);
                        }
                    }
                    break;
                }

                default: {
                    int damage = (16 - roll) * crafted.getMaxDamage() / 15;
                    crafted.setDamage(damage);
                    break;
                }
            }
            player.addItemStackToInventory(crafted);
        }
    }


    private static boolean isSurrounded(PlayerEntity player,BlockPos toCheck) {
        World world = player.world;
        return Arrays.stream(Direction.values()).map(toCheck::offset).map(world::getBlockState).map(BlockState::getBlock)
                .anyMatch(block -> block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRAVEL);
    }
}
