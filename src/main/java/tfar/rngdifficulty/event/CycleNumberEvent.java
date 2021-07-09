package tfar.rngdifficulty.event;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.*;
import tfar.rngdifficulty.RNGDifficulty;

public class CycleNumberEvent extends ScheduledEvent {

    private static final String[] symbols = new String[]{"Θ", "≈", "ε", "⨅", "σ", "δ", "ᔑ", "φ", "ø", "τ", "ʖ", "\ud835\ude79", "⋮", "∴", "∷", "\u268D", "µ", "Þ", "¤", "\u221E"};
    private final int number;

    public CycleNumberEvent(long delay, EntityPlayerMP player) {
        super(delay, player);
        this.number = player.getRNG().nextInt(20);
    }

    @Override
    public void run() {
        super.run();

        ITextComponent component = new TextComponentString(symbols[number]).setStyle(new Style().setColor(TextFormatting.GOLD));
        ITextComponent component1 = new TextComponentString(number + 1 + "").setStyle(new Style().setColor(TextFormatting.GOLD));
        SPacketTitle packet1 = null;
        SPacketTitle packet2 = null;
        SPacketTitle packet3 = null;
        try {
            packet1 = new SPacketTitle(SPacketTitle.Type.TIMES, TextComponentUtils.processComponent(player.getServer(), null, player),
                    0, 3, 15);
            packet2 = new SPacketTitle(SPacketTitle.Type.TITLE, TextComponentUtils.processComponent(player.getServer(), component, player));
            packet3 = new SPacketTitle(SPacketTitle.Type.SUBTITLE, TextComponentUtils.processComponent(player.getServer(), component1, player));
        } catch (CommandException e) {
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
