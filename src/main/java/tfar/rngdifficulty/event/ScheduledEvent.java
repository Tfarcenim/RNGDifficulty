package tfar.rngdifficulty.event;

import net.minecraft.entity.player.EntityPlayerMP;

public class ScheduledEvent implements Runnable {

    public boolean ran;

    public final long startTick;
    public final long delay;
    public final long endTick;
    public final EntityPlayerMP player;

    public ScheduledEvent(long delay, EntityPlayerMP player) {
        this.startTick = player.world.getTotalWorldTime();
        this.delay = delay;
        this.endTick = delay + startTick;
        this.player = player;
    }

    public boolean canRun() {
        return !ran && player.world.getWorldInfo().getWorldTotalTime() >= endTick;
    }

    @Override
    public void run() {
        ran = true;
    }
}
