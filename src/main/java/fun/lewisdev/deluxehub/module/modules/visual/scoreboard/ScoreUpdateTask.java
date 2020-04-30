package fun.lewisdev.deluxehub.module.modules.visual.scoreboard;

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
