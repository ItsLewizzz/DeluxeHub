package fun.lewisdev.deluxehub.action.actions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.action.Action;
import fun.lewisdev.deluxehub.config.Messages;
import org.bukkit.entity.Player;

public class BungeeAction implements Action {

    @Override
    public String getIdentifier() {
        return "BUNGEE";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        String[] args = data.split(";");
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(args[0]);
        if (player.hasPermission(args[1]))
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        else Messages.NO_PERMISSION.send(player);
    }
}
