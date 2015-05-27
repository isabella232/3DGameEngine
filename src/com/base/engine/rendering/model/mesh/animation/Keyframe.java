package com.base.engine.rendering.model.mesh.animation;

public class Keyframe {
	float tX, tY, tZ;
	float rX, rY, rZ;
	float sX, sY, sZ;
	float rAngle;

	public Keyframe() {
	}

	public void setRotate(float x, float y, float z, float angle) {
		this.rX = x;
		this.rY = y;
		this.rZ = z;
		this.rAngle = angle;
	}

	public void setScale(float x, float y, float z) {
		this.sX = x;
		this.sY = y;
		this.sZ = z;
	}

	public void setTranslate(float x, float y, float z) {
		this.tX = x;
		this.tY = y;
		this.tZ = z;
	}

}
