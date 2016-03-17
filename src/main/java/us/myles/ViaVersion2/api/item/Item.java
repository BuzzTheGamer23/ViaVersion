package us.myles.ViaVersion2.api.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private short id;
    private byte amount;
    private short data;
    private CompoundTag tag;

    public static Item getItem(ItemStack stack) {
        if(stack == null) return null;
        return new Item((short) stack.getTypeId(), (byte) stack.getAmount(), stack.getDurability(), null);
    }
}