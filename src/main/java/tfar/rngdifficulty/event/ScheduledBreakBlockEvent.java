package tfar.rngdifficulty.event;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import tfar.rngdifficulty.EventType;
import tfar.rngdifficulty.Perform;

public class ScheduledBreakBlockEvent extends ScheduledActionEvent {
    private final BlockState state;
    private final BlockPos pos;

    public ScheduledBreakBlockEvent(long delay, ServerPlayerEntity player, int roll, BlockState state, BlockPos pos) {
        super(delay, player, roll, EventType.MINE);
        this.state = state;
        this.pos = pos;
    }

    public static double stoneRollChance = 0.012D;

    @Override
    public void performEvent() {
        Perform.doEvent(this.type,player,roll,pos,state);
    }


}