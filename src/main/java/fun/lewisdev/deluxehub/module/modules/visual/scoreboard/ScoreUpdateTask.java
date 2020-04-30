package fun.lewisdev.deluxehub.module.modules.visual.scoreboard;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.visual.scoreboard.ScoreHelper;
import fun.lewisdev.deluxehub.module.modules.visual.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ScoreUpdateTask implements Runnable {

	private ScoreboardManager scoreboardManager;

	public ScoreUpdateTask(ScoreboardManager scoreboardManager) {
		this.scoreboardManager = scoreboardManager;
	}

	@Override
	public void run() {
		scoreboardManager.getPlayers().forEach(uuid -> scoreboardManager.updateScoreboard(uuid));
	}

}
