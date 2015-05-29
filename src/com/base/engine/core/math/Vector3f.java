/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.engine.core.math;

public class Vector3f {
	private float x;
	private float y;
	private float z;

	public Vector3f(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	public float dot(final Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}

	public Vector3f cross(final Vector3f r) {
		final float x_ = y * r.getZ() - z * r.getY();
		final float y_ = z * r.getX() - x * r.getZ();
		final float z_ = x * r.getY() - y * r.getX();

		return new Vector3f(x_, y_, z_);
	}

	public Vector3f normalized() {
		final float length = length();

		return new Vector3f(x / length, y / length, z / length);
	}

	public Vector3f rotate(final Vector3f axis, final float angle) {
		final float sinAngle = (float) Math.sin(-angle);
		final float cosAngle = (float) Math.cos(-angle);

		return cross(axis.mul(sinAngle)).add( // Rotation on local X
				this.mul(cosAngle).add( // Rotation on local Z
						axis.mul(dot(axis.mul(1 - cosAngle))))); // Rotation on
																	// local Y
	}

	public Vector3f rotate(final Quaternion rotation) {
		final Quaternion conjugate = rotation.conjugate();

		final Quaternion w = rotation.mul(this).mul(conjugate);

		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}

	public Vector3f lerp(final Vector3f dest, final float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector3f add(final Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}

	public Vector3f add(final float r) {
		return new Vector3f(x + r, y + r, z + r);
	}

	public Vector3f sub(final Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}

	public Vector3f sub(final float r) {
		return new Vector3f(x - r, y - r, z - r);
	}

	public Vector3f mul(final Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}

	public Vector3f mul(final float r) {
		return new Vector3f(x * r, y * r, z * r);
	}

	public Vector3f div(final Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}

	public Vector3f div(final float r) {
		return new Vector3f(x / r, y / r, z / r);
	}

	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	@Override
	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}

	public Vector2f getXY() {
		return new Vector2f(x, y);
	}

	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}

	public Vector2f getZX() {
		return new Vector2f(z, x);
	}

	public Vector2f getYX() {
		return new Vector2f(y, x);
	}

	public Vector2f getZY() {
		return new Vector2f(z, y);
	}

	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}

	public Vector3f set(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3f set(final Vector3f r) {
		set(r.getX(), r.getY(), r.getZ());
		return this;
	}

	public float getX() {
		return x;
	}

	public void setX(final float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(final float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(final float z) {
		this.z = z;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector3f) {
			return equals((Vector3f) obj);
		}
		return super.equals(obj);
	}

	private boolean equals(final Vector3f r) {
		return x == r.getX() && y == r.getY() && z == r.getZ();
	}
}
