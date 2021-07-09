package tfar.rngdifficulty.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class ScheduledEatEvent extends ScheduledActionEvent {


    public ScheduledEatEvent(long delay, EntityPlayerMP player, int roll) {
        super(delay, player, roll);
    }

    @Override
    public void performEvent() {
        if (roll == 20) {
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 2));
            player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1200, 2));
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 2));
        }

        if (roll > 16) {
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1200, 1));
        }

        if (roll > 13) {
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1200, 1));
        }

        if (roll < 10) {
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 2));
        }

        if (roll < 8) {
            player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1200, 3));
        }

        if (roll < 6) {
            player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 1200, 2));
        }

        if (roll < 4) {
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 1200, 2));
        }

        if (roll == 1) {
            player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 1200, 2));
        }
    }
}
