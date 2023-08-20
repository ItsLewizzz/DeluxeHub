package fun.lewisdev.deluxehub.module.modules.chat;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import fun.lewisdev.deluxehub.utility.TextUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFormat extends Module {

    private DeluxeHubPlugin plugin;
    private List<String> groups;

    public ChatFormat(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.CHAT_FORMAT);

        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        groups = new ArrayList<>();
        loadGroups();
    }

    @Override
    public void onDisable() {

        groups.clear();
        loadGroups();

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String message = event.getMessage();
        Player player = event.getPlayer();

        String format = getPlayerFormat(player);

        if(format != null){
            format = PlaceholderUtil.setPlaceholders(format, player);
            format = TextUtil.color(format);

            if(player.hasPermission(Permissions.COLOR_CHAT.getPermission()))
                message = TextUtil.color(message);

            event.setFormat(format.replace("%message%", message));
            return;
        }

        plugin.getLogger().severe("============= DELUXEHUB CHAT FORMAT =============");
        plugin.getLogger().severe("Can't find the default chat formatting!");
        plugin.getLogger().severe("Create it in the configuration file");
        plugin.getLogger().severe("============= DELUXEHUB CHAT FORMAT =============");

    }

    private String getPlayerGroup(Player player){
        for (String group : groups){
            if(player.hasPermission(Permissions.GROUP_CHAT.getPermission() + "." + group)){
                return group;
            }
        }
        return "default";
    }

    private String getPlayerFormat(Player player){
        return getConfig(ConfigType.SETTINGS).getString("chat_format.group_formats." + getPlayerGroup(player));
    }

    private void loadGroups(){
        ConfigurationSection section = getConfig(ConfigType.SETTINGS).getConfigurationSection("chat_format.group_formats");
        groups.addAll(section.getKeys(false));
    }

}
