package fun.lewisdev.deluxehub.inventory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.inventory.inventories.CustomGUI;

public class InventoryManager {

    private DeluxeHub plugin;

    private Map<String, AbstractInventory> inventories;

    public InventoryManager() {
        inventories = new HashMap<>();
    }

    public void onEnable(DeluxeHub plugin) {
        this.plugin = plugin;

        loadCustomMenus();

        inventories.values().forEach(AbstractInventory::onEnable);

        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

    private void loadCustomMenus() {

        File directory = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus");

        if (!directory.exists()) {
            directory.mkdir();
            File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus",
                    "serverselector.yml");
            try (InputStream inputStream = this.plugin.getResource("serverselector.yml")) {
                file.createNewFile();
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Load all menu files
        File[] yamlFiles = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus")
                .listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null)
            return;

        for (File file : yamlFiles) {
            String name = file.getName().replace(".yml", "");
            if (inventories.containsKey(name)) {
                plugin.getLogger()
                        .warning("Inventory with name '" + file.getName() + "' already exists, skipping duplicate..");
                continue;
            }

            CustomGUI customGUI;
            try {
                customGUI = new CustomGUI(plugin, YamlConfiguration.loadConfiguration(file));
            } catch (Exception e) {
                plugin.getLogger().severe("Could not load file '" + name + "' (YAML error).");
                e.printStackTrace();
                continue;
            }

            inventories.put(name, customGUI);
            plugin.getLogger().info("Loaded custom menu '" + name + "'.");
        }
    }

    public void addInventory(String key, AbstractInventory inventory) {
        inventories.put(key, inventory);
    }

    public Map<String, AbstractInventory> getInventories() {
        return inventories;
    }

    public AbstractInventory getInventory(String key) {
        return inventories.get(key);
    }

    public void onDisable() {
        inventories.values().forEach(abstractInventory -> {
            for (UUID uuid : abstractInventory.getOpenInventories()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null)
                    player.closeInventory();
            }
            abstractInventory.getOpenInventories().clear();
        });
        inventories.clear();
    }
}
