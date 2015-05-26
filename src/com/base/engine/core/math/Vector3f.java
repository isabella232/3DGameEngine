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
	private float m_x;
	private float m_y;
	private float m_z;

	public Vector3f(final float x, final float y, final float z) {
		m_x = x;
		m_y = y;
		m_z = z;
	}

	public float Length() {
		return (float) Math.sqrt(m_x * m_x + m_y * m_y + m_z * m_z);
	}

	public float Max() {
		return Math.max(m_x, Math.max(m_y, m_z));
	}

	public float Dot(final Vector3f r) {
		return m_x * r.GetX() + m_y * r.GetY() + m_z * r.GetZ();
	}

	public Vector3f Cross(final Vector3f r) {
		final float x_ = m_y * r.GetZ() - m_z * r.GetY();
		final float y_ = m_z * r.GetX() - m_x * r.GetZ();
		final float z_ = m_x * r.GetY() - m_y * r.GetX();

		return new Vector3f(x_, y_, z_);
	}

	public Vector3f Normalized() {
		final float length = Length();

		return new Vector3f(m_x / length, m_y / length, m_z / length);
	}

	public Vector3f Rotate(final Vector3f axis, final float angle) {
		final float sinAngle = (float) Math.sin(-angle);
		final float cosAngle = (float) Math.cos(-angle);

		return Cross(axis.Mul(sinAngle)).Add( // Rotation on local X
				this.Mul(cosAngle).Add( // Rotation on local Z
						axis.Mul(Dot(axis.Mul(1 - cosAngle))))); // Rotation on
																	// local Y
	}

	public Vector3f Rotate(final Quaternion rotation) {
		final Quaternion conjugate = rotation.Conjugate();

		final Quaternion w = rotation.Mul(this).Mul(conjugate);

		return new Vector3f(w.GetX(), w.GetY(), w.GetZ());
	}

	public Vector3f Lerp(final Vector3f dest, final float lerpFactor) {
		return dest.Sub(this).Mul(lerpFactor).Add(this);
	}

	public Vector3f Add(final Vector3f r) {
		return new Vector3f(m_x + r.GetX(), m_y + r.GetY(), m_z + r.GetZ());
	}

	public Vector3f Add(final float r) {
		return new Vector3f(m_x + r, m_y + r, m_z + r);
	}

	public Vector3f Sub(final Vector3f r) {
		return new Vector3f(m_x - r.GetX(), m_y - r.GetY(), m_z - r.GetZ());
	}

	public Vector3f Sub(final float r) {
		return new Vector3f(m_x - r, m_y - r, m_z - r);
	}

	public Vector3f Mul(final Vector3f r) {
		return new Vector3f(m_x * r.GetX(), m_y * r.GetY(), m_z * r.GetZ());
	}

	public Vector3f Mul(final float r) {
		return new Vector3f(m_x * r, m_y * r, m_z * r);
	}

	public Vector3f Div(final Vector3f r) {
		return new Vector3f(m_x / r.GetX(), m_y / r.GetY(), m_z / r.GetZ());
	}

	public Vector3f Div(final float r) {
		return new Vector3f(m_x / r, m_y / r, m_z / r);
	}

	public Vector3f Abs() {
		return new Vector3f(Math.abs(m_x), Math.abs(m_y), Math.abs(m_z));
	}

	@Override
	public String toString() {
		return "(" + m_x + " " + m_y + " " + m_z + ")";
	}

	public Vector2f GetXY() {
		return new Vector2f(m_x, m_y);
	}

	public Vector2f GetYZ() {
		return new Vector2f(m_y, m_z);
	}

	public Vector2f GetZX() {
		return new Vector2f(m_z, m_x);
	}

	public Vector2f GetYX() {
		return new Vector2f(m_y, m_x);
	}

	public Vector2f GetZY() {
		return new Vector2f(m_z, m_y);
	}

	public Vector2f GetXZ() {
		return new Vector2f(m_x, m_z);
	}

	public Vector3f Set(final float x, final float y, final float z) {
		m_x = x;
		m_y = y;
		m_z = z;
		return this;
	}

	public Vector3f Set(final Vector3f r) {
		Set(r.GetX(), r.GetY(), r.GetZ());
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

	public float GetZ() {
		return m_z;
	}

	public void SetZ(final float z) {
		m_z = z;
	}

	public boolean equals(final Vector3f r) {
		return m_x == r.GetX() && m_y == r.GetY() && m_z == r.GetZ();
	}
}
