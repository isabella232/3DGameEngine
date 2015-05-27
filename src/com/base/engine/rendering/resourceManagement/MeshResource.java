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
	private final int vbo;
	private final int ibo;
	private final int size;

	public MeshResource(final int size) {
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		this.size = size;
		addReference();
	}

	@Override
	protected void finalize() {
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
	}

	public int getVbo() {
		return vbo;
	}

	public int getIbo() {
		return ibo;
	}

	public int getSize() {
		return size;
	}
}
