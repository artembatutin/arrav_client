package net.arrav.game.model;

import net.arrav.cache.unit.ObjectType;

public final class ObjectStack extends Entity {

	public int id;
	public int x;
	public int y;
	public int amount;

	public ObjectStack() {
	}

	@Override
	public final Model getTransformedModel() {
		final ObjectType obj = ObjectType.get(id);
		return obj.getAmountModel(amount);
	}
}
