package hack.skate.client.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import hack.skate.client.mixin.ClientPlayerInteractionManagerAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.SwordItem;


public final class InventoryUtils implements Imports {
    public static void setInvSlot(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).syncSlot();
    }

    public static boolean selectItemFromHotbar(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            inv.selectedSlot = i;
            return true;
        }
        return false;
    }

    public static boolean selectItemFromHotbar(Item item) {
        return InventoryUtils.selectItemFromHotbar((Item i) -> i == item);
    }

    public static boolean hasItemInHotbar(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            return true;
        }
        return false;
    }

    public static int countItem(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        int count = 0;
        for (int i = 0; i < 36; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            count += itemStack.getCount();
        }
        return count;
    }

    public static int countItemExceptHotbar(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        int count = 0;
        for (int i = 9; i < 36; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            count += itemStack.getCount();
        }
        return count;
    }

    public static int getSwordSlot() {
        PlayerInventory playerInventory = mc.player.getInventory();
        for (int itemIndex = 0; itemIndex < 9; ++itemIndex) {
            if (!(playerInventory.getStack(itemIndex).getItem() instanceof SwordItem)) continue;
            return itemIndex;
        }
        return -1;
    }

    public static boolean selectSword() {
        int itemIndex = InventoryUtils.getSwordSlot();
        if (itemIndex != -1) {
            InventoryUtils.setInvSlot(itemIndex);
            return true;
        }
        return false;
    }





    public static int findTotemSlot() {
        assert (mc.player != null);
        PlayerInventory inv = mc.player.getInventory();
        for (int index = 9; index < 36; ++index) {
            if (((ItemStack)inv.main.get(index)).getItem() != Items.TOTEM_OF_UNDYING) continue;
            return index;
        }
        return -1;
    }

    public static boolean selectAxe() {
        int itemIndex = InventoryUtils.getAxeSlot();
        if (itemIndex != -1) {
            mc.player.getInventory().selectedSlot = itemIndex;
            return true;
        }
        return false;
    }

    public static int findRandomTotemSlot() {
        PlayerInventory inventory = mc.player.getInventory();
        Random random = new Random();
        ArrayList<Integer> totemIndexes = new ArrayList<Integer>();
        for (int i = 9; i < 36; ++i) {
            if (((ItemStack)inventory.main.get(i)).getItem() != Items.TOTEM_OF_UNDYING) continue;
            totemIndexes.add(i);
        }
        if (!totemIndexes.isEmpty()) {
            return (Integer)totemIndexes.get(random.nextInt(totemIndexes.size()));
        }
        return -1;
    }

    public static int findRandomPot(String potion) {
        PlayerInventory inventory = mc.player.getInventory();
        Random random = new Random();
        int slotIndex = random.nextInt(27) + 9;
        for (int i = 0; i < 27; ++i) {
            int index = (slotIndex + i) % 36;
            ItemStack itemStack = (ItemStack)inventory.main.get(index);
            if (!(itemStack.getItem() instanceof SplashPotionItem) || index == 36 && index == 37 && index == 38 && index == 39) continue;
            if (!((PotionContentsComponent)itemStack.get(DataComponentTypes.POTION_CONTENTS)).getEffects().toString().contains(potion.toString())) {
                return -1;
            }
            return index;
        }
        return -1;
    }



    public static List<Integer> getEmptyHotbarSlots() {
        PlayerInventory inventory = mc.player.getInventory();
        ArrayList<Integer> slots = new ArrayList<Integer>();
        for (int i = 0; i < 9; ++i) {
            if (((ItemStack)inventory.main.get(i)).isEmpty()) {
                slots.add(i);
                continue;
            }
            if (!slots.contains(i) || ((ItemStack)inventory.main.get(i)).isEmpty()) continue;
            slots.remove(i);
        }
        return slots;
    }

    public static int getAxeSlot() {
        PlayerInventory playerInventory = mc.player.getInventory();
        for (int itemIndex = 0; itemIndex < 9; ++itemIndex) {
            if (!(playerInventory.getStack(itemIndex).getItem() instanceof AxeItem)) continue;
            return itemIndex;
        }
        return -1;
    }

    public static int countItem(Item item) {
        return InventoryUtils.countItem((Item i) -> i == item);
    }
}
