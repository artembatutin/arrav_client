package net.edge.activity.ui;

import net.edge.activity.ui.fixed.FixedUI;
import net.edge.activity.ui.resize.ResizableUI;

import java.util.HashMap;
import java.util.Map;

public final class UICache {

	private final Map<Integer, FixedUI> fixed = new HashMap<>();
	private final Map<Integer, ResizableUI> resizable = new HashMap<>();

	/**
	 * Registers a game frame with it's revision.
	 */
	public void register(int id, FixedUI frame) {
		fixed.put(id, frame);
	}

	/**
	 * Registers a game frame with it's revision.
	 */
	public void register(int id, ResizableUI frame) {
		resizable.put(id, frame);
	}

	/**
	 * Get the fixed game frame mode revision.
	 */
	public FixedUI getFixed(int id) {
		return fixed.get(id);
	}

	/**
	 * Get the resizable game frame mode revision.
	 */
	public ResizableUI getResizable(int id) {
		return resizable.get(id);
	}
}
