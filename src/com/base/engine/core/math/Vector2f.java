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

public class Vector2f {
	private float x;
	private float y;

	public Vector2f(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float max() {
		return Math.max(x, y);
	}

	public float dot(final Vector2f r) {
		return x * r.getX() + y * r.getY();
	}

	public Vector2f normalized() {
		final float length = length();

		return new Vector2f(x / length, y / length);
	}

	public float cross(final Vector2f r) {
		return x * r.getY() - y * r.getX();
	}

	public Vector2f lerp(final Vector2f dest, final float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector2f rotate(final float angle) {
		final double rad = Math.toRadians(angle);
		final double cos = Math.cos(rad);
		final double sin = Math.sin(rad);

		return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	public Vector2f add(final Vector2f r) {
		return new Vector2f(x + r.getX(), y + r.getY());
	}

	public Vector2f add(final float r) {
		return new Vector2f(x + r, y + r);
	}

	public Vector2f sub(final Vector2f r) {
		return new Vector2f(x - r.getX(), y - r.getY());
	}

	public Vector2f sub(final float r) {
		return new Vector2f(x - r, y - r);
	}

	public Vector2f mul(final Vector2f r) {
		return new Vector2f(x * r.getX(), y * r.getY());
	}

	public Vector2f mul(final float r) {
		return new Vector2f(x * r, y * r);
	}

	public Vector2f div(final Vector2f r) {
		return new Vector2f(x / r.getX(), y / r.getY());
	}

	public Vector2f div(final float r) {
		return new Vector2f(x / r, y / r);
	}

	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	@Override
	public String toString() {
		return "(" + x + " " + y + ")";
	}

	public Vector2f set(final float x, final float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2f set(final Vector2f r) {
		set(r.getX(), r.getY());
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

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Vector2f) {
			return equals((Vector2f) obj);
		}
		
		return super.equals(obj);
	}
	
	public boolean equals(final Vector2f r) {
		return x == r.getX() && y == r.getY();
	}
}
