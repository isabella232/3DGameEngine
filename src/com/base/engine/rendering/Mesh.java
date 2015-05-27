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

package com.base.engine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.base.engine.core.Util;
import com.base.engine.core.math.Vector3f;
import com.base.engine.core.math.Vertex;
import com.base.engine.rendering.resourceManagement.MeshResource;

public class Mesh extends MeshResource {

	public Mesh(final Vertex[] vertices, final int[] indices, final boolean calcNormals) {
		super(vertices.length);
		addVertices(vertices, indices, calcNormals);
	}

	private void addVertices(final Vertex[] vertices, final int[] indices, final boolean calcNormals) {
		
		if (calcNormals) {
			calcNormals(vertices, indices);
		}

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, getVbo());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, getIbo());
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}

	public void draw() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, getVbo());
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 12);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 20);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 32);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, getIbo());
		GL11.glDrawElements(GL11.GL_TRIANGLES, getSize(), GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
	}

	private void calcNormals(final Vertex[] vertices, final int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			final int i0 = indices[i];
			final int i1 = indices[i + 1];
			final int i2 = indices[i + 2];

			final Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			final Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());

			final Vector3f normal = v1.cross(v2).normalized();

			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}

		for (final Vertex vertice : vertices) {
			vertice.setNormal(vertice.getNormal().normalized());
		}
	}

}
