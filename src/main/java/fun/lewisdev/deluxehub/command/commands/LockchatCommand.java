package fun.lewisdev.deluxehub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.chat.ChatLock;
import org.bukkit.command.CommandSender;

public class LockchatCommand {

    private DeluxeHub plugin;

    public LockchatCommand(DeluxeHub plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"lockchat"},
            desc = "Locks global chat"
    )
    public void lockchat(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(Permissions.COMMAND_LOCKCHAT.getPermission())) {
            sender.sendMessage(Messages.NO_PERMISSION.toString());
            return;
        }

        ChatLock chatLockModule = (ChatLock) plugin.getModuleManager().getModule(ModuleType.CHAT_LOCK);

        if (chatLockModule.isChatLocked()) {
            plugin.getServer().broadcastMessage(Messages.CHAT_UNLOCKED_BROADCAST.toString().replace("%player%", sender.getName()));
            chatLockModule.setChatLocked(false);
        } else {
            plugin.getServer().broadcastMessage(Messages.CHAT_LOCKED_BROADCAST.toString().replace("%player%", sender.getName()));
            chatLockModule.setChatLocked(true);
        }
    }
}
