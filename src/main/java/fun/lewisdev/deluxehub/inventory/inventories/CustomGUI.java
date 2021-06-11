package fun.lewisdev.deluxehub.inventory.inventories;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.inventory.AbstractInventory;
import fun.lewisdev.deluxehub.inventory.InventoryBuilder;
import fun.lewisdev.deluxehub.inventory.InventoryItem;
import fun.lewisdev.deluxehub.utility.ItemStackBuilder;
import fun.lewisdev.deluxehub.utility.TextUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

public class CustomGUI extends AbstractInventory {

    private InventoryBuilder inventory;
    private FileConfiguration config;

    public CustomGUI(DeluxeHubPlugin plugin, FileConfiguration config) {
        super(plugin);
        this.config = config;
    }

    @Override
    public void onEnable() {

        InventoryBuilder inventoryBuilder = new InventoryBuilder(config.getInt("slots"), TextUtil.color(config.getString("title")));

        if (config.contains("refresh") && config.getBoolean("refresh.enabled")) {
            setInventoryRefresh(config.getLong("refresh.rate"));
        }

        for (String entry : config.getConfigurationSection("items").getKeys(false)) {

            try {
                ItemStackBuilder builder = ItemStackBuilder.getItemStack(config.getConfigurationSection("items." + entry));

                InventoryItem inventoryItem;
                if (!config.contains("items." + entry + ".actions")) {
                    inventoryItem = new InventoryItem(builder.build());
                } else {
                    inventoryItem = new InventoryItem(builder.build()).addClickAction(p -> getPlugin().getActionManager().executeActions(p, config.getStringList("items." + entry + ".actions")));
                }

                if (config.contains("items." + entry + ".slots")) {
                    for (String slot : config.getStringList("items." + entry + ".slots")) {
                        inventoryBuilder.setItem(Integer.parseInt(slot), inventoryItem);
                    }
                } else if (config.contains("items." + entry + ".slot")) {
                    int slot = config.getInt("items." + entry + ".slot");
                    if (slot == -1) {
                        while (inventoryBuilder.getInventory().firstEmpty() != -1) {
                            inventoryBuilder.setItem(inventoryBuilder.getInventory().firstEmpty(), inventoryItem);
                        }
                    } else inventoryBuilder.setItem(slot, inventoryItem);
                }

            } catch (Exception e) {
                e.printStackTrace();
                getPlugin().getLogger().warning("There was an error loading GUI item ID '" + entry + "', skipping..");
            }
        }

        inventory = inventoryBuilder;

    }

    @Override
    protected Inventory getInventory() {
        return inventory.getInventory();
    }
}
