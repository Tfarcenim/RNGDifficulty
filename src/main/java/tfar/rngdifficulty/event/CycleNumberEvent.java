package tfar.rngdifficulty.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.*;

public class CycleNumberEvent extends ScheduledEvent {

    private static final String[] symbols = new String[]{"Θ", "≈", "ε", "⨅", "σ", "δ", "ᔑ", "φ", "ø", "τ", "ʖ", "\ud835\ude79", "⋮", "∴", "∷", "\u268D", "µ", "Þ", "¤", "\u221E"};
    private final int number;

    public CycleNumberEvent(long delay, ServerPlayerEntity player) {
        super(delay, player);
        this.number = player.getRNG().nextInt(20);
    }

    @Override
    public void run() {
        super.run();

        ITextComponent component = new StringTextComponent(symbols[number]).mergeStyle(TextFormatting.GOLD);
        ITextComponent component1 = new StringTextComponent(number + 1 + "").mergeStyle(TextFormatting.GOLD);
        STitlePacket packet1 = null;
        STitlePacket packet2 = null;
        STitlePacket packet3 = null;
        try {
            packet1 = new STitlePacket(0, 3, 15);
            packet2 = new STitlePacket(STitlePacket.Type.TITLE, TextComponentUtils.func_240645_a_(player.getServer().getCommandSource(), component, player,0));
            packet3 = new STitlePacket(STitlePacket.Type.SUBTITLE, TextComponentUtils.func_240645_a_(player.getServer().getCommandSource(), component1, player,0));
        } catch (CommandException | CommandSyntaxException e) {
            e.printStackTrace();
        }
        player.connection.sendPacket(packet1);
        player.connection.sendPacket(packet2);
        player.connection.sendPacket(packet3);
    }

    public int getNumber() {
        return number;
    }
}
