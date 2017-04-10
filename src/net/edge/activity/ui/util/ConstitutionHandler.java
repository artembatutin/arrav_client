package net.edge.activity.ui.util;

public final class ConstitutionHandler {

	/**
	 * An instance of {@link ConstitutionHandler}, the singleton.
	 */
	private static final ConstitutionHandler instance = new ConstitutionHandler();

	/**
	 * Returns the singleton instance.
	 * @return The singleton instance.
	 */
	public static ConstitutionHandler getInstance() {
		return instance;
	}

	/**
	 * The amount of constitution.
	 */
	private short amount;
	/**
	 * The maximum amount of constitution.
	 */
	private short maxAmount;

	/**
	 * Returns the amount of constitution.
	 * @return The constitution amount.
	 */
	public final int getAmount() {
		return amount;
	}

	/**
	 * Sets the {@link #amount} <code>int</code> to that of a new value.
	 * @param amount The new amount.
	 */
	public final void setAmount(int amount) {
		this.amount = (short) amount;
	}

	/**
	 * Returns the maximum amount of constitution.
	 * @return the maxAmount	The maximum constitution amount.
	 */
	public int getMaxAmount() {
		return maxAmount;
	}

	/**
	 * Sets the {@link #maxAmount} <code>int</code> to that of a new value.
	 * @param maxAmount The new maximum amount.
	 */
	public void setMaxAmount(int maxAmount) {
		this.maxAmount = (short) maxAmount;
	}

	/**
	 * Constructs a new {@link ConstitutionHandler}.
	 * This default private constructor is used to prevent instantiation by other classes.
	 */
	private ConstitutionHandler() {
		super();
	}
}