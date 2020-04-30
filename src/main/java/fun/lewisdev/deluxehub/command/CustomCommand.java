package fun.lewisdev.deluxehub.command;

import java.util.ArrayList;
import java.util.List;

public class CustomCommand {

    private String permission;
    private List<String> aliases, actions;

    public CustomCommand(String command, List<String> actions) {
        this.aliases = new ArrayList<>();
        this.aliases.add(command);
        this.actions = actions;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void addAliases(List<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getActions() {
        return actions;
    }

}
