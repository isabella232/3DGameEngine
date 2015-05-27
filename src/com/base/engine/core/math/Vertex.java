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

/**
 *
 * Basically a vertex. A point in 2d or 3d space.
 *
 */
public class Vertex {
	public static final int SIZE = 11;

	/**
	 * The position of the vertex
	 */
	private Vector3f pos;
	/**
	 * Location on a texture to pull data from to map to this point. Used for
	 * wrapping textures over objects
	 */
	private Vector2f texCoord;
	/**
	 * The direction of the vertex
	 */
	private Vector3f normal;
	/**
	 * A Tangent vector is typically regarded as one vector that exists within
	 * the surface's plane or which lies tangent to a reference point on a
	 * curved surface. Taken from: http://gamedev.stackexchange.com/a/51402
	 */
	private Vector3f tangent;

	/**
	 * Initialize a new {@link Vertex}
	 * 
	 * @param pos
	 *            - The position of the vertex
	 */
	public Vertex(final Vector3f pos) {
		this(pos, new Vector2f(0, 0));
	}

	/**
	 * Initialize a new {@link Vertex} with a texture coordinate
	 * 
	 * @param pos
	 *            - The position of the vertex
	 * @param texCoord
	 *            - Location on a texture to pull data from to map to this
	 *            point. Used for wrapping textures over objects
	 */
	public Vertex(final Vector3f pos, final Vector2f texCoord) {
		this(pos, texCoord, new Vector3f(0, 0, 0));
	}

	/**
	 * Initialize a new {@link Vertex} with a texture coordinate and a
	 * normalization
	 * 
	 * @param pos
	 *            - The position of the vertex
	 * @param texCoord
	 *            - Location on a texture to pull data from to map to this
	 *            point. Used for wrapping textures over objects
	 * @param normal
	 *            - The direction of the vertex
	 */
	public Vertex(final Vector3f pos, final Vector2f texCoord, final Vector3f normal) {
		this(pos, texCoord, normal, new Vector3f(0, 0, 0));
	}

	/**
	 *
	 * Initialize a new {@link Vertex} with a texture coordinate, normalization,
	 * and a tangent
	 * 
	 * @param pos
	 *            - The position of the vertex
	 * @param texCoord
	 *            - Location on a texture to pull data from to map to this
	 *            point. Used for wrapping textures over objects
	 * @param normal
	 *            - The direction of the vertex
	 * @param tangent
	 *            - Vector that exists within the surface's plane or which lies
	 *            tangent to a reference point on a curved surface.
	 */
	public Vertex(final Vector3f pos, final Vector2f texCoord, final Vector3f normal, final Vector3f tangent) {
		this.pos = pos;
		this.texCoord = texCoord;
		this.normal = normal;
		this.tangent = tangent;
	}

	/**
	 * Get the tangent
	 * 
	 * @return Vector that exists within the surface's plane or which lies
	 *         tangent to a reference point on a curved surface.
	 */
	public Vector3f getTangent() {
		return tangent;
	}

	/**
	 * Set the tangent
	 * 
	 * @param tangent
	 *            - Vector that exists within the surface's plane or which lies
	 *            tangent to a reference point on a curved surface.
	 */
	public void setTangent(final Vector3f tangent) {
		this.tangent = tangent;
	}

	/**
	 * Get the position
	 * 
	 * @return The position of the vertex
	 */
	public Vector3f getPos() {
		return pos;
	}

	/**
	 * Set the position
	 * 
	 * @param pos
	 *            - The position of the vertex
	 */
	public void setPos(final Vector3f pos) {
		this.pos = pos;
	}

	/**
	 * Get the texture coordinate
	 * 
	 * @return Location on a texture to pull data from to map to this point.
	 *         Used for wrapping textures over objects
	 */
	public Vector2f getTexCoord() {
		return texCoord;
	}

	/**
	 * Set the texture coordinate
	 * 
	 * @param texCoord
	 *            - Location on a texture to pull data from to map to this
	 *            point. Used for wrapping textures over objects
	 */
	public void setTexCoord(final Vector2f texCoord) {
		this.texCoord = texCoord;
	}

	/**
	 * Get the normal
	 * 
	 * @return The direction of the vertex
	 */
	public Vector3f getNormal() {
		return normal;
	}

	/**
	 * Get the normal
	 * 
	 * @param normal
	 *            - The direction of the vertex
	 */
	public void setNormal(final Vector3f normal) {
		this.normal = normal;
	}
}
