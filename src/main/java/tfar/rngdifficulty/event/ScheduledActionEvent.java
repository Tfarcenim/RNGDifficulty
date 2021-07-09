package tfar.rngdifficulty.event;

import net.minecraft.entity.player.EntityPlayerMP;

public abstract class ScheduledActionEvent extends ScheduledEvent {

    protected final int roll;

    public ScheduledActionEvent(long delay, EntityPlayerMP player,int roll) {
        super(delay, player);
        this.roll = roll;
    }

    @Override
    public final void run() {
        super.run();
        performEvent();
    }

    public abstract void performEvent();

}
