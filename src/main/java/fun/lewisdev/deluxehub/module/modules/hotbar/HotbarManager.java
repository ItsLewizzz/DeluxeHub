package fun.lewisdev.deluxehub.module.modules.hotbar;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.hotbar.items.CustomItem;
import fun.lewisdev.deluxehub.module.modules.hotbar.items.PlayerHider;
import fun.lewisdev.deluxehub.utility.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HotbarManager extends Module {

    private List<HotbarItem> hotbarItems;

    public HotbarManager(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.HOTBAR_ITEMS);
    }

    @Override
    public void onEnable() {
        hotbarItems = new ArrayList<>();
        FileConfiguration config = getConfig(ConfigType.SETTINGS);

        if (config.getBoolean("custom_join_items.enabled")) {

            for (String entry : config.getConfigurationSection("custom_join_items.items").getKeys(false)) {
                ItemStack item = ItemStackBuilder.getItemStack(config.getConfigurationSection("custom_join_items.items." + entry)).build();
                CustomItem customItem = new CustomItem(this, item, config.getInt("custom_join_items.items." + entry + ".slot"), entry);

                if (config.contains("custom_join_items.items." + entry + ".permission")) {
                    customItem.setPermission(config.getString("custom_join_items.items." + entry + ".permission"));
                }

                customItem.setConfigurationSection(config.getConfigurationSection("custom_join_items.items." + entry));
                customItem.setAllowMovement(config.getBoolean("custom_join_items.disable_inventory_movement"));
                registerHotbarItem(customItem);
            }

        }

        if (config.getBoolean("player_hider.enabled")) {
            ItemStack item = ItemStackBuilder.getItemStack(config.getConfigurationSection("player_hider.not_hidden")).build();
            PlayerHider playerHider = new PlayerHider(this, item, config.getInt("player_hider.slot"), "PLAYER_HIDER");

            playerHider.setAllowMovement(config.getBoolean("player_hider.disable_inventory_movement"));

            registerHotbarItem(playerHider);
        }

        giveItems();
    }

    @Override
    public void onDisable() {
        removeItems();
    }

    public List<HotbarItem> getHotbarItems() {
        return hotbarItems;
    }

    public void registerHotbarItem(HotbarItem hotbarItem) {
        getPlugin().getServer().getPluginManager().registerEvents(hotbarItem, getPlugin());
        hotbarItems.add(hotbarItem);
    }

    private void giveItems() {
        Bukkit.getOnlinePlayers().stream().filter(player -> !inDisabledWorld(player.getLocation())).forEach(player -> hotbarItems.forEach(hotbarItem -> hotbarItem.giveItem(player)));
    }

    private void removeItems() {
        Bukkit.getOnlinePlayers().stream().filter(player -> !inDisabledWorld(player.getLocation())).forEach(player -> hotbarItems.forEach(hotbarItem -> hotbarItem.removeItem(player)));
    }

}
