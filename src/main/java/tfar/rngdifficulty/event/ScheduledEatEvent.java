package tfar.rngdifficulty.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import tfar.rngdifficulty.EventType;
import tfar.rngdifficulty.Perform;

public class ScheduledEatEvent extends ScheduledActionEvent {


    public ScheduledEatEvent(long delay, ServerPlayerEntity player, int roll) {
        super(delay, player, roll, EventType.EAT);
    }

    @Override
    public void performEvent() {
        Perform.doEvent(this.type,player,roll);
    }
}
