package tfar.rngdifficulty.event;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ScheduledCraftItemEvent extends ScheduledActionEvent {
    private final int roll;
    private final ItemStack crafted;

    public ScheduledCraftItemEvent(long delay, EntityPlayerMP player, int roll, ItemStack crafted) {
        super(delay, player,roll);
        this.roll = roll;
        this.crafted = crafted;
    }

    @Override
    public void performEvent() {

        switch (roll) {
            case 1: {
                crafted.setItemDamage(crafted.getMaxDamage());
                crafted.addEnchantment(Enchantments.BINDING_CURSE,1);
                crafted.addEnchantment(Enchantments.VANISHING_CURSE,1);
                break;
            }

            case 20: {
                for (int i = 0; i < 4; i++) {
                   List<EnchantmentData> enchantmentData = EnchantmentHelper.buildEnchantmentList(player.getRNG(),crafted,90,true);
                   for (EnchantmentData data : enchantmentData) {
                       if (EnchantmentHelper.getEnchantmentLevel(data.enchantment,crafted) == 0)
                       crafted.addEnchantment(data.enchantment,data.enchantmentLevel);
                   }
                }
                break;
            }

            default: {
                int damage = (16 - roll) * crafted.getMaxDamage() / 15;
                crafted.setItemDamage(damage);
                break;
            }
        }
        player.addItemStackToInventory(crafted);
    }
}
