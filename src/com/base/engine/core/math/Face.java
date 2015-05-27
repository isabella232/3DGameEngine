package com.base.engine.core.math;

public class Face {
	private final Vertex[] vx = new Vertex[3];

	public Face(Vertex v1, Vertex v2, Vertex v3) {
		this.vx[0] = v1;
		this.vx[1] = v2;
		this.vx[2] = v3;
	}

	public Vertex getVertex(int index) {
		return this.vx[index];
	}

	public void setVertex(int index, Vertex v) {
		this.vx[index] = v;
	}
}
