package fun.lewisdev.deluxehub.module;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.modules.chat.*;
import fun.lewisdev.deluxehub.module.modules.hologram.HologramManager;
import fun.lewisdev.deluxehub.module.modules.hotbar.HotbarManager;
import fun.lewisdev.deluxehub.module.modules.player.PlayerListener;
import fun.lewisdev.deluxehub.module.modules.player.PlayerVanish;
import fun.lewisdev.deluxehub.module.modules.world.AntiWorldDownloader;
import fun.lewisdev.deluxehub.module.modules.player.DoubleJump;
import fun.lewisdev.deluxehub.module.modules.world.Launchpad;
import fun.lewisdev.deluxehub.module.modules.visual.scoreboard.ScoreboardManager;
import fun.lewisdev.deluxehub.module.modules.visual.tablist.TablistManager;
import fun.lewisdev.deluxehub.module.modules.world.LobbySpawn;
import fun.lewisdev.deluxehub.module.modules.world.WorldProtect;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

    private List<String> disabledWorlds;
    private Map<ModuleType, Module> modules = new HashMap<>();

    public void loadModules(DeluxeHub plugin) {
        if(!modules.isEmpty()) unloadModules();

        FileConfiguration config = plugin.getConfigManager().getFile(ConfigType.SETTINGS).getConfig();
        if(config.getBoolean("disabled-worlds.invert")) {
            for(World world : Bukkit.getWorlds()) {
                if(!config.getStringList("disabled-worlds.worlds").contains(world.getName())) disabledWorlds.add(world.getName());
            }
        }else{
            disabledWorlds = config.getStringList("disabled-worlds.worlds");
        }

        registerModule(new AntiWorldDownloader(plugin), "anti_wdl.enabled");
        registerModule(new DoubleJump(plugin),"double_jump.enabled");
        registerModule(new Launchpad(plugin), "launchpad.enabled");
        registerModule(new ScoreboardManager(plugin), "scoreboard.enabled");
        registerModule(new TablistManager(plugin), "tablist.enabled");
        registerModule(new AutoBroadcast(plugin), "announcements.enabled");
        registerModule(new AntiSwear(plugin), "anti_swear.enabled");
        registerModule(new ChatCommandBlock(plugin), "command_block.enabled");
        registerModule(new ChatLock(plugin));
        registerModule(new CustomCommands(plugin));
        registerModule(new PlayerListener(plugin));
        registerModule(new HotbarManager(plugin));
        registerModule(new HologramManager(plugin));
        registerModule(new WorldProtect(plugin));
        registerModule(new LobbySpawn(plugin));
        registerModule(new PlayerVanish(plugin));

        modules.values().forEach(module -> {
            try {
                module.setDisabledWorlds(disabledWorlds);
                module.onEnable();
            }catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("There was an error loading the " + module.getModuleType().toString() + " module.");
                modules.remove(module.getModuleType());
            }
        });

        plugin.getLogger().info("Loaded " + modules.size() + " plugin modules.");
    }

    public void unloadModules() {
        modules.values().forEach(module -> {
            module.onDisable();
            HandlerList.unregisterAll(module);
        });
        modules.clear();
    }

    public Module getModule(ModuleType type) {
        return modules.get(type);
    }

    public void registerModule(Module module) {
        registerModule(module, null);
    }

    public void registerModule(Module module, String isEnabledPath) {
        DeluxeHub plugin = module.getPlugin();

        if(isEnabledPath != null) if(!plugin.getConfigManager().getFile(ConfigType.SETTINGS).getConfig().getBoolean(isEnabledPath)) return;

        plugin.getServer().getPluginManager().registerEvents(module, plugin);
        modules.put(module.getModuleType(), module);
    }

    public boolean isEnabled(ModuleType type) {
        return modules.containsKey(type);
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }
}
