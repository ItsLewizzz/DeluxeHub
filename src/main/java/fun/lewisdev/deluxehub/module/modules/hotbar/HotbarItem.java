package fun.lewisdev.deluxehub.module.modules.hotbar;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fun.lewisdev.deluxehub.DeluxeHub;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class HotbarItem implements Listener {

    private HotbarManager hotbarManager;
    private ItemStack item;
    private String key;
    private String permission = null;
    private int slot;
    private boolean allowMovement;

    public HotbarItem(HotbarManager hotbarManager, ItemStack item, int slot, String key) {
        this.hotbarManager = hotbarManager;
        this.key = key;
        this.slot = slot;

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("hotbarItem", key);
        this.item = nbtItem.getItem();
    }

    public DeluxeHub getPlugin() {
        return hotbarManager.getPlugin();
    }

    public HotbarManager getHotbarManager() {
        return hotbarManager;
    }

    public ItemStack getItem() {
        return item;
    }

    protected abstract void onInteract(Player player);

    public String getKey() {
        return key;
    }

    public int getSlot() {
        return slot;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setAllowMovement(boolean allowMovement) {
        this.allowMovement = allowMovement;
    }

    public String getPermission() {
        return permission;
    }

    public void giveItem(Player player) {
        if (permission != null && !player.hasPermission(permission)) return;
        player.getInventory().setItem(slot, item);
    }

    public void removeItem(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItem(slot);

        if (item != null && new NBTItem(item).getString("hotbarItem").equals(key)) {
            inventory.remove(inventory.getItem(slot));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!allowMovement) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (getHotbarManager().inDisabledWorld(player.getLocation())) return;
        if ((event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) && (event.getCursor() == null || event.getCursor().getType() == Material.AIR))
            return;

        if (event.getSlot() == slot && new NBTItem(clicked).getString("hotbarItem").equals(key))
            event.setCancelled(true);
    }

    @EventHandler
    public void hotbarItemInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (getPlugin().getServerVersionNumber() > 8 && event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (getHotbarManager().inDisabledWorld(player.getLocation())) return;
        if (item == null || item.getType() == Material.AIR) return;
        if (!new NBTItem(item).getString("hotbarItem").equals(key)) return;

        onInteract(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hotbarPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!getHotbarManager().inDisabledWorld(player.getLocation())) giveItem(player);
    }

    @EventHandler
    public void hotbarPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!getHotbarManager().inDisabledWorld(player.getLocation())) removeItem(player);
    }

    @EventHandler
    public void hotbarWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (getHotbarManager().inDisabledWorld(player.getLocation())) {
            removeItem(player);
        } else {
            giveItem(player);
        }
    }

    @EventHandler
    public void hotbarPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!getHotbarManager().inDisabledWorld(player.getLocation())) giveItem(player);
    }

}
