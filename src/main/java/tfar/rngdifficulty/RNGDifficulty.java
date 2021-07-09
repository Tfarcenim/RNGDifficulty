package tfar.rngdifficulty;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tfar.rngdifficulty.event.*;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = RNGDifficulty.MODID, name = RNGDifficulty.NAME, version = RNGDifficulty.VERSION)
@Mod.EventBusSubscriber
public class RNGDifficulty {
    public static final String MODID = "rngdifficulty";
    public static final String NAME = "RNG Difficulty";
    public static final String VERSION = "1.0";

    private static final List<ScheduledEvent> active_events = new ArrayList<>();
    private static final List<ScheduledEvent> events_to_add = new ArrayList<>();
    private static final List<ScheduledEvent> events_to_remove = new ArrayList<>();

    private static boolean isRolling;

    public static void setIsRolling() {
        isRolling = true;
    }

    public static void setStopRolling() {
        isRolling = false;
    }

    @SubscribeEvent
    public static void tick(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote && e.phase == TickEvent.Phase.START) {
            tickEvents(e.world.getMinecraftServer());
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent e) {
        if (!e.getPlayer().world.isRemote) {
            IBlockState state = e.getState();
            if (state.getBlock().getRegistryName().toString().contains("log") || (state.getBlock() == Blocks.STONE && e.getPlayer().getRNG().nextDouble() < ScheduledBreakBlockEvent.stoneRollChance)) {
                int i = cycleNumbersAndPick((EntityPlayerMP) e.getPlayer());
                addEventToServer(new ScheduledBreakBlockEvent(2 * itr + 4, (EntityPlayerMP) e.getPlayer(), i, e.getState(),e.getPos()));
            }
        }
    }

    @SubscribeEvent
    public static void craftItem(PlayerEvent.ItemCraftedEvent e) {
        if (!e.player.world.isRemote && e.crafting.isItemStackDamageable()) {
            int i = cycleNumbersAndPick((EntityPlayerMP) e.player);
            ItemStack crafted = e.crafting.copy();
            e.craftMatrix.clear();
            e.crafting.setCount(0);
            e.player.closeScreen();
            addEventToServer(new ScheduledCraftItemEvent(2 * itr + 4, (EntityPlayerMP) e.player,i,crafted));
        }
    }

    @SubscribeEvent
    public static void onEaten(LivingEntityUseItemEvent.Finish e) {
        if (e.getItem().getItem() instanceof ItemFood && e.getEntityLiving() instanceof EntityPlayerMP) {
            int i = cycleNumbersAndPick((EntityPlayerMP) e.getEntityLiving());
            addEventToServer(new ScheduledEatEvent(2 * itr + 4, (EntityPlayerMP) e.getEntityLiving(),i));
        }
    }

    public static void tickEvents(MinecraftServer server) {
        active_events.addAll(events_to_add);
        events_to_add.clear();

        for (ScheduledEvent event : active_events) {
            if (event.canRun()) {
                event.run();
                events_to_remove.add(event);
            }
        }
        active_events.removeAll(events_to_remove);
        events_to_remove.clear();
    }


    public static int itr = 25;

    public static int cycleNumbersAndPick(EntityPlayerMP player) {
        setIsRolling();
        for (int i = 1 ; i < itr;i++) {
            CycleNumberEvent event = new CycleNumberEvent(i * 2,player);
            RNGDifficulty.addEventToServer(event);
        }

        CycleNumberLastEvent event = new CycleNumberLastEvent(2 * itr,player);
        RNGDifficulty.addEventToServer(event);
        return event.getNumber() + 1;
    }

    public static void addEventToServer(ScheduledEvent event) {
        events_to_add.add(event);
    }
}
