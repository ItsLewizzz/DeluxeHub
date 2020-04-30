package fun.lewisdev.deluxehub.module.modules.visual.tablist;

public class TablistUpdateTask implements Runnable {

	private TablistManager tablistManager;

	public TablistUpdateTask(TablistManager tablistManager) {
		this.tablistManager = tablistManager;
	}

	@Override
	public void run() {
		tablistManager.getPlayers().forEach(uuid -> tablistManager.updateTablist(uuid));
	}

}
