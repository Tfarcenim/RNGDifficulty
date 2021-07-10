package tfar.rngdifficulty.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import tfar.rngdifficulty.EventType;

public abstract class ScheduledActionEvent extends ScheduledEvent {

    protected final int roll;
    protected final EventType type;

    public ScheduledActionEvent(long delay, ServerPlayerEntity player, int roll,EventType type) {
        super(delay, player);
        this.roll = roll;
        this.type = type;
    }

    @Override
    public final void run() {
        super.run();
        performEvent();
    }

    public abstract void performEvent();

}
