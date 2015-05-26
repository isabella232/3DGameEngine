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
 * Basically a vertex. 
 * A point in 2d or 3d space.
 *
 */
public class Vertex
{
	public static final int SIZE = 11;
	
	/**
	 * The position of the vertex 
	 */
	private Vector3f m_pos;
	/**
	 * Location on a texture to pull data from to map to this point.
	 * Used for wrapping textures over objects  
	 */
	private Vector2f m_texCoord;
	/**
	 * The direction of the vertex
	 */
	private Vector3f m_normal;
	/**
	 * A Tangent vector is typically regarded as one vector that exists within the surface's plane or which lies tangent to a reference point on a curved surface.
	 * Taken from: http://gamedev.stackexchange.com/a/51402
	 */
	private Vector3f m_tangent;
	
	/**
	 * Initialize a new {@link Vertex} 
	 * @param pos - The position of the vertex
	 */
	public Vertex(Vector3f pos)
	{
		this(pos, new Vector2f(0,0));
	}
	
	/**
	 * Initialize a new {@link Vertex} with a texture coordinate
	 * @param pos - The position of the vertex
	 * @param texCoord - Location on a texture to pull data from to map to this point. Used for wrapping textures over objects  
	 */
	public Vertex(Vector3f pos, Vector2f texCoord)
	{
		this(pos, texCoord, new Vector3f(0,0,0));
	}
	
	/**
	 * Initialize a new {@link Vertex} with a texture coordinate and a normalization
	 * @param pos - The position of the vertex
	 * @param texCoord - Location on a texture to pull data from to map to this point. Used for wrapping textures over objects  
	 * @param normal - The direction of the vertex
	 */
	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal)
	{
		this(pos, texCoord, normal, new Vector3f(0,0,0));
	}
	
	/**
	 * 
	 * Initialize a new {@link Vertex} with a texture coordinate, normalization, and a tangent
	 * @param pos - The position of the vertex
	 * @param texCoord - Location on a texture to pull data from to map to this point. Used for wrapping textures over objects  
	 * @param normal - The direction of the vertex
	 * @param tangent - Vector that exists within the surface's plane or which lies tangent to a reference point on a curved surface. 
	 */
	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal, Vector3f tangent)
	{
		this.m_pos = pos;
		this.m_texCoord = texCoord;
		this.m_normal = normal;
		this.m_tangent = tangent;
	}

	/**
	 * Get the tangent
	 * @return Vector that exists within the surface's plane or which lies tangent to a reference point on a curved surface.
	 */
	public Vector3f GetTangent() {
		return m_tangent;
	}

	/**
	 * Set the tangent
	 * @param tangent - Vector that exists within the surface's plane or which lies tangent to a reference point on a curved surface.
	 */
	public void SetTangent(Vector3f tangent) {
		this.m_tangent = tangent;
	}

	/**
	 * Get the position 
	 * @return The position of the vertex
	 */
	public Vector3f GetPos()
	{
		return m_pos;
	}

	/**
	 * Set the position
	 * @param pos - The position of the vertex
	 */
	public void SetPos(Vector3f pos)
	{
		this.m_pos = pos;
	}

	/**
	 * Get the texture coordinate
	 * @return Location on a texture to pull data from to map to this point. Used for wrapping textures over objects
	 */
	public Vector2f GetTexCoord()
	{
		return m_texCoord;
	}

	/**
	 * Set the texture coordinate
	 * @param texCoord - Location on a texture to pull data from to map to this point. Used for wrapping textures over objects
	 */
	public void SetTexCoord(Vector2f texCoord)
	{
		this.m_texCoord = texCoord;
	}

	/**
	 * Get the normal 
	 * @return The direction of the vertex
	 */
	public Vector3f GetNormal()
	{
		return m_normal;
	}

	/**
	 * Get the normal
	 * @param normal - The direction of the vertex
	 */
	public void SetNormal(Vector3f normal)
	{
		this.m_normal = normal;
	}
}
