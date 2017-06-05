package net.edge.activity.panel;

import net.edge.activity.Activity;

public abstract class Panel extends Activity {

	public abstract int getId();
	
	public abstract boolean blockedMove();
	
	public boolean on() {
		int x = 8;
		int y = 8;
		if(client.uiRenderer.isResizableOrFull()) {
			x = client.windowWidth / 2 - 380;
			y = client.windowHeight / 2 - 250;
		}
		return client.mouseInRegion(x, y, x + 500, y + 336);
	}

}
