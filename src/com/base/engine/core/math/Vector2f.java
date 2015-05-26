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
	private float m_x;
	private float m_y;

	public Vector2f(final float x, final float y) {
		m_x = x;
		m_y = y;
	}

	public float Length() {
		return (float) Math.sqrt(m_x * m_x + m_y * m_y);
	}

	public float Max() {
		return Math.max(m_x, m_y);
	}

	public float Dot(final Vector2f r) {
		return m_x * r.GetX() + m_y * r.GetY();
	}

	public Vector2f Normalized() {
		final float length = Length();

		return new Vector2f(m_x / length, m_y / length);
	}

	public float Cross(final Vector2f r) {
		return m_x * r.GetY() - m_y * r.GetX();
	}

	public Vector2f Lerp(final Vector2f dest, final float lerpFactor) {
		return dest.Sub(this).Mul(lerpFactor).Add(this);
	}

	public Vector2f Rotate(final float angle) {
		final double rad = Math.toRadians(angle);
		final double cos = Math.cos(rad);
		final double sin = Math.sin(rad);

		return new Vector2f((float) (m_x * cos - m_y * sin), (float) (m_x * sin + m_y * cos));
	}

	public Vector2f Add(final Vector2f r) {
		return new Vector2f(m_x + r.GetX(), m_y + r.GetY());
	}

	public Vector2f Add(final float r) {
		return new Vector2f(m_x + r, m_y + r);
	}

	public Vector2f Sub(final Vector2f r) {
		return new Vector2f(m_x - r.GetX(), m_y - r.GetY());
	}

	public Vector2f Sub(final float r) {
		return new Vector2f(m_x - r, m_y - r);
	}

	public Vector2f Mul(final Vector2f r) {
		return new Vector2f(m_x * r.GetX(), m_y * r.GetY());
	}

	public Vector2f Mul(final float r) {
		return new Vector2f(m_x * r, m_y * r);
	}

	public Vector2f Div(final Vector2f r) {
		return new Vector2f(m_x / r.GetX(), m_y / r.GetY());
	}

	public Vector2f Div(final float r) {
		return new Vector2f(m_x / r, m_y / r);
	}

	public Vector2f Abs() {
		return new Vector2f(Math.abs(m_x), Math.abs(m_y));
	}

	@Override
	public String toString() {
		return "(" + m_x + " " + m_y + ")";
	}

	public Vector2f Set(final float x, final float y) {
		m_x = x;
		m_y = y;
		return this;
	}

	public Vector2f Set(final Vector2f r) {
		Set(r.GetX(), r.GetY());
		return this;
	}

	public float GetX() {
		return m_x;
	}

	public void SetX(final float x) {
		m_x = x;
	}

	public float GetY() {
		return m_y;
	}

	public void SetY(final float y) {
		m_y = y;
	}

	public boolean equals(final Vector2f r) {
		return m_x == r.GetX() && m_y == r.GetY();
	}
}
