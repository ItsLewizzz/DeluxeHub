package fun.lewisdev.deluxehub.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private final JavaPlugin plugin;
    private final String name;
    private final File file;

    private FileConfiguration configuration;

    public ConfigHandler(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.file = new File(plugin.getDataFolder(), this.name);
    }

    public ConfigHandler(JavaPlugin plugin, String path, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.file = new File(plugin.getDataFolder() + path, this.name);
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        if (configuration == null || file == null) return;
        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return configuration;
    }
}
