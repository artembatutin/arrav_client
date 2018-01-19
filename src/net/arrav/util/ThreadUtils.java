package net.arrav.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class ThreadUtils {

	/**
	 * Creates a single thread.
	 * @param name The name of the thread factory group.
	 * @return The threads within a executor.
	 */
	public static ScheduledExecutorService createScheduledThread(String name) {
		final NamedThreadFactory factory = new NamedThreadFactory(name);
		return Executors.newSingleThreadScheduledExecutor(factory);
	}

	/**
	 * Creates multiple threads based on the processor.
	 * @param name The name of the thread factory group.
	 * @return The threads within a executor.
	 */
	public static ScheduledExecutorService createScheduledThreads(String name) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final NamedThreadFactory factory = new NamedThreadFactory(name);
		return Executors.newScheduledThreadPool(processors, factory);
	}

	/**
	 * Creates multiple threads based on the processor.
	 * @param name         The name of the thread factory group.
	 * @param multiplicity The multiplicity of the cores.
	 * @return The threads within a executor.
	 */
	public static ScheduledExecutorService createScheduledThreads(String name, int multiplicity) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final NamedThreadFactory factory = new NamedThreadFactory(name);
		return Executors.newScheduledThreadPool(processors * multiplicity, factory);
	}

	/**
	 * Creates a single thread.
	 * @param name The name of the thread factory group.
	 * @return The threads within a executor.
	 */
	public static ExecutorService createThread(String name) {
		final NamedThreadFactory factory = new NamedThreadFactory(name);
		return Executors.newSingleThreadExecutor(factory);
	}

	/**
	 * Creates multiple threads based on the processor.
	 * @param name The name of the thread factory group.
	 * @return The threads within a executor.
	 */
	public static ExecutorService createThreads(String name) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final NamedThreadFactory factory = new NamedThreadFactory(name);
		return Executors.newFixedThreadPool(processors, factory);
	}

	/**
	 * Creates multiply threads based on the multiplicity.
	 * @param name         The name of the thread factory group.
	 * @param multiplicity The multiplicity by the cores.
	 * @return The threads within a executor.
	 */
	public static ExecutorService createThreads(String name, int multiplicity) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final NamedThreadFactory factory = new NamedThreadFactory(name);
		return Executors.newFixedThreadPool(processors * multiplicity, factory);
	}

	/**
	 * Default constructor to prevent initiation.
	 */
	private ThreadUtils() {
	}
}
