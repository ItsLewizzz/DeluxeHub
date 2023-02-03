package fun.lewisdev.deluxehub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.world.LobbySpawn;
import fun.lewisdev.deluxehub.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupLobbyCommand {

    private final DeluxeHubPlugin plugin;

    public SetupLobbyCommand(DeluxeHubPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"setuplobby"},
            desc = "Setup the lobby location"
    )
    public void setlobby(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(Permissions.COMMAND_SET_LOBBY.getPermission())) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot set the spawn location.");
            return;
        }

        Player player = (Player) sender;
        if (plugin.getModuleManager().getDisabledWorlds().contains(player.getWorld().getName())) {
            sender.sendMessage(TextUtil.color("&cYou cannot set the lobby location in a disabled world."));
            return;
        }
        
        if(args[0] == "set" || args[0] == "add"){
            LobbySpawn lobbyModule = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY));
            lobbyModule.setLocation(player.getLocation());
            Messages.SET_LOBBY.send(sender);
        } else if (args[0] == "remove" || args[0] == "delete") {
            LobbySpawn lobbyModule = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY));
            lobbyModule.removeLobby();
            Messages.REMOVE_LOBBY.send(sender);
        } else {
             Messages.USAGE_LOBBY.send(sender);
        }
    }

}
