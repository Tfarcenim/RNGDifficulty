package tfar.rngdifficulty.event;

import net.minecraft.entity.player.ServerPlayerEntity;

public class ScheduledEvent implements Runnable {

    public boolean ran;

    public final long startTick;
    public final long delay;
    public final long endTick;
    public final ServerPlayerEntity player;

    public ScheduledEvent(long delay, ServerPlayerEntity player) {
        this.startTick = player.world.getGameTime();
        this.delay = delay;
        this.endTick = delay + startTick;
        this.player = player;
    }

    public boolean canRun() {
        return !ran && player.world.getWorldInfo().getGameTime() >= endTick;
    }

    @Override
    public void run() {
        ran = true;
    }
}
