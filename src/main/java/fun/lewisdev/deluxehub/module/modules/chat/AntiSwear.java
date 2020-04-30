package fun.lewisdev.deluxehub.module.modules.chat;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class AntiSwear extends Module {

    private List<String> blockedWords;

    public AntiSwear(DeluxeHub plugin) {
        super(plugin, ModuleType.ANTI_SWEAR);
    }

    @Override
    public void onEnable() {
        blockedWords = getConfig(ConfigType.SETTINGS).getStringList("anti_swear.blocked_words");
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        if (player.hasPermission(Permissions.ANTI_SWEAR_BYPASS.getPermission())) return;

        String message = event.getMessage();

        for (String word : blockedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {

                event.setCancelled(true);
                player.sendMessage(Messages.ANTI_SWEAR_WORD_BLOCKED.toString());

                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(Permissions.ANTI_SWEAR_NOTIFY.getPermission())).forEach(p -> {
                    p.sendMessage(Messages.ANTI_SWEAR_ADMIN_NOTIFY.toString().replace("%player%", player.getName()).replace("%word%", message));
                });

                return;
            }
        }
    }
}
