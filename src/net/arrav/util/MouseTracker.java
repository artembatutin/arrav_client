package net.arrav.util;

import net.arrav.Client;

public final class MouseTracker implements Runnable {

	/*
	 * Declared fields.
	 */
	private final Client client;
	public final int[] mouseX;
	public final int[] mouseY;
	public int index;
	public final Object synchronizer;
	public boolean running;

	/*
	 * Declared Constructor.
	 */
	public MouseTracker(Client client) {
		this.client = client;
		mouseX = new int[500];
		mouseY = new int[500];
		synchronizer = new Object();
		running = true;
	}

	/*
	 * Runnable mouse detection.
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(running) {
			synchronized(synchronizer) {
				if(index < 500) {
					mouseX[index] = client.mouseX;
					mouseY[index] = client.mouseY;
					index++;
				}
			}
			try {
				Thread.sleep(50L);
			} catch(final Exception ignored) {
				//empty
			}
		}
	}
}