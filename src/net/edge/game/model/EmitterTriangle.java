package net.edge.game.model;

public final class EmitterTriangle {

	public final int type;
	public final int vertexA;
	public final int vertexB;
	public final int vertexC;
	public final int priority;

	public EmitterTriangle(int type, int vertexA, int vertexB, int vertexC, int priority) {
		this.type = type;
		this.vertexA = vertexA;
		this.vertexB = vertexB;
		this.vertexC = vertexC;
		this.priority = priority;
	}
}
