package fun.lewisdev.deluxehub.inventory;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryItem {

    public final ItemStack itemStack;
    public final List<ClickAction> clickActions;

    public InventoryItem(final ItemStack itemStack) {
        this.clickActions = new ArrayList<>();
        this.itemStack = itemStack;
    }

    public InventoryItem addClickAction(final ClickAction clickAction) {
        this.clickActions.add(clickAction);
        return this;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
