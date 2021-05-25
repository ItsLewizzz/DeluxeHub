package fun.lewisdev.deluxehub.action.actions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.action.Action;

public class BungeeAction implements Action {

    @Override
    public String getIdentifier() {
        return "BUNGEE";
    }

    @Override
    public void execute(DeluxeHub plugin, Player player, String data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(data);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
