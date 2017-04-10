package net.edge.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class NamedThreadFactory implements ThreadFactory {

	/**
	 * The unique name.
	 */
	private final String name;
	/**
	 * The nextNode nodeId.
	 */
	private final AtomicInteger id = new AtomicInteger(0);

	/**
	 * Creates the named thread factory.
	 * @param name The unique name.
	 */
	NamedThreadFactory(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.node.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(Runnable runnable) {
		final int currentId = id.getAndIncrement();
		return new Thread(runnable, name + " [nodeId=" + currentId + "]");
	}
}
