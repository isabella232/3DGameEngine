package com.base.engine.rendering.animation;

public class VertexDeformation {
	private int vertexId;
	private float weight;

	public VertexDeformation(int vertex, float weight) {
		this.vertexId = vertex;
		this.weight = weight;
	}

	public int getVertexId() {
		return vertexId;
	}

	public void setVertex(int vertexId) {
		this.vertexId = vertexId;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
}
