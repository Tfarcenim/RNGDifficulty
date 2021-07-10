package tfar.rngdifficulty.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import tfar.rngdifficulty.EventType;
import tfar.rngdifficulty.Perform;

public class ScheduledCraftItemEvent extends ScheduledActionEvent {
    private final int roll;
    private final ItemStack crafted;

    public ScheduledCraftItemEvent(long delay, ServerPlayerEntity player, int roll, ItemStack crafted) {
        super(delay, player,roll, EventType.CRAFT);
        this.roll = roll;
        this.crafted = crafted;
    }

    @Override
    public void performEvent() {
        Perform.doEvent(this.type,player,roll,crafted);
    }
}
