package net.arrav.world.model;

/**
 * Created by artembatutin on 6/19/17.
 */
public final class Item {
	
	/**
	 * The identification of this item.
	 */
	private final int id;
	
	/**
	 * The quantity of this item.
	 */
	private final int amount;
	
	/**
	 * Creates a new {@link Item}.
	 * @param id the identification of this item.
	 */
	public Item(int id) {
		this.id = id;
		this.amount = 1;
	}
	
	/**
	 * Creates a new {@link Item}.
	 * @param id     the identification of this item.
	 * @param amount the quantity of this item.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
	
	/**
	 * Gets the identification of this item.
	 * @return the identification.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Gets the quantity of this item.
	 * @return the quantity.
	 */
	public final int getAmount() {
		return amount;
	}
}
