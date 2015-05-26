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

package com.base.engine.rendering.resourceManagement;

import org.lwjgl.opengl.GL15;

public class MeshResource extends ReferenceCounter {
	private final int m_vbo;
	private final int m_ibo;
	private final int m_size;

	public MeshResource(final int size) {
		m_vbo = GL15.glGenBuffers();
		m_ibo = GL15.glGenBuffers();
		m_size = size;
		AddReference();
	}

	@Override
	protected void finalize() {
		GL15.glDeleteBuffers(m_vbo);
		GL15.glDeleteBuffers(m_ibo);
	}

	public int GetVbo() {
		return m_vbo;
	}

	public int GetIbo() {
		return m_ibo;
	}

	public int GetSize() {
		return m_size;
	}
}
