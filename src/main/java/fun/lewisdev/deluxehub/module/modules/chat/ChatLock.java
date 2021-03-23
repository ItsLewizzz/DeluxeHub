package fun.lewisdev.deluxehub.module.modules.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;

public class ChatLock extends Module {

    private boolean isChatLocked;

    public ChatLock(DeluxeHub plugin) {
        super(plugin, ModuleType.CHAT_LOCK);
    }

    @Override
    public void onEnable() {
        isChatLocked = getPlugin().getConfigManager().getFile(ConfigType.DATA).getConfig().getBoolean("chat_locked");
    }

    @Override
    public void onDisable() {
        getPlugin().getConfigManager().getFile(ConfigType.DATA).getConfig().set("chat_locked", isChatLocked);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!isChatLocked || player.hasPermission(Permissions.LOCK_CHAT_BYPASS.getPermission()))
            return;

        event.setCancelled(true);
        player.sendMessage(Messages.CHAT_LOCKED.toString());
    }

    public boolean isChatLocked() {
        return isChatLocked;
    }

    public void setChatLocked(boolean chatLocked) {
        isChatLocked = chatLocked;
    }
}
