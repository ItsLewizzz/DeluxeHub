package fun.lewisdev.deluxehub.hook;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.hook.hooks.head.BaseHead;
import fun.lewisdev.deluxehub.hook.hooks.head.DatabaseHead;
import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HooksManager {

    private Map<String, PluginHook> hooks;

    public HooksManager(DeluxeHubPlugin plugin) {
        hooks = new HashMap<>();

        // Base64 head
        hooks.put("BASE64", new BaseHead());

        // PlaceholderAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            hooks.put("PLACEHOLDER_API", null);
            PlaceholderUtil.PAPI = true;
            plugin.getLogger().info(" Hooked into PlaceholderAPI");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            hooks.put("HEAD_DATABASE", new DatabaseHead());
            plugin.getLogger().info(" Hooked into HeadDatabase");
        }

        hooks.values().stream().filter(Objects::nonNull).forEach(pluginHook -> pluginHook.onEnable(plugin));

    }

    public boolean isHookEnabled(String id) {
        return hooks.containsKey(id);
    }

    public PluginHook getPluginHook(String id) {
        return hooks.get(id);
    }

}
